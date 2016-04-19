package org.gerald.inventario.repository;

import org.gerald.inventario.domain.Material;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Material entity.
 */
public interface MaterialRepository extends JpaRepository<Material,Long> {

}
