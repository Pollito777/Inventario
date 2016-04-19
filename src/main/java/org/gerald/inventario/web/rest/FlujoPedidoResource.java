package org.gerald.inventario.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gerald.inventario.domain.FlujoPedido;
import org.gerald.inventario.repository.FlujoPedidoRepository;
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
 * REST controller for managing FlujoPedido.
 */
@RestController
@RequestMapping("/api")
public class FlujoPedidoResource {

    private final Logger log = LoggerFactory.getLogger(FlujoPedidoResource.class);
        
    @Inject
    private FlujoPedidoRepository flujoPedidoRepository;
    
    /**
     * POST  /flujo-pedidos : Create a new flujoPedido.
     *
     * @param flujoPedido the flujoPedido to create
     * @return the ResponseEntity with status 201 (Created) and with body the new flujoPedido, or with status 400 (Bad Request) if the flujoPedido has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/flujo-pedidos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FlujoPedido> createFlujoPedido(@RequestBody FlujoPedido flujoPedido) throws URISyntaxException {
        log.debug("REST request to save FlujoPedido : {}", flujoPedido);
        if (flujoPedido.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("flujoPedido", "idexists", "A new flujoPedido cannot already have an ID")).body(null);
        }
        FlujoPedido result = flujoPedidoRepository.save(flujoPedido);
        return ResponseEntity.created(new URI("/api/flujo-pedidos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("flujoPedido", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /flujo-pedidos : Updates an existing flujoPedido.
     *
     * @param flujoPedido the flujoPedido to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated flujoPedido,
     * or with status 400 (Bad Request) if the flujoPedido is not valid,
     * or with status 500 (Internal Server Error) if the flujoPedido couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/flujo-pedidos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FlujoPedido> updateFlujoPedido(@RequestBody FlujoPedido flujoPedido) throws URISyntaxException {
        log.debug("REST request to update FlujoPedido : {}", flujoPedido);
        if (flujoPedido.getId() == null) {
            return createFlujoPedido(flujoPedido);
        }
        FlujoPedido result = flujoPedidoRepository.save(flujoPedido);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("flujoPedido", flujoPedido.getId().toString()))
            .body(result);
    }

    /**
     * GET  /flujo-pedidos : get all the flujoPedidos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of flujoPedidos in body
     */
    @RequestMapping(value = "/flujo-pedidos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FlujoPedido> getAllFlujoPedidos() {
        log.debug("REST request to get all FlujoPedidos");
        List<FlujoPedido> flujoPedidos = flujoPedidoRepository.findAll();
        return flujoPedidos;
    }

    /**
     * GET  /flujo-pedidos/:id : get the "id" flujoPedido.
     *
     * @param id the id of the flujoPedido to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the flujoPedido, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/flujo-pedidos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FlujoPedido> getFlujoPedido(@PathVariable Long id) {
        log.debug("REST request to get FlujoPedido : {}", id);
        FlujoPedido flujoPedido = flujoPedidoRepository.findOne(id);
        return Optional.ofNullable(flujoPedido)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /flujo-pedidos/:id : delete the "id" flujoPedido.
     *
     * @param id the id of the flujoPedido to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/flujo-pedidos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFlujoPedido(@PathVariable Long id) {
        log.debug("REST request to delete FlujoPedido : {}", id);
        flujoPedidoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("flujoPedido", id.toString())).build();
    }

}
