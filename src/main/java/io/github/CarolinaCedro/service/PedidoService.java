package io.github.CarolinaCedro.service;

import io.github.CarolinaCedro.domain.entities.Pedido;
import io.github.CarolinaCedro.domain.enums.StatusPedido;
import io.github.CarolinaCedro.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {
    Pedido salvar(PedidoDTO dto);
    Optional<Pedido> obterPedidoCompleto(Integer id);
    void atualizarStatus(Integer id, StatusPedido statusPedido);
}
