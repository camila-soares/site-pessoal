package com.camila.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacaoClient {

    private final WebClient notificationWebClient;

    public void enviarNotificacaoPagamento(Long pedidoId, String clienteEmail,
                                           String clienteNome, String status,
                                           String detalhes) {
        var payload = Map.of(
                "destinatario", clienteEmail,
                "nomeDestinatario", clienteNome,
                "tipo", "EMAIL",
                "assunto", "Atualização do seu pagamento — Pedido #" + pedidoId,
                "mensagem", buildMensagem(clienteNome, pedidoId, status, detalhes),
                "referencia", "PAGAMENTO-" + pedidoId
        );

        notificationWebClient.post()
                .uri("/api/v1/notificacoes")
                .bodyValue(payload)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(r -> log.info("Notificação enviada — pedido={} status={}", pedidoId, status))
                .doOnError(e -> log.error("Falha ao enviar notificação — pedido={}: {}", pedidoId, e.getMessage()))
                .subscribe();
    }

    private String buildMensagem(String nome, Long pedidoId, String status, String detalhes) {
        return "Olá, %s!\n\nSeu pagamento referente ao Pedido #%d foi %s.\n%s\n\nObrigado por comprar conosco!"
                .formatted(nome, pedidoId, status, detalhes != null ? detalhes : "");
    }
}
