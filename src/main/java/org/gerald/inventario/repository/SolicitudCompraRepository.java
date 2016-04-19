package org.gerald.inventario.repository;

import org.gerald.inventario.domain.SolicitudCompra;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SolicitudCompra entity.
 */
public interface SolicitudCompraRepository extends JpaRepository<SolicitudCompra,Long> {

}
