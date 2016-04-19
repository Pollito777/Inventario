package org.gerald.inventario.web.rest;

import org.gerald.inventario.InventarioApp;
import org.gerald.inventario.domain.FlujoCompra;
import org.gerald.inventario.repository.FlujoCompraRepository;

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
 * Test class for the FlujoCompraResource REST controller.
 *
 * @see FlujoCompraResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InventarioApp.class)
@WebAppConfiguration
@IntegrationTest
public class FlujoCompraResourceIntTest {

    private static final String DEFAULT_ESTADO = "AAAAA";
    private static final String UPDATED_ESTADO = "BBBBB";

    @Inject
    private FlujoCompraRepository flujoCompraRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFlujoCompraMockMvc;

    private FlujoCompra flujoCompra;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FlujoCompraResource flujoCompraResource = new FlujoCompraResource();
        ReflectionTestUtils.setField(flujoCompraResource, "flujoCompraRepository", flujoCompraRepository);
        this.restFlujoCompraMockMvc = MockMvcBuilders.standaloneSetup(flujoCompraResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        flujoCompra = new FlujoCompra();
        flujoCompra.setEstado(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    public void createFlujoCompra() throws Exception {
        int databaseSizeBeforeCreate = flujoCompraRepository.findAll().size();

        // Create the FlujoCompra

        restFlujoCompraMockMvc.perform(post("/api/flujo-compras")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(flujoCompra)))
                .andExpect(status().isCreated());

        // Validate the FlujoCompra in the database
        List<FlujoCompra> flujoCompras = flujoCompraRepository.findAll();
        assertThat(flujoCompras).hasSize(databaseSizeBeforeCreate + 1);
        FlujoCompra testFlujoCompra = flujoCompras.get(flujoCompras.size() - 1);
        assertThat(testFlujoCompra.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    public void getAllFlujoCompras() throws Exception {
        // Initialize the database
        flujoCompraRepository.saveAndFlush(flujoCompra);

        // Get all the flujoCompras
        restFlujoCompraMockMvc.perform(get("/api/flujo-compras?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(flujoCompra.getId().intValue())))
                .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @Test
    @Transactional
    public void getFlujoCompra() throws Exception {
        // Initialize the database
        flujoCompraRepository.saveAndFlush(flujoCompra);

        // Get the flujoCompra
        restFlujoCompraMockMvc.perform(get("/api/flujo-compras/{id}", flujoCompra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(flujoCompra.getId().intValue()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFlujoCompra() throws Exception {
        // Get the flujoCompra
        restFlujoCompraMockMvc.perform(get("/api/flujo-compras/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFlujoCompra() throws Exception {
        // Initialize the database
        flujoCompraRepository.saveAndFlush(flujoCompra);
        int databaseSizeBeforeUpdate = flujoCompraRepository.findAll().size();

        // Update the flujoCompra
        FlujoCompra updatedFlujoCompra = new FlujoCompra();
        updatedFlujoCompra.setId(flujoCompra.getId());
        updatedFlujoCompra.setEstado(UPDATED_ESTADO);

        restFlujoCompraMockMvc.perform(put("/api/flujo-compras")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFlujoCompra)))
                .andExpect(status().isOk());

        // Validate the FlujoCompra in the database
        List<FlujoCompra> flujoCompras = flujoCompraRepository.findAll();
        assertThat(flujoCompras).hasSize(databaseSizeBeforeUpdate);
        FlujoCompra testFlujoCompra = flujoCompras.get(flujoCompras.size() - 1);
        assertThat(testFlujoCompra.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void deleteFlujoCompra() throws Exception {
        // Initialize the database
        flujoCompraRepository.saveAndFlush(flujoCompra);
        int databaseSizeBeforeDelete = flujoCompraRepository.findAll().size();

        // Get the flujoCompra
        restFlujoCompraMockMvc.perform(delete("/api/flujo-compras/{id}", flujoCompra.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FlujoCompra> flujoCompras = flujoCompraRepository.findAll();
        assertThat(flujoCompras).hasSize(databaseSizeBeforeDelete - 1);
    }
}
