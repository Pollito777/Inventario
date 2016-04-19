package org.gerald.inventario.web.rest;

import org.gerald.inventario.InventarioApp;
import org.gerald.inventario.domain.FlujoPedido;
import org.gerald.inventario.repository.FlujoPedidoRepository;

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
 * Test class for the FlujoPedidoResource REST controller.
 *
 * @see FlujoPedidoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InventarioApp.class)
@WebAppConfiguration
@IntegrationTest
public class FlujoPedidoResourceIntTest {

    private static final String DEFAULT_ESTADO = "AAAAA";
    private static final String UPDATED_ESTADO = "BBBBB";

    @Inject
    private FlujoPedidoRepository flujoPedidoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFlujoPedidoMockMvc;

    private FlujoPedido flujoPedido;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FlujoPedidoResource flujoPedidoResource = new FlujoPedidoResource();
        ReflectionTestUtils.setField(flujoPedidoResource, "flujoPedidoRepository", flujoPedidoRepository);
        this.restFlujoPedidoMockMvc = MockMvcBuilders.standaloneSetup(flujoPedidoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        flujoPedido = new FlujoPedido();
        flujoPedido.setEstado(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    public void createFlujoPedido() throws Exception {
        int databaseSizeBeforeCreate = flujoPedidoRepository.findAll().size();

        // Create the FlujoPedido

        restFlujoPedidoMockMvc.perform(post("/api/flujo-pedidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(flujoPedido)))
                .andExpect(status().isCreated());

        // Validate the FlujoPedido in the database
        List<FlujoPedido> flujoPedidos = flujoPedidoRepository.findAll();
        assertThat(flujoPedidos).hasSize(databaseSizeBeforeCreate + 1);
        FlujoPedido testFlujoPedido = flujoPedidos.get(flujoPedidos.size() - 1);
        assertThat(testFlujoPedido.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    public void getAllFlujoPedidos() throws Exception {
        // Initialize the database
        flujoPedidoRepository.saveAndFlush(flujoPedido);

        // Get all the flujoPedidos
        restFlujoPedidoMockMvc.perform(get("/api/flujo-pedidos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(flujoPedido.getId().intValue())))
                .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @Test
    @Transactional
    public void getFlujoPedido() throws Exception {
        // Initialize the database
        flujoPedidoRepository.saveAndFlush(flujoPedido);

        // Get the flujoPedido
        restFlujoPedidoMockMvc.perform(get("/api/flujo-pedidos/{id}", flujoPedido.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(flujoPedido.getId().intValue()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFlujoPedido() throws Exception {
        // Get the flujoPedido
        restFlujoPedidoMockMvc.perform(get("/api/flujo-pedidos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFlujoPedido() throws Exception {
        // Initialize the database
        flujoPedidoRepository.saveAndFlush(flujoPedido);
        int databaseSizeBeforeUpdate = flujoPedidoRepository.findAll().size();

        // Update the flujoPedido
        FlujoPedido updatedFlujoPedido = new FlujoPedido();
        updatedFlujoPedido.setId(flujoPedido.getId());
        updatedFlujoPedido.setEstado(UPDATED_ESTADO);

        restFlujoPedidoMockMvc.perform(put("/api/flujo-pedidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFlujoPedido)))
                .andExpect(status().isOk());

        // Validate the FlujoPedido in the database
        List<FlujoPedido> flujoPedidos = flujoPedidoRepository.findAll();
        assertThat(flujoPedidos).hasSize(databaseSizeBeforeUpdate);
        FlujoPedido testFlujoPedido = flujoPedidos.get(flujoPedidos.size() - 1);
        assertThat(testFlujoPedido.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void deleteFlujoPedido() throws Exception {
        // Initialize the database
        flujoPedidoRepository.saveAndFlush(flujoPedido);
        int databaseSizeBeforeDelete = flujoPedidoRepository.findAll().size();

        // Get the flujoPedido
        restFlujoPedidoMockMvc.perform(delete("/api/flujo-pedidos/{id}", flujoPedido.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FlujoPedido> flujoPedidos = flujoPedidoRepository.findAll();
        assertThat(flujoPedidos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
