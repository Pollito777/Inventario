package org.gerald.inventario.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gerald.inventario.domain.EntidadPublica;
import org.gerald.inventario.repository.EntidadPublicaRepository;
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
 * REST controller for managing EntidadPublica.
 */
@RestController
@RequestMapping("/api")
public class EntidadPublicaResource {

    private final Logger log = LoggerFactory.getLogger(EntidadPublicaResource.class);
        
    @Inject
    private EntidadPublicaRepository entidadPublicaRepository;
    
    /**
     * POST  /entidad-publicas : Create a new entidadPublica.
     *
     * @param entidadPublica the entidadPublica to create
     * @return the ResponseEntity with status 201 (Created) and with body the new entidadPublica, or with status 400 (Bad Request) if the entidadPublica has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/entidad-publicas",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EntidadPublica> createEntidadPublica(@RequestBody EntidadPublica entidadPublica) throws URISyntaxException {
        log.debug("REST request to save EntidadPublica : {}", entidadPublica);
        if (entidadPublica.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("entidadPublica", "idexists", "A new entidadPublica cannot already have an ID")).body(null);
        }
        EntidadPublica result = entidadPublicaRepository.save(entidadPublica);
        return ResponseEntity.created(new URI("/api/entidad-publicas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("entidadPublica", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /entidad-publicas : Updates an existing entidadPublica.
     *
     * @param entidadPublica the entidadPublica to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated entidadPublica,
     * or with status 400 (Bad Request) if the entidadPublica is not valid,
     * or with status 500 (Internal Server Error) if the entidadPublica couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/entidad-publicas",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EntidadPublica> updateEntidadPublica(@RequestBody EntidadPublica entidadPublica) throws URISyntaxException {
        log.debug("REST request to update EntidadPublica : {}", entidadPublica);
        if (entidadPublica.getId() == null) {
            return createEntidadPublica(entidadPublica);
        }
        EntidadPublica result = entidadPublicaRepository.save(entidadPublica);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("entidadPublica", entidadPublica.getId().toString()))
            .body(result);
    }

    /**
     * GET  /entidad-publicas : get all the entidadPublicas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of entidadPublicas in body
     */
    @RequestMapping(value = "/entidad-publicas",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EntidadPublica> getAllEntidadPublicas() {
        log.debug("REST request to get all EntidadPublicas");
        List<EntidadPublica> entidadPublicas = entidadPublicaRepository.findAll();
        return entidadPublicas;
    }

    /**
     * GET  /entidad-publicas/:id : get the "id" entidadPublica.
     *
     * @param id the id of the entidadPublica to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the entidadPublica, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/entidad-publicas/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EntidadPublica> getEntidadPublica(@PathVariable Long id) {
        log.debug("REST request to get EntidadPublica : {}", id);
        EntidadPublica entidadPublica = entidadPublicaRepository.findOne(id);
        return Optional.ofNullable(entidadPublica)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /entidad-publicas/:id : delete the "id" entidadPublica.
     *
     * @param id the id of the entidadPublica to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/entidad-publicas/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEntidadPublica(@PathVariable Long id) {
        log.debug("REST request to delete EntidadPublica : {}", id);
        entidadPublicaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("entidadPublica", id.toString())).build();
    }

}
