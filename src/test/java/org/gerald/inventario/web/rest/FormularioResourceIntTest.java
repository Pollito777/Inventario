package org.gerald.inventario.web.rest;

import org.gerald.inventario.InventarioApp;
import org.gerald.inventario.domain.Formulario;
import org.gerald.inventario.repository.FormularioRepository;

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
 * Test class for the FormularioResource REST controller.
 *
 * @see FormularioResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InventarioApp.class)
@WebAppConfiguration
@IntegrationTest
public class FormularioResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";
    private static final String DEFAULT_DESCRIPCION = "AAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBB";

    @Inject
    private FormularioRepository formularioRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFormularioMockMvc;

    private Formulario formulario;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FormularioResource formularioResource = new FormularioResource();
        ReflectionTestUtils.setField(formularioResource, "formularioRepository", formularioRepository);
        this.restFormularioMockMvc = MockMvcBuilders.standaloneSetup(formularioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        formulario = new Formulario();
        formulario.setNombre(DEFAULT_NOMBRE);
        formulario.setDescripcion(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createFormulario() throws Exception {
        int databaseSizeBeforeCreate = formularioRepository.findAll().size();

        // Create the Formulario

        restFormularioMockMvc.perform(post("/api/formularios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(formulario)))
                .andExpect(status().isCreated());

        // Validate the Formulario in the database
        List<Formulario> formularios = formularioRepository.findAll();
        assertThat(formularios).hasSize(databaseSizeBeforeCreate + 1);
        Formulario testFormulario = formularios.get(formularios.size() - 1);
        assertThat(testFormulario.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testFormulario.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllFormularios() throws Exception {
        // Initialize the database
        formularioRepository.saveAndFlush(formulario);

        // Get all the formularios
        restFormularioMockMvc.perform(get("/api/formularios?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(formulario.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }

    @Test
    @Transactional
    public void getFormulario() throws Exception {
        // Initialize the database
        formularioRepository.saveAndFlush(formulario);

        // Get the formulario
        restFormularioMockMvc.perform(get("/api/formularios/{id}", formulario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(formulario.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFormulario() throws Exception {
        // Get the formulario
        restFormularioMockMvc.perform(get("/api/formularios/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormulario() throws Exception {
        // Initialize the database
        formularioRepository.saveAndFlush(formulario);
        int databaseSizeBeforeUpdate = formularioRepository.findAll().size();

        // Update the formulario
        Formulario updatedFormulario = new Formulario();
        updatedFormulario.setId(formulario.getId());
        updatedFormulario.setNombre(UPDATED_NOMBRE);
        updatedFormulario.setDescripcion(UPDATED_DESCRIPCION);

        restFormularioMockMvc.perform(put("/api/formularios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFormulario)))
                .andExpect(status().isOk());

        // Validate the Formulario in the database
        List<Formulario> formularios = formularioRepository.findAll();
        assertThat(formularios).hasSize(databaseSizeBeforeUpdate);
        Formulario testFormulario = formularios.get(formularios.size() - 1);
        assertThat(testFormulario.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testFormulario.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void deleteFormulario() throws Exception {
        // Initialize the database
        formularioRepository.saveAndFlush(formulario);
        int databaseSizeBeforeDelete = formularioRepository.findAll().size();

        // Get the formulario
        restFormularioMockMvc.perform(delete("/api/formularios/{id}", formulario.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Formulario> formularios = formularioRepository.findAll();
        assertThat(formularios).hasSize(databaseSizeBeforeDelete - 1);
    }
}
