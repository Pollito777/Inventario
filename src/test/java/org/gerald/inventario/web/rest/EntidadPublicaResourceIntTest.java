package org.gerald.inventario.web.rest;

import org.gerald.inventario.InventarioApp;
import org.gerald.inventario.domain.EntidadPublica;
import org.gerald.inventario.repository.EntidadPublicaRepository;

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
 * Test class for the EntidadPublicaResource REST controller.
 *
 * @see EntidadPublicaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InventarioApp.class)
@WebAppConfiguration
@IntegrationTest
public class EntidadPublicaResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";
    private static final String DEFAULT_DIRECCION = "AAAAA";
    private static final String UPDATED_DIRECCION = "BBBBB";
    private static final String DEFAULT_TIPO = "AAAAA";
    private static final String UPDATED_TIPO = "BBBBB";

    @Inject
    private EntidadPublicaRepository entidadPublicaRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEntidadPublicaMockMvc;

    private EntidadPublica entidadPublica;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EntidadPublicaResource entidadPublicaResource = new EntidadPublicaResource();
        ReflectionTestUtils.setField(entidadPublicaResource, "entidadPublicaRepository", entidadPublicaRepository);
        this.restEntidadPublicaMockMvc = MockMvcBuilders.standaloneSetup(entidadPublicaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        entidadPublica = new EntidadPublica();
        entidadPublica.setNombre(DEFAULT_NOMBRE);
        entidadPublica.setDireccion(DEFAULT_DIRECCION);
        entidadPublica.setTipo(DEFAULT_TIPO);
    }

    @Test
    @Transactional
    public void createEntidadPublica() throws Exception {
        int databaseSizeBeforeCreate = entidadPublicaRepository.findAll().size();

        // Create the EntidadPublica

        restEntidadPublicaMockMvc.perform(post("/api/entidad-publicas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(entidadPublica)))
                .andExpect(status().isCreated());

        // Validate the EntidadPublica in the database
        List<EntidadPublica> entidadPublicas = entidadPublicaRepository.findAll();
        assertThat(entidadPublicas).hasSize(databaseSizeBeforeCreate + 1);
        EntidadPublica testEntidadPublica = entidadPublicas.get(entidadPublicas.size() - 1);
        assertThat(testEntidadPublica.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEntidadPublica.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testEntidadPublica.getTipo()).isEqualTo(DEFAULT_TIPO);
    }

    @Test
    @Transactional
    public void getAllEntidadPublicas() throws Exception {
        // Initialize the database
        entidadPublicaRepository.saveAndFlush(entidadPublica);

        // Get all the entidadPublicas
        restEntidadPublicaMockMvc.perform(get("/api/entidad-publicas?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(entidadPublica.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION.toString())))
                .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())));
    }

    @Test
    @Transactional
    public void getEntidadPublica() throws Exception {
        // Initialize the database
        entidadPublicaRepository.saveAndFlush(entidadPublica);

        // Get the entidadPublica
        restEntidadPublicaMockMvc.perform(get("/api/entidad-publicas/{id}", entidadPublica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(entidadPublica.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION.toString()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEntidadPublica() throws Exception {
        // Get the entidadPublica
        restEntidadPublicaMockMvc.perform(get("/api/entidad-publicas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntidadPublica() throws Exception {
        // Initialize the database
        entidadPublicaRepository.saveAndFlush(entidadPublica);
        int databaseSizeBeforeUpdate = entidadPublicaRepository.findAll().size();

        // Update the entidadPublica
        EntidadPublica updatedEntidadPublica = new EntidadPublica();
        updatedEntidadPublica.setId(entidadPublica.getId());
        updatedEntidadPublica.setNombre(UPDATED_NOMBRE);
        updatedEntidadPublica.setDireccion(UPDATED_DIRECCION);
        updatedEntidadPublica.setTipo(UPDATED_TIPO);

        restEntidadPublicaMockMvc.perform(put("/api/entidad-publicas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEntidadPublica)))
                .andExpect(status().isOk());

        // Validate the EntidadPublica in the database
        List<EntidadPublica> entidadPublicas = entidadPublicaRepository.findAll();
        assertThat(entidadPublicas).hasSize(databaseSizeBeforeUpdate);
        EntidadPublica testEntidadPublica = entidadPublicas.get(entidadPublicas.size() - 1);
        assertThat(testEntidadPublica.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEntidadPublica.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testEntidadPublica.getTipo()).isEqualTo(UPDATED_TIPO);
    }

    @Test
    @Transactional
    public void deleteEntidadPublica() throws Exception {
        // Initialize the database
        entidadPublicaRepository.saveAndFlush(entidadPublica);
        int databaseSizeBeforeDelete = entidadPublicaRepository.findAll().size();

        // Get the entidadPublica
        restEntidadPublicaMockMvc.perform(delete("/api/entidad-publicas/{id}", entidadPublica.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<EntidadPublica> entidadPublicas = entidadPublicaRepository.findAll();
        assertThat(entidadPublicas).hasSize(databaseSizeBeforeDelete - 1);
    }
}
