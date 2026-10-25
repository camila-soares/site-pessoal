package com.camila.sales.repository;

import com.camila.sales.model.Pedido;
import com.camila.sales.model.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteId(Long clienteId);
    List<Pedido> findByStatus(StatusPedido status);

    @Query("SELECT p FROM Pedido p JOIN FETCH p.cliente JOIN FETCH p.itens WHERE p.id = :id")
    java.util.Optional<Pedido> findByIdComDetalhes(Long id);
}
