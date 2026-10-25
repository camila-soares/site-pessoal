package com.camila.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Instant;

@Slf4j
@RestController
public class FallbackController {

    @RequestMapping("/fallback/sales")
    public Mono<ProblemDetail> salesFallback() {
        log.warn("Circuit breaker aberto — sales-service indisponível");
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE);
        problem.setTitle("Serviço indisponível");
        problem.setDetail("O serviço de vendas está temporariamente indisponível. Tente novamente em instantes.");
        problem.setType(URI.create("https://api.camila.dev/errors/service-unavailable"));
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("service", "sales-service");
        return Mono.just(problem);
    }
}
