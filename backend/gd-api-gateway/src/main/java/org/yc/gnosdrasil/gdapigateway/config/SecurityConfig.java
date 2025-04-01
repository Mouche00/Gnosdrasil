package org.yc.gnosdrasil.gdapigateway.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPatternParser;

import org.yc.gnosdrasil.gdapigateway.exceptions.InvalidTokenException;
import org.yc.gnosdrasil.gdapigateway.exceptions.TokenExpiredException;
import org.yc.gnosdrasil.gdapigateway.security.JwtAuthenticationConverter;
import org.yc.gnosdrasil.gdapigateway.security.JwtReactiveAuthenticationManager;
import org.yc.gnosdrasil.gdapigateway.security.JwtUtils;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${cors.config.allowed.origin}")
    private String ALLOWED_ORIGIN;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        // Create authentication manager and converter
        ReactiveAuthenticationManager authenticationManager = new JwtReactiveAuthenticationManager(jwtUtils);
        ServerAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();

        // Create authentication web filter
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(authenticationConverter);

        // Define a matcher to exclude public and auth endpoints
        ServerWebExchangeMatcher matcher = new NegatedServerWebExchangeMatcher(
                new OrServerWebExchangeMatcher(new PathPatternParserServerWebExchangeMatcher("/api/auth/**")));

        return http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Arrays.asList(ALLOWED_ORIGIN));
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept",
                            "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers", "Cookie"));
                    config.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
                    config.setAllowCredentials(true);
                    config.setMaxAge(3600L);
                    return config;
                }))
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .pathMatchers("/api/auth/**").permitAll()
                        .anyExchange().authenticated())
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .securityMatcher(matcher)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((exchange, ex) -> {
                            if (ex instanceof TokenExpiredException) {
                                return handleTokenExpiredException(exchange, ex);
                            } else if (ex instanceof InvalidTokenException) {
                                return handleInvalidTokenException(exchange, ex);
                            } else {
                                return handleGenericException(exchange, ex);
                            }
                        })
                        .accessDeniedHandler((exchange, ex) -> {
                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                            String responseBody = "{\"error\": \"Access Denied\", \"message\": \"You don't have permission to access this resource\"}";
                            return exchange.getResponse()
                                    .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(responseBody.getBytes())));
                        }))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList(ALLOWED_ORIGIN));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin",
                "Access-Control-Request-Method", "Access-Control-Request-Headers", "Cookie"));
        config.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/api/**", config);

        return new CorsWebFilter(source);
    }

    private Mono<Void> handleTokenExpiredException(ServerWebExchange exchange, Throwable ex) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String responseBody = "{\"error\": \"Token has expired\", \"message\": \"" + ex.getMessage() + "\"}";
        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(responseBody.getBytes())));
    }

    private Mono<Void> handleInvalidTokenException(ServerWebExchange exchange, Throwable ex) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String responseBody = "{\"error\": \"Invalid token\", \"message\": \"" + ex.getMessage() + "\"}";
        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(responseBody.getBytes())));
    }

    private Mono<Void> handleGenericException(ServerWebExchange exchange, Throwable ex) {
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String responseBody = "{\"error\": \"Internal Server Error\", \"message\": \"" + ex.getMessage() + "\"}";
        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(responseBody.getBytes())));
    }
}