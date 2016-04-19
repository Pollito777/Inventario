package org.gerald.inventario.repository;

import org.gerald.inventario.domain.Pedido;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Pedido entity.
 */
public interface PedidoRepository extends JpaRepository<Pedido,Long> {

}
