package org.gerald.inventario.web.rest;

import org.gerald.inventario.InventarioApp;
import org.gerald.inventario.domain.Empleado;
import org.gerald.inventario.repository.EmpleadoRepository;

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
 * Test class for the EmpleadoResource REST controller.
 *
 * @see EmpleadoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = InventarioApp.class)
@WebAppConfiguration
@IntegrationTest
public class EmpleadoResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";
    private static final String DEFAULT_APELLIDO_PATERNO = "AAAAA";
    private static final String UPDATED_APELLIDO_PATERNO = "BBBBB";
    private static final String DEFAULT_APELLIDO_MATERNO = "AAAAA";
    private static final String UPDATED_APELLIDO_MATERNO = "BBBBB";
    private static final String DEFAULT_SEXO = "AAAAA";
    private static final String UPDATED_SEXO = "BBBBB";
    private static final String DEFAULT_NACIONALIDAD = "AAAAA";
    private static final String UPDATED_NACIONALIDAD = "BBBBB";
    private static final String DEFAULT_PROFESION = "AAAAA";
    private static final String UPDATED_PROFESION = "BBBBB";

    private static final LocalDate DEFAULT_FECHA_ALTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ALTA = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_BAJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_BAJA = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_CI = 1;
    private static final Integer UPDATED_CI = 2;

    @Inject
    private EmpleadoRepository empleadoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEmpleadoMockMvc;

    private Empleado empleado;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmpleadoResource empleadoResource = new EmpleadoResource();
        ReflectionTestUtils.setField(empleadoResource, "empleadoRepository", empleadoRepository);
        this.restEmpleadoMockMvc = MockMvcBuilders.standaloneSetup(empleadoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        empleado = new Empleado();
        empleado.setNombre(DEFAULT_NOMBRE);
        empleado.setApellidoPaterno(DEFAULT_APELLIDO_PATERNO);
        empleado.setApellidoMaterno(DEFAULT_APELLIDO_MATERNO);
        empleado.setSexo(DEFAULT_SEXO);
        empleado.setNacionalidad(DEFAULT_NACIONALIDAD);
        empleado.setProfesion(DEFAULT_PROFESION);
        empleado.setFechaAlta(DEFAULT_FECHA_ALTA);
        empleado.setFechaBaja(DEFAULT_FECHA_BAJA);
        empleado.setCi(DEFAULT_CI);
    }

    @Test
    @Transactional
    public void createEmpleado() throws Exception {
        int databaseSizeBeforeCreate = empleadoRepository.findAll().size();

        // Create the Empleado

        restEmpleadoMockMvc.perform(post("/api/empleados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(empleado)))
                .andExpect(status().isCreated());

        // Validate the Empleado in the database
        List<Empleado> empleados = empleadoRepository.findAll();
        assertThat(empleados).hasSize(databaseSizeBeforeCreate + 1);
        Empleado testEmpleado = empleados.get(empleados.size() - 1);
        assertThat(testEmpleado.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEmpleado.getApellidoPaterno()).isEqualTo(DEFAULT_APELLIDO_PATERNO);
        assertThat(testEmpleado.getApellidoMaterno()).isEqualTo(DEFAULT_APELLIDO_MATERNO);
        assertThat(testEmpleado.getSexo()).isEqualTo(DEFAULT_SEXO);
        assertThat(testEmpleado.getNacionalidad()).isEqualTo(DEFAULT_NACIONALIDAD);
        assertThat(testEmpleado.getProfesion()).isEqualTo(DEFAULT_PROFESION);
        assertThat(testEmpleado.getFechaAlta()).isEqualTo(DEFAULT_FECHA_ALTA);
        assertThat(testEmpleado.getFechaBaja()).isEqualTo(DEFAULT_FECHA_BAJA);
        assertThat(testEmpleado.getCi()).isEqualTo(DEFAULT_CI);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = empleadoRepository.findAll().size();
        // set the field null
        empleado.setNombre(null);

        // Create the Empleado, which fails.

        restEmpleadoMockMvc.perform(post("/api/empleados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(empleado)))
                .andExpect(status().isBadRequest());

        List<Empleado> empleados = empleadoRepository.findAll();
        assertThat(empleados).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmpleados() throws Exception {
        // Initialize the database
        empleadoRepository.saveAndFlush(empleado);

        // Get all the empleados
        restEmpleadoMockMvc.perform(get("/api/empleados?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(empleado.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].apellidoPaterno").value(hasItem(DEFAULT_APELLIDO_PATERNO.toString())))
                .andExpect(jsonPath("$.[*].apellidoMaterno").value(hasItem(DEFAULT_APELLIDO_MATERNO.toString())))
                .andExpect(jsonPath("$.[*].sexo").value(hasItem(DEFAULT_SEXO.toString())))
                .andExpect(jsonPath("$.[*].nacionalidad").value(hasItem(DEFAULT_NACIONALIDAD.toString())))
                .andExpect(jsonPath("$.[*].profesion").value(hasItem(DEFAULT_PROFESION.toString())))
                .andExpect(jsonPath("$.[*].fechaAlta").value(hasItem(DEFAULT_FECHA_ALTA.toString())))
                .andExpect(jsonPath("$.[*].fechaBaja").value(hasItem(DEFAULT_FECHA_BAJA.toString())))
                .andExpect(jsonPath("$.[*].ci").value(hasItem(DEFAULT_CI)));
    }

    @Test
    @Transactional
    public void getEmpleado() throws Exception {
        // Initialize the database
        empleadoRepository.saveAndFlush(empleado);

        // Get the empleado
        restEmpleadoMockMvc.perform(get("/api/empleados/{id}", empleado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(empleado.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.apellidoPaterno").value(DEFAULT_APELLIDO_PATERNO.toString()))
            .andExpect(jsonPath("$.apellidoMaterno").value(DEFAULT_APELLIDO_MATERNO.toString()))
            .andExpect(jsonPath("$.sexo").value(DEFAULT_SEXO.toString()))
            .andExpect(jsonPath("$.nacionalidad").value(DEFAULT_NACIONALIDAD.toString()))
            .andExpect(jsonPath("$.profesion").value(DEFAULT_PROFESION.toString()))
            .andExpect(jsonPath("$.fechaAlta").value(DEFAULT_FECHA_ALTA.toString()))
            .andExpect(jsonPath("$.fechaBaja").value(DEFAULT_FECHA_BAJA.toString()))
            .andExpect(jsonPath("$.ci").value(DEFAULT_CI));
    }

    @Test
    @Transactional
    public void getNonExistingEmpleado() throws Exception {
        // Get the empleado
        restEmpleadoMockMvc.perform(get("/api/empleados/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmpleado() throws Exception {
        // Initialize the database
        empleadoRepository.saveAndFlush(empleado);
        int databaseSizeBeforeUpdate = empleadoRepository.findAll().size();

        // Update the empleado
        Empleado updatedEmpleado = new Empleado();
        updatedEmpleado.setId(empleado.getId());
        updatedEmpleado.setNombre(UPDATED_NOMBRE);
        updatedEmpleado.setApellidoPaterno(UPDATED_APELLIDO_PATERNO);
        updatedEmpleado.setApellidoMaterno(UPDATED_APELLIDO_MATERNO);
        updatedEmpleado.setSexo(UPDATED_SEXO);
        updatedEmpleado.setNacionalidad(UPDATED_NACIONALIDAD);
        updatedEmpleado.setProfesion(UPDATED_PROFESION);
        updatedEmpleado.setFechaAlta(UPDATED_FECHA_ALTA);
        updatedEmpleado.setFechaBaja(UPDATED_FECHA_BAJA);
        updatedEmpleado.setCi(UPDATED_CI);

        restEmpleadoMockMvc.perform(put("/api/empleados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEmpleado)))
                .andExpect(status().isOk());

        // Validate the Empleado in the database
        List<Empleado> empleados = empleadoRepository.findAll();
        assertThat(empleados).hasSize(databaseSizeBeforeUpdate);
        Empleado testEmpleado = empleados.get(empleados.size() - 1);
        assertThat(testEmpleado.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEmpleado.getApellidoPaterno()).isEqualTo(UPDATED_APELLIDO_PATERNO);
        assertThat(testEmpleado.getApellidoMaterno()).isEqualTo(UPDATED_APELLIDO_MATERNO);
        assertThat(testEmpleado.getSexo()).isEqualTo(UPDATED_SEXO);
        assertThat(testEmpleado.getNacionalidad()).isEqualTo(UPDATED_NACIONALIDAD);
        assertThat(testEmpleado.getProfesion()).isEqualTo(UPDATED_PROFESION);
        assertThat(testEmpleado.getFechaAlta()).isEqualTo(UPDATED_FECHA_ALTA);
        assertThat(testEmpleado.getFechaBaja()).isEqualTo(UPDATED_FECHA_BAJA);
        assertThat(testEmpleado.getCi()).isEqualTo(UPDATED_CI);
    }

    @Test
    @Transactional
    public void deleteEmpleado() throws Exception {
        // Initialize the database
        empleadoRepository.saveAndFlush(empleado);
        int databaseSizeBeforeDelete = empleadoRepository.findAll().size();

        // Get the empleado
        restEmpleadoMockMvc.perform(delete("/api/empleados/{id}", empleado.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Empleado> empleados = empleadoRepository.findAll();
        assertThat(empleados).hasSize(databaseSizeBeforeDelete - 1);
    }
}
