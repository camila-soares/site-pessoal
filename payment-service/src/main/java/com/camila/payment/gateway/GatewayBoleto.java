package com.camila.payment.gateway;

import com.camila.payment.model.FormaPagamento;
import com.camila.payment.model.Pagamento;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

/** Boleto: sempre retorna PENDENTE; confirmação chega via webhook. */
@Slf4j
@Component
public class GatewayBoleto implements GatewayPagamento {

    @Override
    public FormaPagamento formaSuportada() {
        return FormaPagamento.BOLETO;
    }

    @Override
    public RespostaGateway processar(Pagamento pagamento) {
        String nossoNumero = "BOL-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
        log.info("Boleto gerado — pedido={} nossoNumero={}", pagamento.getPedidoId(), nossoNumero);
        return RespostaGateway.pendente(nossoNumero);
    }
}
