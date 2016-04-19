package org.gerald.inventario.web.rest;

import org.gerald.inventario.InventarioApp;
import org.gerald.inventario.domain.Pedido;
import org.gerald.inventario.repository.PedidoRepository;

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
 * Test class for the PedidoResource REST controller.
 *
 * @see PedidoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InventarioApp.class)
@WebAppConfiguration
@IntegrationTest
public class PedidoResourceIntTest {


    private static final Integer DEFAULT_NUMERO_PEDIDO = 1;
    private static final Integer UPDATED_NUMERO_PEDIDO = 2;
    private static final String DEFAULT_JUSTIFICACION = "AAAAA";
    private static final String UPDATED_JUSTIFICACION = "BBBBB";

    private static final Integer DEFAULT_AUTORIZA_ID = 1;
    private static final Integer UPDATED_AUTORIZA_ID = 2;

    private static final LocalDate DEFAULT_FECHA_PEDIDO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_PEDIDO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_AUTORIZA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_AUTORIZA = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private PedidoRepository pedidoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPedidoMockMvc;

    private Pedido pedido;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PedidoResource pedidoResource = new PedidoResource();
        ReflectionTestUtils.setField(pedidoResource, "pedidoRepository", pedidoRepository);
        this.restPedidoMockMvc = MockMvcBuilders.standaloneSetup(pedidoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        pedido = new Pedido();
        pedido.setNumeroPedido(DEFAULT_NUMERO_PEDIDO);
        pedido.setJustificacion(DEFAULT_JUSTIFICACION);
        pedido.setAutorizaId(DEFAULT_AUTORIZA_ID);
        pedido.setFechaPedido(DEFAULT_FECHA_PEDIDO);
        pedido.setFechaAutoriza(DEFAULT_FECHA_AUTORIZA);
    }

    @Test
    @Transactional
    public void createPedido() throws Exception {
        int databaseSizeBeforeCreate = pedidoRepository.findAll().size();

        // Create the Pedido

        restPedidoMockMvc.perform(post("/api/pedidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pedido)))
                .andExpect(status().isCreated());

        // Validate the Pedido in the database
        List<Pedido> pedidos = pedidoRepository.findAll();
        assertThat(pedidos).hasSize(databaseSizeBeforeCreate + 1);
        Pedido testPedido = pedidos.get(pedidos.size() - 1);
        assertThat(testPedido.getNumeroPedido()).isEqualTo(DEFAULT_NUMERO_PEDIDO);
        assertThat(testPedido.getJustificacion()).isEqualTo(DEFAULT_JUSTIFICACION);
        assertThat(testPedido.getAutorizaId()).isEqualTo(DEFAULT_AUTORIZA_ID);
        assertThat(testPedido.getFechaPedido()).isEqualTo(DEFAULT_FECHA_PEDIDO);
        assertThat(testPedido.getFechaAutoriza()).isEqualTo(DEFAULT_FECHA_AUTORIZA);
    }

    @Test
    @Transactional
    public void checkFechaPedidoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pedidoRepository.findAll().size();
        // set the field null
        pedido.setFechaPedido(null);

        // Create the Pedido, which fails.

        restPedidoMockMvc.perform(post("/api/pedidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pedido)))
                .andExpect(status().isBadRequest());

        List<Pedido> pedidos = pedidoRepository.findAll();
        assertThat(pedidos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPedidos() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidos
        restPedidoMockMvc.perform(get("/api/pedidos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pedido.getId().intValue())))
                .andExpect(jsonPath("$.[*].numeroPedido").value(hasItem(DEFAULT_NUMERO_PEDIDO)))
                .andExpect(jsonPath("$.[*].justificacion").value(hasItem(DEFAULT_JUSTIFICACION.toString())))
                .andExpect(jsonPath("$.[*].autorizaId").value(hasItem(DEFAULT_AUTORIZA_ID)))
                .andExpect(jsonPath("$.[*].fechaPedido").value(hasItem(DEFAULT_FECHA_PEDIDO.toString())))
                .andExpect(jsonPath("$.[*].fechaAutoriza").value(hasItem(DEFAULT_FECHA_AUTORIZA.toString())));
    }

    @Test
    @Transactional
    public void getPedido() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get the pedido
        restPedidoMockMvc.perform(get("/api/pedidos/{id}", pedido.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(pedido.getId().intValue()))
            .andExpect(jsonPath("$.numeroPedido").value(DEFAULT_NUMERO_PEDIDO))
            .andExpect(jsonPath("$.justificacion").value(DEFAULT_JUSTIFICACION.toString()))
            .andExpect(jsonPath("$.autorizaId").value(DEFAULT_AUTORIZA_ID))
            .andExpect(jsonPath("$.fechaPedido").value(DEFAULT_FECHA_PEDIDO.toString()))
            .andExpect(jsonPath("$.fechaAutoriza").value(DEFAULT_FECHA_AUTORIZA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPedido() throws Exception {
        // Get the pedido
        restPedidoMockMvc.perform(get("/api/pedidos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePedido() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);
        int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();

        // Update the pedido
        Pedido updatedPedido = new Pedido();
        updatedPedido.setId(pedido.getId());
        updatedPedido.setNumeroPedido(UPDATED_NUMERO_PEDIDO);
        updatedPedido.setJustificacion(UPDATED_JUSTIFICACION);
        updatedPedido.setAutorizaId(UPDATED_AUTORIZA_ID);
        updatedPedido.setFechaPedido(UPDATED_FECHA_PEDIDO);
        updatedPedido.setFechaAutoriza(UPDATED_FECHA_AUTORIZA);

        restPedidoMockMvc.perform(put("/api/pedidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPedido)))
                .andExpect(status().isOk());

        // Validate the Pedido in the database
        List<Pedido> pedidos = pedidoRepository.findAll();
        assertThat(pedidos).hasSize(databaseSizeBeforeUpdate);
        Pedido testPedido = pedidos.get(pedidos.size() - 1);
        assertThat(testPedido.getNumeroPedido()).isEqualTo(UPDATED_NUMERO_PEDIDO);
        assertThat(testPedido.getJustificacion()).isEqualTo(UPDATED_JUSTIFICACION);
        assertThat(testPedido.getAutorizaId()).isEqualTo(UPDATED_AUTORIZA_ID);
        assertThat(testPedido.getFechaPedido()).isEqualTo(UPDATED_FECHA_PEDIDO);
        assertThat(testPedido.getFechaAutoriza()).isEqualTo(UPDATED_FECHA_AUTORIZA);
    }

    @Test
    @Transactional
    public void deletePedido() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);
        int databaseSizeBeforeDelete = pedidoRepository.findAll().size();

        // Get the pedido
        restPedidoMockMvc.perform(delete("/api/pedidos/{id}", pedido.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Pedido> pedidos = pedidoRepository.findAll();
        assertThat(pedidos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
