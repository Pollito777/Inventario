package org.gerald.inventario.web.rest;

import org.gerald.inventario.InventarioApp;
import org.gerald.inventario.domain.Documento;
import org.gerald.inventario.repository.DocumentoRepository;

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
 * Test class for the DocumentoResource REST controller.
 *
 * @see DocumentoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InventarioApp.class)
@WebAppConfiguration
@IntegrationTest
public class DocumentoResourceIntTest {

    private static final String DEFAULT_U_RI = "AAAAA";
    private static final String UPDATED_U_RI = "BBBBB";
    private static final String DEFAULT_TIPO = "AAAAA";
    private static final String UPDATED_TIPO = "BBBBB";
    private static final String DEFAULT_DESCRIPCION = "AAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBB";

    @Inject
    private DocumentoRepository documentoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDocumentoMockMvc;

    private Documento documento;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DocumentoResource documentoResource = new DocumentoResource();
        ReflectionTestUtils.setField(documentoResource, "documentoRepository", documentoRepository);
        this.restDocumentoMockMvc = MockMvcBuilders.standaloneSetup(documentoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        documento = new Documento();
        documento.setuRi(DEFAULT_U_RI);
        documento.setTipo(DEFAULT_TIPO);
        documento.setDescripcion(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createDocumento() throws Exception {
        int databaseSizeBeforeCreate = documentoRepository.findAll().size();

        // Create the Documento

        restDocumentoMockMvc.perform(post("/api/documentos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(documento)))
                .andExpect(status().isCreated());

        // Validate the Documento in the database
        List<Documento> documentos = documentoRepository.findAll();
        assertThat(documentos).hasSize(databaseSizeBeforeCreate + 1);
        Documento testDocumento = documentos.get(documentos.size() - 1);
        assertThat(testDocumento.getuRi()).isEqualTo(DEFAULT_U_RI);
        assertThat(testDocumento.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testDocumento.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllDocumentos() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get all the documentos
        restDocumentoMockMvc.perform(get("/api/documentos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(documento.getId().intValue())))
                .andExpect(jsonPath("$.[*].uRi").value(hasItem(DEFAULT_U_RI.toString())))
                .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
                .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }

    @Test
    @Transactional
    public void getDocumento() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);

        // Get the documento
        restDocumentoMockMvc.perform(get("/api/documentos/{id}", documento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(documento.getId().intValue()))
            .andExpect(jsonPath("$.uRi").value(DEFAULT_U_RI.toString()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDocumento() throws Exception {
        // Get the documento
        restDocumentoMockMvc.perform(get("/api/documentos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocumento() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);
        int databaseSizeBeforeUpdate = documentoRepository.findAll().size();

        // Update the documento
        Documento updatedDocumento = new Documento();
        updatedDocumento.setId(documento.getId());
        updatedDocumento.setuRi(UPDATED_U_RI);
        updatedDocumento.setTipo(UPDATED_TIPO);
        updatedDocumento.setDescripcion(UPDATED_DESCRIPCION);

        restDocumentoMockMvc.perform(put("/api/documentos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDocumento)))
                .andExpect(status().isOk());

        // Validate the Documento in the database
        List<Documento> documentos = documentoRepository.findAll();
        assertThat(documentos).hasSize(databaseSizeBeforeUpdate);
        Documento testDocumento = documentos.get(documentos.size() - 1);
        assertThat(testDocumento.getuRi()).isEqualTo(UPDATED_U_RI);
        assertThat(testDocumento.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testDocumento.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void deleteDocumento() throws Exception {
        // Initialize the database
        documentoRepository.saveAndFlush(documento);
        int databaseSizeBeforeDelete = documentoRepository.findAll().size();

        // Get the documento
        restDocumentoMockMvc.perform(delete("/api/documentos/{id}", documento.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Documento> documentos = documentoRepository.findAll();
        assertThat(documentos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
