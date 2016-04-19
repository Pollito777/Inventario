package org.gerald.inventario.repository;

import org.gerald.inventario.domain.Documento;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Documento entity.
 */
public interface DocumentoRepository extends JpaRepository<Documento,Long> {

}
