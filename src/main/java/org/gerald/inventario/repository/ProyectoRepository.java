package org.gerald.inventario.repository;

import org.gerald.inventario.domain.Proyecto;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Proyecto entity.
 */
public interface ProyectoRepository extends JpaRepository<Proyecto,Long> {

}
