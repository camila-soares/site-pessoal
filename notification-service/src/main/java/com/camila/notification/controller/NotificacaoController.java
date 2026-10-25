package com.camila.notification.controller;

import com.camila.notification.dto.NotificacaoRequest;
import com.camila.notification.dto.NotificacaoResponse;
import com.camila.notification.service.NotificacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    @PostMapping
    public ResponseEntity<NotificacaoResponse> enviar(@Valid @RequestBody NotificacaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificacaoService.enviar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacaoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(notificacaoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<NotificacaoResponse>> listar(
            @RequestParam(required = false) String destinatario,
            @RequestParam(required = false) String referencia) {
        if (destinatario != null) return ResponseEntity.ok(notificacaoService.listarPorDestinatario(destinatario));
        if (referencia != null)   return ResponseEntity.ok(notificacaoService.listarPorReferencia(referencia));
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{id}/reenviar")
    public ResponseEntity<NotificacaoResponse> reenviar(@PathVariable Long id) {
        return ResponseEntity.ok(notificacaoService.reenviar(id));
    }
}
