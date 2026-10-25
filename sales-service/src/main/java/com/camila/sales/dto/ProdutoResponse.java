package com.camila.sales.dto;

import com.camila.sales.model.Produto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProdutoResponse {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer estoque;
    private Boolean ativo;
    private LocalDateTime criadoEm;

    public static ProdutoResponse from(Produto p) {
        ProdutoResponse dto = new ProdutoResponse();
        dto.id = p.getId();
        dto.nome = p.getNome();
        dto.descricao = p.getDescricao();
        dto.preco = p.getPreco();
        dto.estoque = p.getEstoque();
        dto.ativo = p.getAtivo();
        dto.criadoEm = p.getCriadoEm();
        return dto;
    }
}
