package org.gerald.inventario.repository;

import org.gerald.inventario.domain.LinSolicitud;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LinSolicitud entity.
 */
public interface LinSolicitudRepository extends JpaRepository<LinSolicitud,Long> {

}
