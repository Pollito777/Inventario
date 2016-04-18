package org.gerald.inventario.repository;

import org.gerald.inventario.domain.Rol;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Rol entity.
 */
public interface RolRepository extends JpaRepository<Rol,Long> {

}
