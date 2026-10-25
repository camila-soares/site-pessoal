package com.camila.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        long start = Instant.now().toEpochMilli();

        exchange = exchange.mutate()
                .request(r -> r.header("X-Request-Id", requestId))
                .build();

        log.info("[{}] --> {} {}", requestId, request.getMethod(), request.getURI().getPath());

        ServerWebExchange finalExchange = exchange;
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long elapsed = Instant.now().toEpochMilli() - start;
            int status = finalExchange.getResponse().getStatusCode() != null
                    ? finalExchange.getResponse().getStatusCode().value() : 0;
            log.info("[{}] <-- {} {} {}ms", requestId,
                    status, finalExchange.getRequest().getURI().getPath(), elapsed);
        }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
