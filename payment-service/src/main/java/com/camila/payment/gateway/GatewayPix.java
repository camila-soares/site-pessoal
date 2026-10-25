package com.camila.payment.gateway;

import com.camila.payment.model.FormaPagamento;
import com.camila.payment.model.Pagamento;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/** PIX: aprovação instantânea (simulado). */
@Slf4j
@Component
public class GatewayPix implements GatewayPagamento {

    @Override
    public FormaPagamento formaSuportada() {
        return FormaPagamento.PIX;
    }

    @Override
    public RespostaGateway processar(Pagamento pagamento) {
        String txid = "PIX-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
        String autorizacao = "AUTH-PIX-" + System.currentTimeMillis();
        log.info("PIX processado — pedido={} txid={} valor={}", pagamento.getPedidoId(), txid, pagamento.getValor());
        return RespostaGateway.aprovado(txid, autorizacao);
    }
}
