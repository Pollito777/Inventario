package org.gerald.inventario.web.rest;

import org.gerald.inventario.InventarioApp;
import org.gerald.inventario.domain.SolicitudCompra;
import org.gerald.inventario.repository.SolicitudCompraRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SolicitudCompraResource REST controller.
 *
 * @see SolicitudCompraResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InventarioApp.class)
@WebAppConfiguration
@IntegrationTest
public class SolicitudCompraResourceIntTest {


    private static final Integer DEFAULT_NUMERO_SOLICITUD = 1;
    private static final Integer UPDATED_NUMERO_SOLICITUD = 2;
    private static final String DEFAULT_JUSTIFICACION = "AAAAA";
    private static final String UPDATED_JUSTIFICACION = "BBBBB";

    private static final Integer DEFAULT_AUTORIZA_ID = 1;
    private static final Integer UPDATED_AUTORIZA_ID = 2;

    private static final Integer DEFAULT_ORDEN_ID = 1;
    private static final Integer UPDATED_ORDEN_ID = 2;

    private static final LocalDate DEFAULT_FECHA_SOLICITUD = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_SOLICITUD = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_AUTORIZA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_AUTORIZA = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_ORDEN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ORDEN = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private SolicitudCompraRepository solicitudCompraRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSolicitudCompraMockMvc;

    private SolicitudCompra solicitudCompra;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SolicitudCompraResource solicitudCompraResource = new SolicitudCompraResource();
        ReflectionTestUtils.setField(solicitudCompraResource, "solicitudCompraRepository", solicitudCompraRepository);
        this.restSolicitudCompraMockMvc = MockMvcBuilders.standaloneSetup(solicitudCompraResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        solicitudCompra = new SolicitudCompra();
        solicitudCompra.setNumeroSolicitud(DEFAULT_NUMERO_SOLICITUD);
        solicitudCompra.setJustificacion(DEFAULT_JUSTIFICACION);
        solicitudCompra.setAutorizaId(DEFAULT_AUTORIZA_ID);
        solicitudCompra.setOrdenId(DEFAULT_ORDEN_ID);
        solicitudCompra.setFechaSolicitud(DEFAULT_FECHA_SOLICITUD);
        solicitudCompra.setFechaAutoriza(DEFAULT_FECHA_AUTORIZA);
        solicitudCompra.setFechaOrden(DEFAULT_FECHA_ORDEN);
    }

    @Test
    @Transactional
    public void createSolicitudCompra() throws Exception {
        int databaseSizeBeforeCreate = solicitudCompraRepository.findAll().size();

        // Create the SolicitudCompra

        restSolicitudCompraMockMvc.perform(post("/api/solicitud-compras")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(solicitudCompra)))
                .andExpect(status().isCreated());

