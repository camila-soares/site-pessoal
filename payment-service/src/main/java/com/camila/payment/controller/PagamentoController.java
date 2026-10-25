package com.camila.payment.controller;

import com.camila.payment.dto.PagamentoRequest;
import com.camila.payment.dto.PagamentoResponse;
import com.camila.payment.dto.WebhookRequest;
import com.camila.payment.service.PagamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<PagamentoResponse>> listarPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(pagamentoService.listarPorPedido(pedidoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(pagamentoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<PagamentoResponse> processar(@Valid @RequestBody PagamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoService.processar(request));
    }

    /** Endpoint chamado pelo gateway externo para confirmar boleto/PIX assíncrono. */
    @PostMapping("/webhook")
    public ResponseEntity<PagamentoResponse> webhook(@Valid @RequestBody WebhookRequest webhook) {
        return ResponseEntity.ok(pagamentoService.processarWebhook(webhook));
    }

    @PostMapping("/{id}/estornar")
    public ResponseEntity<PagamentoResponse> estornar(@PathVariable Long id) {
        return ResponseEntity.ok(pagamentoService.estornar(id));
    }
}
