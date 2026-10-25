package com.camila.sales.repository;

import com.camila.sales.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByAtivoTrue();

    @Query("SELECT p FROM Produto p WHERE p.ativo = true AND p.estoque > 0")
    List<Produto> findDisponiveis();

    List<Produto> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);
}
