package com.camila.sales.config;

import com.camila.sales.model.Cliente;
import com.camila.sales.model.Produto;
import com.camila.sales.repository.ClienteRepository;
import com.camila.sales.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    @Override
    public void run(String... args) {
        if (clienteRepository.count() > 0) return;

        log.info("Carregando dados iniciais...");

        clienteRepository.saveAll(List.of(
                Cliente.builder().nome("Ana Silva").email("ana@exemplo.com").cpf("111.111.111-11").telefone("(11) 99999-0001").build(),
                Cliente.builder().nome("Bruno Santos").email("bruno@exemplo.com").cpf("222.222.222-22").telefone("(11) 99999-0002").build(),
                Cliente.builder().nome("Carla Mendes").email("carla@exemplo.com").cpf("333.333.333-33").telefone("(11) 99999-0003").build()
        ));

        produtoRepository.saveAll(List.of(
                Produto.builder().nome("Notebook Pro 15").descricao("Notebook alta performance").preco(new BigDecimal("4999.90")).estoque(50).build(),
                Produto.builder().nome("Mouse Sem Fio").descricao("Mouse ergonômico bluetooth").preco(new BigDecimal("149.90")).estoque(200).build(),
                Produto.builder().nome("Teclado Mecânico").descricao("Teclado mecânico RGB").preco(new BigDecimal("399.90")).estoque(120).build(),
                Produto.builder().nome("Monitor 27\"").descricao("Monitor Full HD IPS").preco(new BigDecimal("1299.90")).estoque(80).build(),
                Produto.builder().nome("Headset Gamer").descricao("Headset surround 7.1").preco(new BigDecimal("299.90")).estoque(150).build()
        ));

        log.info("Dados iniciais carregados com sucesso.");
    }
}
