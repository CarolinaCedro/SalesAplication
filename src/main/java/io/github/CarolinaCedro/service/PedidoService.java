package io.github.CarolinaCedro.service;

import io.github.CarolinaCedro.domain.entities.Pedido;
import io.github.CarolinaCedro.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {
    Pedido salvar(PedidoDTO dto);

    Optional<Pedido> obterPedidoCompleto(Integer id);
}
