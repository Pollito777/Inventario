package org.gerald.inventario.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gerald.inventario.domain.Proyecto;
import org.gerald.inventario.repository.ProyectoRepository;
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
 * REST controller for managing Proyecto.
 */
@RestController
@RequestMapping("/api")
public class ProyectoResource {

    private final Logger log = LoggerFactory.getLogger(ProyectoResource.class);
        
    @Inject
    private ProyectoRepository proyectoRepository;
    
    /**
     * POST  /proyectos : Create a new proyecto.
     *
     * @param proyecto the proyecto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new proyecto, or with status 400 (Bad Request) if the proyecto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/proyectos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Proyecto> createProyecto(@RequestBody Proyecto proyecto) throws URISyntaxException {
        log.debug("REST request to save Proyecto : {}", proyecto);
        if (proyecto.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("proyecto", "idexists", "A new proyecto cannot already have an ID")).body(null);
        }
        Proyecto result = proyectoRepository.save(proyecto);
        return ResponseEntity.created(new URI("/api/proyectos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("proyecto", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /proyectos : Updates an existing proyecto.
     *
     * @param proyecto the proyecto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated proyecto,
     * or with status 400 (Bad Request) if the proyecto is not valid,
     * or with status 500 (Internal Server Error) if the proyecto couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/proyectos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Proyecto> updateProyecto(@RequestBody Proyecto proyecto) throws URISyntaxException {
        log.debug("REST request to update Proyecto : {}", proyecto);
        if (proyecto.getId() == null) {
            return createProyecto(proyecto);
        }
        Proyecto result = proyectoRepository.save(proyecto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("proyecto", proyecto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /proyectos : get all the proyectos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of proyectos in body
     */
    @RequestMapping(value = "/proyectos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Proyecto> getAllProyectos() {
        log.debug("REST request to get all Proyectos");
        List<Proyecto> proyectos = proyectoRepository.findAll();
        return proyectos;
    }

    /**
     * GET  /proyectos/:id : get the "id" proyecto.
     *
     * @param id the id of the proyecto to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the proyecto, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/proyectos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Proyecto> getProyecto(@PathVariable Long id) {
        log.debug("REST request to get Proyecto : {}", id);
        Proyecto proyecto = proyectoRepository.findOne(id);
        return Optional.ofNullable(proyecto)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /proyectos/:id : delete the "id" proyecto.
     *
     * @param id the id of the proyecto to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/proyectos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProyecto(@PathVariable Long id) {
        log.debug("REST request to delete Proyecto : {}", id);
        proyectoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("proyecto", id.toString())).build();
    }

}
