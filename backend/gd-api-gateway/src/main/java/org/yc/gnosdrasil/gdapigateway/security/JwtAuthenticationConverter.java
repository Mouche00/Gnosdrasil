package org.yc.gnosdrasil.gdapigateway.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class JwtAuthenticationConverter implements ServerAuthenticationConverter {

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        // Prioritize Authorization header, fall back to cookie
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(header -> header.startsWith("Bearer ")).map(header -> header.substring(7))
                .switchIfEmpty(Mono.justOrEmpty(exchange.getRequest().getCookies().getFirst("Authorization"))
                        .map(cookie -> cookie.getValue()))
                .map(BearerTokenAuthenticationToken::new);
    }
}