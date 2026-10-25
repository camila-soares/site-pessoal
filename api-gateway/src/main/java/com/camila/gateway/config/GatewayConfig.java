package com.camila.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    /**
     * Limita por IP remoto. Em produção troque por extração de subject do JWT.
     */
    @Bean
    @Profile("!dev")
    public KeyResolver remoteAddrKeyResolver() {
        return exchange -> Mono.justOrEmpty(
                exchange.getRequest().getRemoteAddress()
        ).map(addr -> addr.getAddress().getHostAddress())
         .defaultIfEmpty("anonymous");
    }

    /** Placeholder para dev (sem Redis). */
    @Bean
    @Profile("dev")
    public KeyResolver devKeyResolver() {
        return exchange -> Mono.just("dev");
    }
}
