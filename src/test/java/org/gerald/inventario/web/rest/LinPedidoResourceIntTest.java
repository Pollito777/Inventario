package org.gerald.inventario.web.rest;

import org.gerald.inventario.InventarioApp;
import org.gerald.inventario.domain.LinPedido;
import org.gerald.inventario.repository.LinPedidoRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the LinPedidoResource REST controller.
 *
 * @see LinPedidoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InventarioApp.class)
@WebAppConfiguration
@IntegrationTest
public class LinPedidoResourceIntTest {


    private static final Integer DEFAULT_CANTIDAD_PEDIDO = 1;
    private static final Integer UPDATED_CANTIDAD_PEDIDO = 2;

    private static final Integer DEFAULT_CANTIDAD_AUTORIZADA = 1;
    private static final Integer UPDATED_CANTIDAD_AUTORIZADA = 2;

    private static final Integer DEFAULT_CANTIDAD_DESPACHADA = 1;
    private static final Integer UPDATED_CANTIDAD_DESPACHADA = 2;

    @Inject
    private LinPedidoRepository linPedidoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLinPedidoMockMvc;

    private LinPedido linPedido;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LinPedidoResource linPedidoResource = new LinPedidoResource();
        ReflectionTestUtils.setField(linPedidoResource, "linPedidoRepository", linPedidoRepository);
        this.restLinPedidoMockMvc = MockMvcBuilders.standaloneSetup(linPedidoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        linPedido = new LinPedido();
        linPedido.setCantidadPedido(DEFAULT_CANTIDAD_PEDIDO);
        linPedido.setCantidadAutorizada(DEFAULT_CANTIDAD_AUTORIZADA);
        linPedido.setCantidadDespachada(DEFAULT_CANTIDAD_DESPACHADA);
    }

    @Test
    @Transactional
    public void createLinPedido() throws Exception {
        int databaseSizeBeforeCreate = linPedidoRepository.findAll().size();

        // Create the LinPedido

        restLinPedidoMockMvc.perform(post("/api/lin-pedidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(linPedido)))
                .andExpect(status().isCreated());

        // Validate the LinPedido in the database
        List<LinPedido> linPedidos = linPedidoRepository.findAll();
        assertThat(linPedidos).hasSize(databaseSizeBeforeCreate + 1);
        LinPedido testLinPedido = linPedidos.get(linPedidos.size() - 1);
        assertThat(testLinPedido.getCantidadPedido()).isEqualTo(DEFAULT_CANTIDAD_PEDIDO);
        assertThat(testLinPedido.getCantidadAutorizada()).isEqualTo(DEFAULT_CANTIDAD_AUTORIZADA);
        assertThat(testLinPedido.getCantidadDespachada()).isEqualTo(DEFAULT_CANTIDAD_DESPACHADA);
    }

    @Test
    @Transactional
    public void getAllLinPedidos() throws Exception {
        // Initialize the database
        linPedidoRepository.saveAndFlush(linPedido);

        // Get all the linPedidos
        restLinPedidoMockMvc.perform(get("/api/lin-pedidos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(linPedido.getId().intValue())))
                .andExpect(jsonPath("$.[*].cantidadPedido").value(hasItem(DEFAULT_CANTIDAD_PEDIDO)))
                .andExpect(jsonPath("$.[*].cantidadAutorizada").value(hasItem(DEFAULT_CANTIDAD_AUTORIZADA)))
                .andExpect(jsonPath("$.[*].cantidadDespachada").value(hasItem(DEFAULT_CANTIDAD_DESPACHADA)));
    }

    @Test
    @Transactional
    public void getLinPedido() throws Exception {
        // Initialize the database
        linPedidoRepository.saveAndFlush(linPedido);

        // Get the linPedido
        restLinPedidoMockMvc.perform(get("/api/lin-pedidos/{id}", linPedido.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(linPedido.getId().intValue()))
            .andExpect(jsonPath("$.cantidadPedido").value(DEFAULT_CANTIDAD_PEDIDO))
            .andExpect(jsonPath("$.cantidadAutorizada").value(DEFAULT_CANTIDAD_AUTORIZADA))
            .andExpect(jsonPath("$.cantidadDespachada").value(DEFAULT_CANTIDAD_DESPACHADA));
    }

    @Test
    @Transactional
    public void getNonExistingLinPedido() throws Exception {
        // Get the linPedido
        restLinPedidoMockMvc.perform(get("/api/lin-pedidos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLinPedido() throws Exception {
        // Initialize the database
        linPedidoRepository.saveAndFlush(linPedido);
        int databaseSizeBeforeUpdate = linPedidoRepository.findAll().size();

        // Update the linPedido
        LinPedido updatedLinPedido = new LinPedido();
        updatedLinPedido.setId(linPedido.getId());
        updatedLinPedido.setCantidadPedido(UPDATED_CANTIDAD_PEDIDO);
        updatedLinPedido.setCantidadAutorizada(UPDATED_CANTIDAD_AUTORIZADA);
        updatedLinPedido.setCantidadDespachada(UPDATED_CANTIDAD_DESPACHADA);

        restLinPedidoMockMvc.perform(put("/api/lin-pedidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLinPedido)))
                .andExpect(status().isOk());

        // Validate the LinPedido in the database
        List<LinPedido> linPedidos = linPedidoRepository.findAll();
        assertThat(linPedidos).hasSize(databaseSizeBeforeUpdate);
        LinPedido testLinPedido = linPedidos.get(linPedidos.size() - 1);
        assertThat(testLinPedido.getCantidadPedido()).isEqualTo(UPDATED_CANTIDAD_PEDIDO);
        assertThat(testLinPedido.getCantidadAutorizada()).isEqualTo(UPDATED_CANTIDAD_AUTORIZADA);
        assertThat(testLinPedido.getCantidadDespachada()).isEqualTo(UPDATED_CANTIDAD_DESPACHADA);
    }

    @Test
    @Transactional
    public void deleteLinPedido() throws Exception {
        // Initialize the database
        linPedidoRepository.saveAndFlush(linPedido);
        int databaseSizeBeforeDelete = linPedidoRepository.findAll().size();

        // Get the linPedido
        restLinPedidoMockMvc.perform(delete("/api/lin-pedidos/{id}", linPedido.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<LinPedido> linPedidos = linPedidoRepository.findAll();
        assertThat(linPedidos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
