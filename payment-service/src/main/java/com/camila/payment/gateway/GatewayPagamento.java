package com.camila.payment.gateway;

import com.camila.payment.model.FormaPagamento;
import com.camila.payment.model.Pagamento;

public interface GatewayPagamento {

    FormaPagamento formaSuportada();

    RespostaGateway processar(Pagamento pagamento);
}
