package com.camila.sales.dto;

import com.camila.sales.model.Cliente;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClienteResponse {
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private LocalDateTime criadoEm;

    public static ClienteResponse from(Cliente c) {
        ClienteResponse dto = new ClienteResponse();
        dto.id = c.getId();
        dto.nome = c.getNome();
        dto.email = c.getEmail();
        dto.cpf = c.getCpf();
        dto.telefone = c.getTelefone();
        dto.criadoEm = c.getCriadoEm();
        return dto;
    }
}
