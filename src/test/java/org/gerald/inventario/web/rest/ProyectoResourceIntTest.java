package org.gerald.inventario.web.rest;

import org.gerald.inventario.InventarioApp;
import org.gerald.inventario.domain.Proyecto;
import org.gerald.inventario.repository.ProyectoRepository;

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
 * Test class for the ProyectoResource REST controller.
 *
 * @see ProyectoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InventarioApp.class)
@WebAppConfiguration
@IntegrationTest
public class ProyectoResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";

    private static final Integer DEFAULT_NO_CONTROL = 1;
    private static final Integer UPDATED_NO_CONTROL = 2;
    private static final String DEFAULT_MODALIDAD = "AAAAA";
    private static final String UPDATED_MODALIDAD = "BBBBB";

    @Inject
    private ProyectoRepository proyectoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProyectoMockMvc;

    private Proyecto proyecto;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProyectoResource proyectoResource = new ProyectoResource();
        ReflectionTestUtils.setField(proyectoResource, "proyectoRepository", proyectoRepository);
        this.restProyectoMockMvc = MockMvcBuilders.standaloneSetup(proyectoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        proyecto = new Proyecto();
        proyecto.setNombre(DEFAULT_NOMBRE);
        proyecto.setNoControl(DEFAULT_NO_CONTROL);
        proyecto.setModalidad(DEFAULT_MODALIDAD);
    }

    @Test
    @Transactional
    public void createProyecto() throws Exception {
        int databaseSizeBeforeCreate = proyectoRepository.findAll().size();

        // Create the Proyecto

        restProyectoMockMvc.perform(post("/api/proyectos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(proyecto)))
                .andExpect(status().isCreated());

        // Validate the Proyecto in the database
        List<Proyecto> proyectos = proyectoRepository.findAll();
        assertThat(proyectos).hasSize(databaseSizeBeforeCreate + 1);
        Proyecto testProyecto = proyectos.get(proyectos.size() - 1);
        assertThat(testProyecto.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testProyecto.getNoControl()).isEqualTo(DEFAULT_NO_CONTROL);
        assertThat(testProyecto.getModalidad()).isEqualTo(DEFAULT_MODALIDAD);
    }

    @Test
    @Transactional
    public void getAllProyectos() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        // Get all the proyectos
        restProyectoMockMvc.perform(get("/api/proyectos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(proyecto.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].noControl").value(hasItem(DEFAULT_NO_CONTROL)))
                .andExpect(jsonPath("$.[*].modalidad").value(hasItem(DEFAULT_MODALIDAD.toString())));
    }

    @Test
    @Transactional
    public void getProyecto() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);

        // Get the proyecto
        restProyectoMockMvc.perform(get("/api/proyectos/{id}", proyecto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(proyecto.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.noControl").value(DEFAULT_NO_CONTROL))
            .andExpect(jsonPath("$.modalidad").value(DEFAULT_MODALIDAD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProyecto() throws Exception {
        // Get the proyecto
        restProyectoMockMvc.perform(get("/api/proyectos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProyecto() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);
        int databaseSizeBeforeUpdate = proyectoRepository.findAll().size();

        // Update the proyecto
        Proyecto updatedProyecto = new Proyecto();
        updatedProyecto.setId(proyecto.getId());
        updatedProyecto.setNombre(UPDATED_NOMBRE);
        updatedProyecto.setNoControl(UPDATED_NO_CONTROL);
        updatedProyecto.setModalidad(UPDATED_MODALIDAD);

        restProyectoMockMvc.perform(put("/api/proyectos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProyecto)))
                .andExpect(status().isOk());

        // Validate the Proyecto in the database
        List<Proyecto> proyectos = proyectoRepository.findAll();
        assertThat(proyectos).hasSize(databaseSizeBeforeUpdate);
        Proyecto testProyecto = proyectos.get(proyectos.size() - 1);
        assertThat(testProyecto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testProyecto.getNoControl()).isEqualTo(UPDATED_NO_CONTROL);
        assertThat(testProyecto.getModalidad()).isEqualTo(UPDATED_MODALIDAD);
    }

    @Test
    @Transactional
    public void deleteProyecto() throws Exception {
        // Initialize the database
        proyectoRepository.saveAndFlush(proyecto);
        int databaseSizeBeforeDelete = proyectoRepository.findAll().size();

        // Get the proyecto
        restProyectoMockMvc.perform(delete("/api/proyectos/{id}", proyecto.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Proyecto> proyectos = proyectoRepository.findAll();
        assertThat(proyectos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
