package io.github.CarolinaCedro.service.impl;

import io.github.CarolinaCedro.domain.entities.Cliente;
import io.github.CarolinaCedro.domain.entities.ItemPedido;
import io.github.CarolinaCedro.domain.entities.Pedido;
import io.github.CarolinaCedro.domain.entities.Produto;
import io.github.CarolinaCedro.domain.enums.StatusPedido;
import io.github.CarolinaCedro.domain.repository.Clientes;
import io.github.CarolinaCedro.domain.repository.ItemPedidos;
import io.github.CarolinaCedro.domain.repository.Pedidos;
import io.github.CarolinaCedro.domain.repository.Produtos;
import io.github.CarolinaCedro.exception.PedidoNaoEncontradoException;
import io.github.CarolinaCedro.exception.RegraNegocioException;
import io.github.CarolinaCedro.rest.dto.ItemPedidoDTO;
import io.github.CarolinaCedro.rest.dto.PedidoDTO;
import io.github.CarolinaCedro.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final Pedidos repository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final ItemPedidos itemsPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar( PedidoDTO dto ) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientesRepository
                .findById(idCliente)
                .orElseThrow(() -> new RegraNegocioException("Código de cliente inválido."));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItems());
        repository.save(pedido);
        itemsPedidoRepository.saveAll(itemsPedido);
        pedido.setItens(itemsPedido);
        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return repository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizarStatus(Integer id, StatusPedido statusPedido) {
            repository
                    .findById(id)
                    .map(pedido -> {
                        pedido.setStatus(statusPedido);
                        return repository.save(pedido);
                    }).orElseThrow(()-> new PedidoNaoEncontradoException());
    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items){
        if(items.isEmpty()){
            throw new RegraNegocioException("Não é possível realizar um pedido sem items.");
        }

        return items
                .stream()
                .map( dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtosRepository
                            .findById(idProduto)
                            .orElseThrow(
                                    () -> new RegraNegocioException(
                                            "Código de produto inválido: "+ idProduto
                                    ));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;
                }).collect(Collectors.toList());

    }
}