        // Validate the SolicitudCompra in the database
        List<SolicitudCompra> solicitudCompras = solicitudCompraRepository.findAll();
        assertThat(solicitudCompras).hasSize(databaseSizeBeforeCreate + 1);
        SolicitudCompra testSolicitudCompra = solicitudCompras.get(solicitudCompras.size() - 1);
        assertThat(testSolicitudCompra.getNumeroSolicitud()).isEqualTo(DEFAULT_NUMERO_SOLICITUD);
        assertThat(testSolicitudCompra.getJustificacion()).isEqualTo(DEFAULT_JUSTIFICACION);
        assertThat(testSolicitudCompra.getAutorizaId()).isEqualTo(DEFAULT_AUTORIZA_ID);
        assertThat(testSolicitudCompra.getOrdenId()).isEqualTo(DEFAULT_ORDEN_ID);
        assertThat(testSolicitudCompra.getFechaSolicitud()).isEqualTo(DEFAULT_FECHA_SOLICITUD);
        assertThat(testSolicitudCompra.getFechaAutoriza()).isEqualTo(DEFAULT_FECHA_AUTORIZA);
        assertThat(testSolicitudCompra.getFechaOrden()).isEqualTo(DEFAULT_FECHA_ORDEN);
    }

    @Test
    @Transactional
    public void checkFechaSolicitudIsRequired() throws Exception {
        int databaseSizeBeforeTest = solicitudCompraRepository.findAll().size();
        // set the field null
        solicitudCompra.setFechaSolicitud(null);

        // Create the SolicitudCompra, which fails.

        restSolicitudCompraMockMvc.perform(post("/api/solicitud-compras")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(solicitudCompra)))
                .andExpect(status().isBadRequest());

        List<SolicitudCompra> solicitudCompras = solicitudCompraRepository.findAll();
        assertThat(solicitudCompras).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSolicitudCompras() throws Exception {
        // Initialize the database
        solicitudCompraRepository.saveAndFlush(solicitudCompra);

        // Get all the solicitudCompras
        restSolicitudCompraMockMvc.perform(get("/api/solicitud-compras?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(solicitudCompra.getId().intValue())))
                .andExpect(jsonPath("$.[*].numeroSolicitud").value(hasItem(DEFAULT_NUMERO_SOLICITUD)))
                .andExpect(jsonPath("$.[*].justificacion").value(hasItem(DEFAULT_JUSTIFICACION.toString())))
                .andExpect(jsonPath("$.[*].autorizaId").value(hasItem(DEFAULT_AUTORIZA_ID)))
                .andExpect(jsonPath("$.[*].ordenId").value(hasItem(DEFAULT_ORDEN_ID)))
                .andExpect(jsonPath("$.[*].fechaSolicitud").value(hasItem(DEFAULT_FECHA_SOLICITUD.toString())))
                .andExpect(jsonPath("$.[*].fechaAutoriza").value(hasItem(DEFAULT_FECHA_AUTORIZA.toString())))
                .andExpect(jsonPath("$.[*].fechaOrden").value(hasItem(DEFAULT_FECHA_ORDEN.toString())));
    }

    @Test
    @Transactional
    public void getSolicitudCompra() throws Exception {
        // Initialize the database
        solicitudCompraRepository.saveAndFlush(solicitudCompra);

        // Get the solicitudCompra
        restSolicitudCompraMockMvc.perform(get("/api/solicitud-compras/{id}", solicitudCompra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(solicitudCompra.getId().intValue()))
            .andExpect(jsonPath("$.numeroSolicitud").value(DEFAULT_NUMERO_SOLICITUD))
            .andExpect(jsonPath("$.justificacion").value(DEFAULT_JUSTIFICACION.toString()))
            .andExpect(jsonPath("$.autorizaId").value(DEFAULT_AUTORIZA_ID))
            .andExpect(jsonPath("$.ordenId").value(DEFAULT_ORDEN_ID))
            .andExpect(jsonPath("$.fechaSolicitud").value(DEFAULT_FECHA_SOLICITUD.toString()))
            .andExpect(jsonPath("$.fechaAutoriza").value(DEFAULT_FECHA_AUTORIZA.toString()))
            .andExpect(jsonPath("$.fechaOrden").value(DEFAULT_FECHA_ORDEN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSolicitudCompra() throws Exception {
        // Get the solicitudCompra
        restSolicitudCompraMockMvc.perform(get("/api/solicitud-compras/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSolicitudCompra() throws Exception {
        // Initialize the database
        solicitudCompraRepository.saveAndFlush(solicitudCompra);
        int databaseSizeBeforeUpdate = solicitudCompraRepository.findAll().size();

        // Update the solicitudCompra
        SolicitudCompra updatedSolicitudCompra = new SolicitudCompra();
        updatedSolicitudCompra.setId(solicitudCompra.getId());
        updatedSolicitudCompra.setNumeroSolicitud(UPDATED_NUMERO_SOLICITUD);
        updatedSolicitudCompra.setJustificacion(UPDATED_JUSTIFICACION);
        updatedSolicitudCompra.setAutorizaId(UPDATED_AUTORIZA_ID);
        updatedSolicitudCompra.setOrdenId(UPDATED_ORDEN_ID);
        updatedSolicitudCompra.setFechaSolicitud(UPDATED_FECHA_SOLICITUD);
        updatedSolicitudCompra.setFechaAutoriza(UPDATED_FECHA_AUTORIZA);
        updatedSolicitudCompra.setFechaOrden(UPDATED_FECHA_ORDEN);

        restSolicitudCompraMockMvc.perform(put("/api/solicitud-compras")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSolicitudCompra)))
                .andExpect(status().isOk());

        // Validate the SolicitudCompra in the database
        List<SolicitudCompra> solicitudCompras = solicitudCompraRepository.findAll();
        assertThat(solicitudCompras).hasSize(databaseSizeBeforeUpdate);
        SolicitudCompra testSolicitudCompra = solicitudCompras.get(solicitudCompras.size() - 1);
        assertThat(testSolicitudCompra.getNumeroSolicitud()).isEqualTo(UPDATED_NUMERO_SOLICITUD);
        assertThat(testSolicitudCompra.getJustificacion()).isEqualTo(UPDATED_JUSTIFICACION);
        assertThat(testSolicitudCompra.getAutorizaId()).isEqualTo(UPDATED_AUTORIZA_ID);
        assertThat(testSolicitudCompra.getOrdenId()).isEqualTo(UPDATED_ORDEN_ID);
        assertThat(testSolicitudCompra.getFechaSolicitud()).isEqualTo(UPDATED_FECHA_SOLICITUD);
        assertThat(testSolicitudCompra.getFechaAutoriza()).isEqualTo(UPDATED_FECHA_AUTORIZA);
        assertThat(testSolicitudCompra.getFechaOrden()).isEqualTo(UPDATED_FECHA_ORDEN);
    }

    @Test
    @Transactional
    public void deleteSolicitudCompra() throws Exception {
        // Initialize the database
        solicitudCompraRepository.saveAndFlush(solicitudCompra);
        int databaseSizeBeforeDelete = solicitudCompraRepository.findAll().size();

        // Get the solicitudCompra
        restSolicitudCompraMockMvc.perform(delete("/api/solicitud-compras/{id}", solicitudCompra.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SolicitudCompra> solicitudCompras = solicitudCompraRepository.findAll();
        assertThat(solicitudCompras).hasSize(databaseSizeBeforeDelete - 1);
    }
}
