package org.gerald.inventario.repository;

import org.gerald.inventario.domain.LinPedido;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LinPedido entity.
 */
public interface LinPedidoRepository extends JpaRepository<LinPedido,Long> {

}
