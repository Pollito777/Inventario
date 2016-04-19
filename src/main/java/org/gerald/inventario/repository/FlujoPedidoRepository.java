package org.gerald.inventario.repository;

import org.gerald.inventario.domain.FlujoPedido;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FlujoPedido entity.
 */
public interface FlujoPedidoRepository extends JpaRepository<FlujoPedido,Long> {

}
