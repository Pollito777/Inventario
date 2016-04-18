package org.gerald.inventario.web.rest;

import org.gerald.inventario.InventarioApp;
import org.gerald.inventario.domain.Permiso;
import org.gerald.inventario.repository.PermisoRepository;

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
 * Test class for the PermisoResource REST controller.
 *
 * @see PermisoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InventarioApp.class)
@WebAppConfiguration
@IntegrationTest
public class PermisoResourceIntTest {


    private static final Boolean DEFAULT_VER = false;
    private static final Boolean UPDATED_VER = true;

    private static final Boolean DEFAULT_CREAR = false;
    private static final Boolean UPDATED_CREAR = true;

    private static final Boolean DEFAULT_MODIFICAR = false;
    private static final Boolean UPDATED_MODIFICAR = true;

    private static final Boolean DEFAULT_AUTORIZAR = false;
    private static final Boolean UPDATED_AUTORIZAR = true;

    @Inject
    private PermisoRepository permisoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPermisoMockMvc;

    private Permiso permiso;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PermisoResource permisoResource = new PermisoResource();
        ReflectionTestUtils.setField(permisoResource, "permisoRepository", permisoRepository);
        this.restPermisoMockMvc = MockMvcBuilders.standaloneSetup(permisoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        permiso = new Permiso();
        permiso.setVer(DEFAULT_VER);
        permiso.setCrear(DEFAULT_CREAR);
        permiso.setModificar(DEFAULT_MODIFICAR);
        permiso.setAutorizar(DEFAULT_AUTORIZAR);
    }

    @Test
    @Transactional
    public void createPermiso() throws Exception {
        int databaseSizeBeforeCreate = permisoRepository.findAll().size();

        // Create the Permiso

        restPermisoMockMvc.perform(post("/api/permisos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(permiso)))
                .andExpect(status().isCreated());

        // Validate the Permiso in the database
        List<Permiso> permisos = permisoRepository.findAll();
        assertThat(permisos).hasSize(databaseSizeBeforeCreate + 1);
        Permiso testPermiso = permisos.get(permisos.size() - 1);
        assertThat(testPermiso.isVer()).isEqualTo(DEFAULT_VER);
        assertThat(testPermiso.isCrear()).isEqualTo(DEFAULT_CREAR);
        assertThat(testPermiso.isModificar()).isEqualTo(DEFAULT_MODIFICAR);
        assertThat(testPermiso.isAutorizar()).isEqualTo(DEFAULT_AUTORIZAR);
    }

    @Test
    @Transactional
    public void getAllPermisos() throws Exception {
        // Initialize the database
        permisoRepository.saveAndFlush(permiso);

        // Get all the permisos
        restPermisoMockMvc.perform(get("/api/permisos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(permiso.getId().intValue())))
                .andExpect(jsonPath("$.[*].ver").value(hasItem(DEFAULT_VER.booleanValue())))
                .andExpect(jsonPath("$.[*].crear").value(hasItem(DEFAULT_CREAR.booleanValue())))
                .andExpect(jsonPath("$.[*].modificar").value(hasItem(DEFAULT_MODIFICAR.booleanValue())))
                .andExpect(jsonPath("$.[*].autorizar").value(hasItem(DEFAULT_AUTORIZAR.booleanValue())));
    }

    @Test
    @Transactional
    public void getPermiso() throws Exception {
        // Initialize the database
        permisoRepository.saveAndFlush(permiso);

        // Get the permiso
        restPermisoMockMvc.perform(get("/api/permisos/{id}", permiso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(permiso.getId().intValue()))
            .andExpect(jsonPath("$.ver").value(DEFAULT_VER.booleanValue()))
            .andExpect(jsonPath("$.crear").value(DEFAULT_CREAR.booleanValue()))
            .andExpect(jsonPath("$.modificar").value(DEFAULT_MODIFICAR.booleanValue()))
            .andExpect(jsonPath("$.autorizar").value(DEFAULT_AUTORIZAR.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPermiso() throws Exception {
        // Get the permiso
        restPermisoMockMvc.perform(get("/api/permisos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePermiso() throws Exception {
        // Initialize the database
        permisoRepository.saveAndFlush(permiso);
        int databaseSizeBeforeUpdate = permisoRepository.findAll().size();

        // Update the permiso
        Permiso updatedPermiso = new Permiso();
        updatedPermiso.setId(permiso.getId());
        updatedPermiso.setVer(UPDATED_VER);
        updatedPermiso.setCrear(UPDATED_CREAR);
        updatedPermiso.setModificar(UPDATED_MODIFICAR);
        updatedPermiso.setAutorizar(UPDATED_AUTORIZAR);

        restPermisoMockMvc.perform(put("/api/permisos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPermiso)))
                .andExpect(status().isOk());

        // Validate the Permiso in the database
        List<Permiso> permisos = permisoRepository.findAll();
        assertThat(permisos).hasSize(databaseSizeBeforeUpdate);
        Permiso testPermiso = permisos.get(permisos.size() - 1);
        assertThat(testPermiso.isVer()).isEqualTo(UPDATED_VER);
        assertThat(testPermiso.isCrear()).isEqualTo(UPDATED_CREAR);
        assertThat(testPermiso.isModificar()).isEqualTo(UPDATED_MODIFICAR);
        assertThat(testPermiso.isAutorizar()).isEqualTo(UPDATED_AUTORIZAR);
    }

    @Test
    @Transactional
    public void deletePermiso() throws Exception {
        // Initialize the database
        permisoRepository.saveAndFlush(permiso);
        int databaseSizeBeforeDelete = permisoRepository.findAll().size();

        // Get the permiso
        restPermisoMockMvc.perform(delete("/api/permisos/{id}", permiso.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Permiso> permisos = permisoRepository.findAll();
        assertThat(permisos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
