package org.yc.gnosdrasil.gdapigateway.security;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;


import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtUtils jwtValidator;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        log.info("Authenticating JWT token");
        return Mono.justOrEmpty(authentication).filter(auth -> auth instanceof BearerTokenAuthenticationToken)
                .cast(BearerTokenAuthenticationToken.class).flatMap(token -> {
                    Claims claims = jwtValidator.validateJwtToken(token.getToken());
//                    List<SimpleGrantedAuthority> authorities = ((List<?>) claims.get("roles")).stream()
//                            .map(role -> new SimpleGrantedAuthority(role.toString())).collect(Collectors.toList());
                    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
                    return Mono.just(new PreAuthenticatedAuthenticationToken(claims.getSubject(), null, authorities));
                });
    }
}