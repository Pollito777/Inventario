package org.gerald.inventario.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gerald.inventario.domain.Rol;
import org.gerald.inventario.repository.RolRepository;
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
 * REST controller for managing Rol.
 */
@RestController
@RequestMapping("/api")
public class RolResource {

    private final Logger log = LoggerFactory.getLogger(RolResource.class);
        
    @Inject
    private RolRepository rolRepository;
    
    /**
     * POST  /rols : Create a new rol.
     *
     * @param rol the rol to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rol, or with status 400 (Bad Request) if the rol has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/rols",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Rol> createRol(@RequestBody Rol rol) throws URISyntaxException {
        log.debug("REST request to save Rol : {}", rol);
        if (rol.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("rol", "idexists", "A new rol cannot already have an ID")).body(null);
        }
        Rol result = rolRepository.save(rol);
        return ResponseEntity.created(new URI("/api/rols/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("rol", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rols : Updates an existing rol.
     *
     * @param rol the rol to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rol,
     * or with status 400 (Bad Request) if the rol is not valid,
     * or with status 500 (Internal Server Error) if the rol couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/rols",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Rol> updateRol(@RequestBody Rol rol) throws URISyntaxException {
        log.debug("REST request to update Rol : {}", rol);
        if (rol.getId() == null) {
            return createRol(rol);
        }
        Rol result = rolRepository.save(rol);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("rol", rol.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rols : get all the rols.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rols in body
     */
    @RequestMapping(value = "/rols",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Rol> getAllRols() {
        log.debug("REST request to get all Rols");
        List<Rol> rols = rolRepository.findAll();
        return rols;
    }

    /**
     * GET  /rols/:id : get the "id" rol.
     *
     * @param id the id of the rol to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rol, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/rols/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Rol> getRol(@PathVariable Long id) {
        log.debug("REST request to get Rol : {}", id);
        Rol rol = rolRepository.findOne(id);
        return Optional.ofNullable(rol)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /rols/:id : delete the "id" rol.
     *
     * @param id the id of the rol to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/rols/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRol(@PathVariable Long id) {
        log.debug("REST request to delete Rol : {}", id);
        rolRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("rol", id.toString())).build();
    }

}
