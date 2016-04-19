package org.gerald.inventario.web.rest;

import org.gerald.inventario.InventarioApp;
import org.gerald.inventario.domain.LinSolicitud;
import org.gerald.inventario.repository.LinSolicitudRepository;

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
 * Test class for the LinSolicitudResource REST controller.
 *
 * @see LinSolicitudResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InventarioApp.class)
@WebAppConfiguration
@IntegrationTest
public class LinSolicitudResourceIntTest {


    private static final Integer DEFAULT_CANTIDAD_SOLICITADA = 1;
    private static final Integer UPDATED_CANTIDAD_SOLICITADA = 2;

    private static final Integer DEFAULT_CANTIDAD_AUTORIZADA = 1;
    private static final Integer UPDATED_CANTIDAD_AUTORIZADA = 2;

    private static final Integer DEFAULT_CANTIDAD_COMPRADA = 1;
    private static final Integer UPDATED_CANTIDAD_COMPRADA = 2;

    private static final Float DEFAULT_PRECIO_UNITARIO = 1F;
    private static final Float UPDATED_PRECIO_UNITARIO = 2F;

    @Inject
    private LinSolicitudRepository linSolicitudRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLinSolicitudMockMvc;

    private LinSolicitud linSolicitud;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LinSolicitudResource linSolicitudResource = new LinSolicitudResource();
        ReflectionTestUtils.setField(linSolicitudResource, "linSolicitudRepository", linSolicitudRepository);
        this.restLinSolicitudMockMvc = MockMvcBuilders.standaloneSetup(linSolicitudResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        linSolicitud = new LinSolicitud();
        linSolicitud.setCantidadSolicitada(DEFAULT_CANTIDAD_SOLICITADA);
        linSolicitud.setCantidadAutorizada(DEFAULT_CANTIDAD_AUTORIZADA);
        linSolicitud.setCantidadComprada(DEFAULT_CANTIDAD_COMPRADA);
        linSolicitud.setPrecioUnitario(DEFAULT_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    public void createLinSolicitud() throws Exception {
        int databaseSizeBeforeCreate = linSolicitudRepository.findAll().size();

        // Create the LinSolicitud

        restLinSolicitudMockMvc.perform(post("/api/lin-solicituds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(linSolicitud)))
                .andExpect(status().isCreated());

        // Validate the LinSolicitud in the database
        List<LinSolicitud> linSolicituds = linSolicitudRepository.findAll();
        assertThat(linSolicituds).hasSize(databaseSizeBeforeCreate + 1);
        LinSolicitud testLinSolicitud = linSolicituds.get(linSolicituds.size() - 1);
        assertThat(testLinSolicitud.getCantidadSolicitada()).isEqualTo(DEFAULT_CANTIDAD_SOLICITADA);
        assertThat(testLinSolicitud.getCantidadAutorizada()).isEqualTo(DEFAULT_CANTIDAD_AUTORIZADA);
        assertThat(testLinSolicitud.getCantidadComprada()).isEqualTo(DEFAULT_CANTIDAD_COMPRADA);
        assertThat(testLinSolicitud.getPrecioUnitario()).isEqualTo(DEFAULT_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    public void checkPrecioUnitarioIsRequired() throws Exception {
        int databaseSizeBeforeTest = linSolicitudRepository.findAll().size();
        // set the field null
        linSolicitud.setPrecioUnitario(null);

        // Create the LinSolicitud, which fails.

        restLinSolicitudMockMvc.perform(post("/api/lin-solicituds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(linSolicitud)))
                .andExpect(status().isBadRequest());

        List<LinSolicitud> linSolicituds = linSolicitudRepository.findAll();
        assertThat(linSolicituds).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLinSolicituds() throws Exception {
        // Initialize the database
        linSolicitudRepository.saveAndFlush(linSolicitud);

        // Get all the linSolicituds
        restLinSolicitudMockMvc.perform(get("/api/lin-solicituds?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(linSolicitud.getId().intValue())))
                .andExpect(jsonPath("$.[*].cantidadSolicitada").value(hasItem(DEFAULT_CANTIDAD_SOLICITADA)))
                .andExpect(jsonPath("$.[*].cantidadAutorizada").value(hasItem(DEFAULT_CANTIDAD_AUTORIZADA)))
                .andExpect(jsonPath("$.[*].cantidadComprada").value(hasItem(DEFAULT_CANTIDAD_COMPRADA)))
                .andExpect(jsonPath("$.[*].precioUnitario").value(hasItem(DEFAULT_PRECIO_UNITARIO.doubleValue())));
    }

    @Test
    @Transactional
    public void getLinSolicitud() throws Exception {
        // Initialize the database
        linSolicitudRepository.saveAndFlush(linSolicitud);

        // Get the linSolicitud
        restLinSolicitudMockMvc.perform(get("/api/lin-solicituds/{id}", linSolicitud.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(linSolicitud.getId().intValue()))
            .andExpect(jsonPath("$.cantidadSolicitada").value(DEFAULT_CANTIDAD_SOLICITADA))
            .andExpect(jsonPath("$.cantidadAutorizada").value(DEFAULT_CANTIDAD_AUTORIZADA))
            .andExpect(jsonPath("$.cantidadComprada").value(DEFAULT_CANTIDAD_COMPRADA))
            .andExpect(jsonPath("$.precioUnitario").value(DEFAULT_PRECIO_UNITARIO.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLinSolicitud() throws Exception {
        // Get the linSolicitud
        restLinSolicitudMockMvc.perform(get("/api/lin-solicituds/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLinSolicitud() throws Exception {
        // Initialize the database
        linSolicitudRepository.saveAndFlush(linSolicitud);
        int databaseSizeBeforeUpdate = linSolicitudRepository.findAll().size();

        // Update the linSolicitud
        LinSolicitud updatedLinSolicitud = new LinSolicitud();
        updatedLinSolicitud.setId(linSolicitud.getId());
        updatedLinSolicitud.setCantidadSolicitada(UPDATED_CANTIDAD_SOLICITADA);
        updatedLinSolicitud.setCantidadAutorizada(UPDATED_CANTIDAD_AUTORIZADA);
        updatedLinSolicitud.setCantidadComprada(UPDATED_CANTIDAD_COMPRADA);
        updatedLinSolicitud.setPrecioUnitario(UPDATED_PRECIO_UNITARIO);

        restLinSolicitudMockMvc.perform(put("/api/lin-solicituds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLinSolicitud)))
                .andExpect(status().isOk());

        // Validate the LinSolicitud in the database
        List<LinSolicitud> linSolicituds = linSolicitudRepository.findAll();
        assertThat(linSolicituds).hasSize(databaseSizeBeforeUpdate);
        LinSolicitud testLinSolicitud = linSolicituds.get(linSolicituds.size() - 1);
        assertThat(testLinSolicitud.getCantidadSolicitada()).isEqualTo(UPDATED_CANTIDAD_SOLICITADA);
        assertThat(testLinSolicitud.getCantidadAutorizada()).isEqualTo(UPDATED_CANTIDAD_AUTORIZADA);
        assertThat(testLinSolicitud.getCantidadComprada()).isEqualTo(UPDATED_CANTIDAD_COMPRADA);
        assertThat(testLinSolicitud.getPrecioUnitario()).isEqualTo(UPDATED_PRECIO_UNITARIO);
    }

    @Test
    @Transactional
    public void deleteLinSolicitud() throws Exception {
        // Initialize the database
        linSolicitudRepository.saveAndFlush(linSolicitud);
        int databaseSizeBeforeDelete = linSolicitudRepository.findAll().size();

        // Get the linSolicitud
        restLinSolicitudMockMvc.perform(delete("/api/lin-solicituds/{id}", linSolicitud.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<LinSolicitud> linSolicituds = linSolicitudRepository.findAll();
        assertThat(linSolicituds).hasSize(databaseSizeBeforeDelete - 1);
    }
}
