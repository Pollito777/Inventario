package org.gerald.inventario.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gerald.inventario.domain.LinPedido;
import org.gerald.inventario.repository.LinPedidoRepository;
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
 * REST controller for managing LinPedido.
 */
@RestController
@RequestMapping("/api")
public class LinPedidoResource {

    private final Logger log = LoggerFactory.getLogger(LinPedidoResource.class);
        
    @Inject
    private LinPedidoRepository linPedidoRepository;
    
    /**
     * POST  /lin-pedidos : Create a new linPedido.
     *
     * @param linPedido the linPedido to create
     * @return the ResponseEntity with status 201 (Created) and with body the new linPedido, or with status 400 (Bad Request) if the linPedido has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lin-pedidos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LinPedido> createLinPedido(@RequestBody LinPedido linPedido) throws URISyntaxException {
        log.debug("REST request to save LinPedido : {}", linPedido);
        if (linPedido.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("linPedido", "idexists", "A new linPedido cannot already have an ID")).body(null);
        }
        LinPedido result = linPedidoRepository.save(linPedido);
        return ResponseEntity.created(new URI("/api/lin-pedidos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("linPedido", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lin-pedidos : Updates an existing linPedido.
     *
     * @param linPedido the linPedido to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated linPedido,
     * or with status 400 (Bad Request) if the linPedido is not valid,
     * or with status 500 (Internal Server Error) if the linPedido couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lin-pedidos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LinPedido> updateLinPedido(@RequestBody LinPedido linPedido) throws URISyntaxException {
        log.debug("REST request to update LinPedido : {}", linPedido);
        if (linPedido.getId() == null) {
            return createLinPedido(linPedido);
        }
        LinPedido result = linPedidoRepository.save(linPedido);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("linPedido", linPedido.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lin-pedidos : get all the linPedidos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of linPedidos in body
     */
    @RequestMapping(value = "/lin-pedidos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LinPedido> getAllLinPedidos() {
        log.debug("REST request to get all LinPedidos");
        List<LinPedido> linPedidos = linPedidoRepository.findAll();
        return linPedidos;
    }

    /**
     * GET  /lin-pedidos/:id : get the "id" linPedido.
     *
     * @param id the id of the linPedido to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the linPedido, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lin-pedidos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LinPedido> getLinPedido(@PathVariable Long id) {
        log.debug("REST request to get LinPedido : {}", id);
        LinPedido linPedido = linPedidoRepository.findOne(id);
        return Optional.ofNullable(linPedido)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lin-pedidos/:id : delete the "id" linPedido.
     *
     * @param id the id of the linPedido to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lin-pedidos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLinPedido(@PathVariable Long id) {
        log.debug("REST request to delete LinPedido : {}", id);
        linPedidoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("linPedido", id.toString())).build();
    }

}
