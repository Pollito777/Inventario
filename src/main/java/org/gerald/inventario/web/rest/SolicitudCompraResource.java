package org.gerald.inventario.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gerald.inventario.domain.SolicitudCompra;
import org.gerald.inventario.repository.SolicitudCompraRepository;
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
 * REST controller for managing SolicitudCompra.
 */
@RestController
@RequestMapping("/api")
public class SolicitudCompraResource {

    private final Logger log = LoggerFactory.getLogger(SolicitudCompraResource.class);
        
    @Inject
    private SolicitudCompraRepository solicitudCompraRepository;
    
    /**
     * POST  /solicitud-compras : Create a new solicitudCompra.
     *
     * @param solicitudCompra the solicitudCompra to create
     * @return the ResponseEntity with status 201 (Created) and with body the new solicitudCompra, or with status 400 (Bad Request) if the solicitudCompra has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/solicitud-compras",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SolicitudCompra> createSolicitudCompra(@Valid @RequestBody SolicitudCompra solicitudCompra) throws URISyntaxException {
        log.debug("REST request to save SolicitudCompra : {}", solicitudCompra);
        if (solicitudCompra.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("solicitudCompra", "idexists", "A new solicitudCompra cannot already have an ID")).body(null);
        }
        SolicitudCompra result = solicitudCompraRepository.save(solicitudCompra);
        return ResponseEntity.created(new URI("/api/solicitud-compras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("solicitudCompra", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /solicitud-compras : Updates an existing solicitudCompra.
     *
     * @param solicitudCompra the solicitudCompra to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated solicitudCompra,
     * or with status 400 (Bad Request) if the solicitudCompra is not valid,
     * or with status 500 (Internal Server Error) if the solicitudCompra couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/solicitud-compras",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SolicitudCompra> updateSolicitudCompra(@Valid @RequestBody SolicitudCompra solicitudCompra) throws URISyntaxException {
        log.debug("REST request to update SolicitudCompra : {}", solicitudCompra);
        if (solicitudCompra.getId() == null) {
            return createSolicitudCompra(solicitudCompra);
        }
        SolicitudCompra result = solicitudCompraRepository.save(solicitudCompra);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("solicitudCompra", solicitudCompra.getId().toString()))
            .body(result);
    }

    /**
     * GET  /solicitud-compras : get all the solicitudCompras.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of solicitudCompras in body
     */
    @RequestMapping(value = "/solicitud-compras",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SolicitudCompra> getAllSolicitudCompras() {
        log.debug("REST request to get all SolicitudCompras");
        List<SolicitudCompra> solicitudCompras = solicitudCompraRepository.findAll();
        return solicitudCompras;
    }

    /**
     * GET  /solicitud-compras/:id : get the "id" solicitudCompra.
     *
     * @param id the id of the solicitudCompra to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the solicitudCompra, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/solicitud-compras/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SolicitudCompra> getSolicitudCompra(@PathVariable Long id) {
        log.debug("REST request to get SolicitudCompra : {}", id);
        SolicitudCompra solicitudCompra = solicitudCompraRepository.findOne(id);
        return Optional.ofNullable(solicitudCompra)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /solicitud-compras/:id : delete the "id" solicitudCompra.
     *
     * @param id the id of the solicitudCompra to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/solicitud-compras/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSolicitudCompra(@PathVariable Long id) {
        log.debug("REST request to delete SolicitudCompra : {}", id);
        solicitudCompraRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("solicitudCompra", id.toString())).build();
    }

}
