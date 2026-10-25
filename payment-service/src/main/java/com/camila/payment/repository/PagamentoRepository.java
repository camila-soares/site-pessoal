package com.camila.payment.repository;

import com.camila.payment.model.Pagamento;
import com.camila.payment.model.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByPedidoId(Long pedidoId);
    Optional<Pagamento> findByGatewayId(String gatewayId);
    List<Pagamento> findByStatus(StatusPagamento status);
}
