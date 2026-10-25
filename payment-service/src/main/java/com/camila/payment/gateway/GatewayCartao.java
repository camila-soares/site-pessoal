package com.camila.payment.gateway;

import com.camila.payment.model.FormaPagamento;
import com.camila.payment.model.Pagamento;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Cartão: aprova se valor ≤ R$ 9.999,99; recusa acima (simula limite de crédito).
 * Suporta CARTAO_CREDITO e CARTAO_DEBITO.
 */
@Slf4j
@Component
public class GatewayCartao implements GatewayPagamento {

    private static final BigDecimal LIMITE_APROVACAO = new BigDecimal("9999.99");

    @Override
    public FormaPagamento formaSuportada() {
        return FormaPagamento.CARTAO_CREDITO;
    }

    @Override
    public RespostaGateway processar(Pagamento pagamento) {
        String gatewayId = "CARD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        if (pagamento.getValor().compareTo(LIMITE_APROVACAO) > 0) {
            log.warn("Cartão recusado — pedido={} valor={} acima do limite", pagamento.getPedidoId(), pagamento.getValor());
            return RespostaGateway.recusado(gatewayId, "Valor acima do limite disponível");
        }

        String autorizacao = "AUTH-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
        log.info("Cartão aprovado — pedido={} gatewayId={}", pagamento.getPedidoId(), gatewayId);
        return RespostaGateway.aprovado(gatewayId, autorizacao);
    }
}
