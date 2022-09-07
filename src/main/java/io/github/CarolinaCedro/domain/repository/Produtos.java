package io.github.CarolinaCedro.domain.repository;

import io.github.CarolinaCedro.domain.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Produtos extends JpaRepository<Produto, Integer> {
}
