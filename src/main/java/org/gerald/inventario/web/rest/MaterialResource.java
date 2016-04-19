package org.gerald.inventario.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gerald.inventario.domain.Material;
import org.gerald.inventario.repository.MaterialRepository;
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
 * REST controller for managing Material.
 */
@RestController
@RequestMapping("/api")
public class MaterialResource {

    private final Logger log = LoggerFactory.getLogger(MaterialResource.class);
        
    @Inject
    private MaterialRepository materialRepository;
    
    /**
     * POST  /materials : Create a new material.
     *
     * @param material the material to create
     * @return the ResponseEntity with status 201 (Created) and with body the new material, or with status 400 (Bad Request) if the material has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/materials",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Material> createMaterial(@Valid @RequestBody Material material) throws URISyntaxException {
        log.debug("REST request to save Material : {}", material);
        if (material.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("material", "idexists", "A new material cannot already have an ID")).body(null);
        }
        Material result = materialRepository.save(material);
        return ResponseEntity.created(new URI("/api/materials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("material", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /materials : Updates an existing material.
     *
     * @param material the material to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated material,
     * or with status 400 (Bad Request) if the material is not valid,
     * or with status 500 (Internal Server Error) if the material couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/materials",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Material> updateMaterial(@Valid @RequestBody Material material) throws URISyntaxException {
        log.debug("REST request to update Material : {}", material);
        if (material.getId() == null) {
            return createMaterial(material);
        }
        Material result = materialRepository.save(material);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("material", material.getId().toString()))
            .body(result);
    }

    /**
     * GET  /materials : get all the materials.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of materials in body
     */
    @RequestMapping(value = "/materials",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Material> getAllMaterials() {
        log.debug("REST request to get all Materials");
        List<Material> materials = materialRepository.findAll();
        return materials;
    }

    /**
     * GET  /materials/:id : get the "id" material.
     *
     * @param id the id of the material to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the material, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/materials/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Material> getMaterial(@PathVariable Long id) {
        log.debug("REST request to get Material : {}", id);
        Material material = materialRepository.findOne(id);
        return Optional.ofNullable(material)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /materials/:id : delete the "id" material.
     *
     * @param id the id of the material to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/materials/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        log.debug("REST request to delete Material : {}", id);
        materialRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("material", id.toString())).build();
    }

}
