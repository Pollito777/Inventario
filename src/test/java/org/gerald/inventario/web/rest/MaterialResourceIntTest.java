package org.gerald.inventario.web.rest;

import org.gerald.inventario.InventarioApp;
import org.gerald.inventario.domain.Material;
import org.gerald.inventario.repository.MaterialRepository;

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
 * Test class for the MaterialResource REST controller.
 *
 * @see MaterialResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InventarioApp.class)
@WebAppConfiguration
@IntegrationTest
public class MaterialResourceIntTest {

    private static final String DEFAULT_CODIGO = "AAAAA";
    private static final String UPDATED_CODIGO = "BBBBB";
    private static final String DEFAULT_DESCRIPCION = "AAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBB";
    private static final String DEFAULT_CODIGO_FABRICA = "AAAAA";
    private static final String UPDATED_CODIGO_FABRICA = "BBBBB";

    private static final LocalDate DEFAULT_FECHA_ALTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ALTA = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_BAJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_BAJA = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_MODIFICAR = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_MODIFICAR = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private MaterialRepository materialRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMaterialMockMvc;

    private Material material;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MaterialResource materialResource = new MaterialResource();
        ReflectionTestUtils.setField(materialResource, "materialRepository", materialRepository);
        this.restMaterialMockMvc = MockMvcBuilders.standaloneSetup(materialResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        material = new Material();
        material.setCodigo(DEFAULT_CODIGO);
        material.setDescripcion(DEFAULT_DESCRIPCION);
        material.setCodigoFabrica(DEFAULT_CODIGO_FABRICA);
        material.setFechaAlta(DEFAULT_FECHA_ALTA);
        material.setFechaBaja(DEFAULT_FECHA_BAJA);
        material.setFechaModificar(DEFAULT_FECHA_MODIFICAR);
    }

    @Test
    @Transactional
    public void createMaterial() throws Exception {
        int databaseSizeBeforeCreate = materialRepository.findAll().size();

        // Create the Material

        restMaterialMockMvc.perform(post("/api/materials")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(material)))
                .andExpect(status().isCreated());

        // Validate the Material in the database
        List<Material> materials = materialRepository.findAll();
        assertThat(materials).hasSize(databaseSizeBeforeCreate + 1);
        Material testMaterial = materials.get(materials.size() - 1);
        assertThat(testMaterial.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testMaterial.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testMaterial.getCodigoFabrica()).isEqualTo(DEFAULT_CODIGO_FABRICA);
        assertThat(testMaterial.getFechaAlta()).isEqualTo(DEFAULT_FECHA_ALTA);
        assertThat(testMaterial.getFechaBaja()).isEqualTo(DEFAULT_FECHA_BAJA);
        assertThat(testMaterial.getFechaModificar()).isEqualTo(DEFAULT_FECHA_MODIFICAR);
    }

    @Test
    @Transactional
    public void checkFechaAltaIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialRepository.findAll().size();
        // set the field null
        material.setFechaAlta(null);

        // Create the Material, which fails.

        restMaterialMockMvc.perform(post("/api/materials")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(material)))
                .andExpect(status().isBadRequest());

        List<Material> materials = materialRepository.findAll();
        assertThat(materials).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMaterials() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materials
        restMaterialMockMvc.perform(get("/api/materials?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(material.getId().intValue())))
                .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())))
                .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
                .andExpect(jsonPath("$.[*].codigoFabrica").value(hasItem(DEFAULT_CODIGO_FABRICA.toString())))
                .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
                .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())))
                .andExpect(jsonPath("$.[*].fechaModificar").value(hasItem(DEFAULT_FECHA_MODIFICAR.toString())));
    }

    @Test
    @Transactional
    public void getMaterial() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get the material
        restMaterialMockMvc.perform(get("/api/materials/{id}", material.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(material.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.codigoFabrica").value(DEFAULT_CODIGO_FABRICA.toString()))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()))
            .andExpect(jsonPath("$.fechaModificar").value(DEFAULT_FECHA_MODIFICAR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMaterial() throws Exception {
        // Get the material
        restMaterialMockMvc.perform(get("/api/materials/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMaterial() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);
        int databaseSizeBeforeUpdate = materialRepository.findAll().size();

        // Update the material
        Material updatedMaterial = new Material();
        updatedMaterial.setId(material.getId());
        updatedMaterial.setCodigo(UPDATED_CODIGO);
        updatedMaterial.setDescripcion(UPDATED_DESCRIPCION);
        updatedMaterial.setCodigoFabrica(UPDATED_CODIGO_FABRICA);
        updatedMaterial.setFechaAlta(UPDATED_FECHA_ALTA);
        updatedMaterial.setFechaBaja(UPDATED_FECHA_BAJA);
        updatedMaterial.setFechaModificar(UPDATED_FECHA_MODIFICAR);

        restMaterialMockMvc.perform(put("/api/materials")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMaterial)))
                .andExpect(status().isOk());

        // Validate the Material in the database
        List<Material> materials = materialRepository.findAll();
        assertThat(materials).hasSize(databaseSizeBeforeUpdate);
        Material testMaterial = materials.get(materials.size() - 1);
        assertThat(testMaterial.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testMaterial.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testMaterial.getCodigoFabrica()).isEqualTo(UPDATED_CODIGO_FABRICA);
        assertThat(testMaterial.getFechaAlta()).isEqualTo(UPDATED_FECHA_ALTA);
        assertThat(testMaterial.getFechaBaja()).isEqualTo(UPDATED_FECHA_BAJA);
        assertThat(testMaterial.getFechaModificar()).isEqualTo(UPDATED_FECHA_MODIFICAR);
    }

    @Test
    @Transactional
    public void deleteMaterial() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);
        int databaseSizeBeforeDelete = materialRepository.findAll().size();

        // Get the material
        restMaterialMockMvc.perform(delete("/api/materials/{id}", material.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Material> materials = materialRepository.findAll();
        assertThat(materials).hasSize(databaseSizeBeforeDelete - 1);
    }
}
