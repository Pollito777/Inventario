package org.gerald.inventario.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gerald.inventario.domain.Formulario;
import org.gerald.inventario.repository.FormularioRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Formulario.
 */
@RestController
@RequestMapping("/api")
public class FormularioResource {

    private final Logger log = LoggerFactory.getLogger(FormularioResource.class);
        
    @Inject
    private FormularioRepository formularioRepository;
    
    /**
     * POST  /formularios : Create a new formulario.
     *
     * @param formulario the formulario to create
     * @return the ResponseEntity with status 201 (Created) and with body the new formulario, or with status 400 (Bad Request) if the formulario has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/formularios",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Formulario> createFormulario(@RequestBody Formulario formulario) throws URISyntaxException {
        log.debug("REST request to save Formulario : {}", formulario);
        if (formulario.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("formulario", "idexists", "A new formulario cannot already have an ID")).body(null);
        }
        Formulario result = formularioRepository.save(formulario);
        return ResponseEntity.created(new URI("/api/formularios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("formulario", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /formularios : Updates an existing formulario.
     *
     * @param formulario the formulario to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated formulario,
     * or with status 400 (Bad Request) if the formulario is not valid,
     * or with status 500 (Internal Server Error) if the formulario couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/formularios",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Formulario> updateFormulario(@RequestBody Formulario formulario) throws URISyntaxException {
        log.debug("REST request to update Formulario : {}", formulario);
        if (formulario.getId() == null) {
            return createFormulario(formulario);
        }
        Formulario result = formularioRepository.save(formulario);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("formulario", formulario.getId().toString()))
            .body(result);
    }

    /**
     * GET  /formularios : get all the formularios.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of formularios in body
     */
    @RequestMapping(value = "/formularios",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Formulario> getAllFormularios(@RequestParam(required = false) String filter) {
        if ("permisos-is-null".equals(filter)) {
            log.debug("REST request to get all Formularios where permisos is null");
            return StreamSupport
                .stream(formularioRepository.findAll().spliterator(), false)
                .filter(formulario -> formulario.getPermisos() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Formularios");
        List<Formulario> formularios = formularioRepository.findAll();
        return formularios;
    }

    /**
     * GET  /formularios/:id : get the "id" formulario.
     *
     * @param id the id of the formulario to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the formulario, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/formularios/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Formulario> getFormulario(@PathVariable Long id) {
        log.debug("REST request to get Formulario : {}", id);
        Formulario formulario = formularioRepository.findOne(id);
        return Optional.ofNullable(formulario)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /formularios/:id : delete the "id" formulario.
     *
     * @param id the id of the formulario to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/formularios/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFormulario(@PathVariable Long id) {
        log.debug("REST request to delete Formulario : {}", id);
        formularioRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("formulario", id.toString())).build();
    }

}
