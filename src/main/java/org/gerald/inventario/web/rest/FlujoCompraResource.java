package org.gerald.inventario.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gerald.inventario.domain.FlujoCompra;
import org.gerald.inventario.repository.FlujoCompraRepository;
import org.gerald.inventario.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FlujoCompra.
 */
@RestController
@RequestMapping("/api")
public class FlujoCompraResource {

    private final Logger log = LoggerFactory.getLogger(FlujoCompraResource.class);
        
    @Inject
    private FlujoCompraRepository flujoCompraRepository;
    
    /**
     * POST  /flujo-compras : Create a new flujoCompra.
     *
     * @param flujoCompra the flujoCompra to create
     * @return the ResponseEntity with status 201 (Created) and with body the new flujoCompra, or with status 400 (Bad Request) if the flujoCompra has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/flujo-compras",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FlujoCompra> createFlujoCompra(@RequestBody FlujoCompra flujoCompra) throws URISyntaxException {
        log.debug("REST request to save FlujoCompra : {}", flujoCompra);
        if (flujoCompra.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("flujoCompra", "idexists", "A new flujoCompra cannot already have an ID")).body(null);
        }
        FlujoCompra result = flujoCompraRepository.save(flujoCompra);
        return ResponseEntity.created(new URI("/api/flujo-compras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("flujoCompra", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /flujo-compras : Updates an existing flujoCompra.
     *
     * @param flujoCompra the flujoCompra to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated flujoCompra,
     * or with status 400 (Bad Request) if the flujoCompra is not valid,
     * or with status 500 (Internal Server Error) if the flujoCompra couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/flujo-compras",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FlujoCompra> updateFlujoCompra(@RequestBody FlujoCompra flujoCompra) throws URISyntaxException {
        log.debug("REST request to update FlujoCompra : {}", flujoCompra);
        if (flujoCompra.getId() == null) {
            return createFlujoCompra(flujoCompra);
        }
        FlujoCompra result = flujoCompraRepository.save(flujoCompra);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("flujoCompra", flujoCompra.getId().toString()))
            .body(result);
    }

    /**
     * GET  /flujo-compras : get all the flujoCompras.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of flujoCompras in body
     */
    @RequestMapping(value = "/flujo-compras",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FlujoCompra> getAllFlujoCompras() {
        log.debug("REST request to get all FlujoCompras");
        List<FlujoCompra> flujoCompras = flujoCompraRepository.findAll();
        return flujoCompras;
    }

    /**
     * GET  /flujo-compras/:id : get the "id" flujoCompra.
     *
     * @param id the id of the flujoCompra to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the flujoCompra, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/flujo-compras/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FlujoCompra> getFlujoCompra(@PathVariable Long id) {
        log.debug("REST request to get FlujoCompra : {}", id);
        FlujoCompra flujoCompra = flujoCompraRepository.findOne(id);
        return Optional.ofNullable(flujoCompra)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /flujo-compras/:id : delete the "id" flujoCompra.
     *
     * @param id the id of the flujoCompra to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/flujo-compras/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFlujoCompra(@PathVariable Long id) {
        log.debug("REST request to delete FlujoCompra : {}", id);
        flujoCompraRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("flujoCompra", id.toString())).build();
    }

}
