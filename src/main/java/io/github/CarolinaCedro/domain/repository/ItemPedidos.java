package io.github.CarolinaCedro.domain.repository;

import io.github.CarolinaCedro.domain.entities.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPedidos extends JpaRepository<ItemPedido, Integer> {
}
