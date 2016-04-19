package org.gerald.inventario.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gerald.inventario.domain.LinSolicitud;
import org.gerald.inventario.repository.LinSolicitudRepository;
import org.gerald.inventario.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing LinSolicitud.
 */
@RestController
@RequestMapping("/api")
public class LinSolicitudResource {

    private final Logger log = LoggerFactory.getLogger(LinSolicitudResource.class);
        
    @Inject
    private LinSolicitudRepository linSolicitudRepository;
    
    /**
     * POST  /lin-solicituds : Create a new linSolicitud.
     *
     * @param linSolicitud the linSolicitud to create
     * @return the ResponseEntity with status 201 (Created) and with body the new linSolicitud, or with status 400 (Bad Request) if the linSolicitud has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lin-solicituds",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LinSolicitud> createLinSolicitud(@Valid @RequestBody LinSolicitud linSolicitud) throws URISyntaxException {
        log.debug("REST request to save LinSolicitud : {}", linSolicitud);
        if (linSolicitud.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("linSolicitud", "idexists", "A new linSolicitud cannot already have an ID")).body(null);
        }
        LinSolicitud result = linSolicitudRepository.save(linSolicitud);
        return ResponseEntity.created(new URI("/api/lin-solicituds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("linSolicitud", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lin-solicituds : Updates an existing linSolicitud.
     *
     * @param linSolicitud the linSolicitud to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated linSolicitud,
     * or with status 400 (Bad Request) if the linSolicitud is not valid,
     * or with status 500 (Internal Server Error) if the linSolicitud couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lin-solicituds",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LinSolicitud> updateLinSolicitud(@Valid @RequestBody LinSolicitud linSolicitud) throws URISyntaxException {
        log.debug("REST request to update LinSolicitud : {}", linSolicitud);
        if (linSolicitud.getId() == null) {
            return createLinSolicitud(linSolicitud);
        }
        LinSolicitud result = linSolicitudRepository.save(linSolicitud);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("linSolicitud", linSolicitud.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lin-solicituds : get all the linSolicituds.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of linSolicituds in body
     */
    @RequestMapping(value = "/lin-solicituds",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LinSolicitud> getAllLinSolicituds() {
        log.debug("REST request to get all LinSolicituds");
        List<LinSolicitud> linSolicituds = linSolicitudRepository.findAll();
        return linSolicituds;
    }

    /**
     * GET  /lin-solicituds/:id : get the "id" linSolicitud.
     *
     * @param id the id of the linSolicitud to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the linSolicitud, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lin-solicituds/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LinSolicitud> getLinSolicitud(@PathVariable Long id) {
        log.debug("REST request to get LinSolicitud : {}", id);
        LinSolicitud linSolicitud = linSolicitudRepository.findOne(id);
        return Optional.ofNullable(linSolicitud)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lin-solicituds/:id : delete the "id" linSolicitud.
     *
     * @param id the id of the linSolicitud to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lin-solicituds/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLinSolicitud(@PathVariable Long id) {
        log.debug("REST request to delete LinSolicitud : {}", id);
        linSolicitudRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("linSolicitud", id.toString())).build();
    }

}
