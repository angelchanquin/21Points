package com.jhipster.health.web.rest;

import com.jhipster.health.Application;

import com.jhipster.health.domain.BloodPressure;
import com.jhipster.health.repository.BloodPressureRepository;
import com.jhipster.health.repository.BloodPressureRepository;
import com.jhipster.health.repository.search.BloodPressureSearchRepository;
import com.jhipster.health.repository.search.BloodPressureSearchRepository;
import com.jhipster.health.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BloodPressureResource REST controller.
 *
 * @see BloodPressureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BloodPressureResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_SYSTOLIC = "AAAAAAAAAA";
    private static final String UPDATED_SYSTOLIC = "BBBBBBBBBB";

    private static final String DEFAULT_DIASTOLIC = "AAAAAAAAAA";
    private static final String UPDATED_DIASTOLIC = "BBBBBBBBBB";

    @Autowired
    private BloodPressureRepository bloodPressureRepository;

    @Autowired
    private BloodPressureSearchRepository bloodPressureSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBloodPresureMockMvc;

    private BloodPressure BloodPresure;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BloodPressureResource BloodPresureResource = new BloodPressureResource(bloodPressureRepository, bloodPressureSearchRepository);
        this.restBloodPresureMockMvc = MockMvcBuilders.standaloneSetup(BloodPresureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BloodPressure createEntity(EntityManager em) {
        BloodPressure BloodPresure = new BloodPressure()
            .date(DEFAULT_DATE)
            .systolic(DEFAULT_SYSTOLIC)
            .diastolic(DEFAULT_DIASTOLIC);
        return BloodPresure;
    }

    @Before
    public void initTest() {
        bloodPressureSearchRepository.deleteAll();
        BloodPresure = createEntity(em);
    }

    @Test
    @Transactional
    public void createBloodPresure() throws Exception {
        int databaseSizeBeforeCreate = bloodPressureRepository.findAll().size();

        // Create the BloodPressure
        restBloodPresureMockMvc.perform(post("/api/blood-pressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(BloodPresure)))
            .andExpect(status().isCreated());

        // Validate the BloodPressure in the database
        List<BloodPressure> BloodPresureList = bloodPressureRepository.findAll();
        assertThat(BloodPresureList).hasSize(databaseSizeBeforeCreate + 1);
        BloodPressure testBloodPresure = BloodPresureList.get(BloodPresureList.size() - 1);
        assertThat(testBloodPresure.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testBloodPresure.getSystolic()).isEqualTo(DEFAULT_SYSTOLIC);
        assertThat(testBloodPresure.getDiastolic()).isEqualTo(DEFAULT_DIASTOLIC);

        // Validate the BloodPressure in Elasticsearch
        BloodPressure BloodPresureEs = bloodPressureSearchRepository.findOne(testBloodPresure.getId());
        assertThat(BloodPresureEs).isEqualToComparingFieldByField(testBloodPresure);
    }

    @Test
    @Transactional
    public void createBloodPresureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bloodPressureRepository.findAll().size();

        // Create the BloodPressure with an existing ID
        BloodPresure.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBloodPresureMockMvc.perform(post("/api/blood-pressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(BloodPresure)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<BloodPressure> BloodPresureList = bloodPressureRepository.findAll();
        assertThat(BloodPresureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bloodPressureRepository.findAll().size();
        // set the field null
        BloodPresure.setDate(null);

        // Create the BloodPressure, which fails.

        restBloodPresureMockMvc.perform(post("/api/blood-pressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(BloodPresure)))
            .andExpect(status().isBadRequest());

        List<BloodPressure> BloodPresureList = bloodPressureRepository.findAll();
        assertThat(BloodPresureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSystolicIsRequired() throws Exception {
        int databaseSizeBeforeTest = bloodPressureRepository.findAll().size();
        // set the field null
        BloodPresure.setSystolic(null);

        // Create the BloodPressure, which fails.

        restBloodPresureMockMvc.perform(post("/api/blood-pressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(BloodPresure)))
            .andExpect(status().isBadRequest());

        List<BloodPressure> BloodPresureList = bloodPressureRepository.findAll();
        assertThat(BloodPresureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDiastolicIsRequired() throws Exception {
        int databaseSizeBeforeTest = bloodPressureRepository.findAll().size();
        // set the field null
        BloodPresure.setDiastolic(null);

        // Create the BloodPressure, which fails.

        restBloodPresureMockMvc.perform(post("/api/blood-pressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(BloodPresure)))
            .andExpect(status().isBadRequest());

        List<BloodPressure> BloodPresureList = bloodPressureRepository.findAll();
        assertThat(BloodPresureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBloodPresures() throws Exception {
        // Initialize the database
        bloodPressureRepository.saveAndFlush(BloodPresure);

        // Get all the BloodPresureList
        restBloodPresureMockMvc.perform(get("/api/blood-pressures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(BloodPresure.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].systolic").value(hasItem(DEFAULT_SYSTOLIC.toString())))
            .andExpect(jsonPath("$.[*].diastolic").value(hasItem(DEFAULT_DIASTOLIC.toString())));
    }

    @Test
    @Transactional
    public void getBloodPresure() throws Exception {
        // Initialize the database
        bloodPressureRepository.saveAndFlush(BloodPresure);

        // Get the BloodPresure
        restBloodPresureMockMvc.perform(get("/api/blood-pressures/{id}", BloodPresure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(BloodPresure.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.systolic").value(DEFAULT_SYSTOLIC.toString()))
            .andExpect(jsonPath("$.diastolic").value(DEFAULT_DIASTOLIC.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBloodPresure() throws Exception {
        // Get the BloodPresure
        restBloodPresureMockMvc.perform(get("/api/blood-pressures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBloodPresure() throws Exception {
        // Initialize the database
        bloodPressureRepository.saveAndFlush(BloodPresure);
        bloodPressureSearchRepository.save(BloodPresure);
        int databaseSizeBeforeUpdate = bloodPressureRepository.findAll().size();

        // Update the BloodPresure
        BloodPressure updatedBloodPresure = bloodPressureRepository.findOne(BloodPresure.getId());
        updatedBloodPresure
            .date(UPDATED_DATE)
            .systolic(UPDATED_SYSTOLIC)
            .diastolic(UPDATED_DIASTOLIC);

        restBloodPresureMockMvc.perform(put("/api/blood-pressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBloodPresure)))
            .andExpect(status().isOk());

        // Validate the BloodPressure in the database
        List<BloodPressure> BloodPresureList = bloodPressureRepository.findAll();
        assertThat(BloodPresureList).hasSize(databaseSizeBeforeUpdate);
        BloodPressure testBloodPresure = BloodPresureList.get(BloodPresureList.size() - 1);
        assertThat(testBloodPresure.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBloodPresure.getSystolic()).isEqualTo(UPDATED_SYSTOLIC);
        assertThat(testBloodPresure.getDiastolic()).isEqualTo(UPDATED_DIASTOLIC);

        // Validate the BloodPressure in Elasticsearch
        BloodPressure BloodPresureEs = bloodPressureSearchRepository.findOne(testBloodPresure.getId());
        assertThat(BloodPresureEs).isEqualToComparingFieldByField(testBloodPresure);
    }

    @Test
    @Transactional
    public void updateNonExistingBloodPresure() throws Exception {
        int databaseSizeBeforeUpdate = bloodPressureRepository.findAll().size();

        // Create the BloodPressure

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBloodPresureMockMvc.perform(put("/api/blood-pressures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(BloodPresure)))
            .andExpect(status().isCreated());

        // Validate the BloodPressure in the database
        List<BloodPressure> BloodPresureList = bloodPressureRepository.findAll();
        assertThat(BloodPresureList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBloodPresure() throws Exception {
        // Initialize the database
        bloodPressureRepository.saveAndFlush(BloodPresure);
        bloodPressureSearchRepository.save(BloodPresure);
        int databaseSizeBeforeDelete = bloodPressureRepository.findAll().size();

        // Get the BloodPresure
        restBloodPresureMockMvc.perform(delete("/api/blood-pressures/{id}", BloodPresure.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean BloodPresureExistsInEs = bloodPressureSearchRepository.exists(BloodPresure.getId());
        assertThat(BloodPresureExistsInEs).isFalse();

        // Validate the database is empty
        List<BloodPressure> BloodPresureList = bloodPressureRepository.findAll();
        assertThat(BloodPresureList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBloodPresure() throws Exception {
        // Initialize the database
        bloodPressureRepository.saveAndFlush(BloodPresure);
        bloodPressureSearchRepository.save(BloodPresure);

        // Search the BloodPresure
        restBloodPresureMockMvc.perform(get("/api/_search/blood-pressures?query=id:" + BloodPresure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(BloodPresure.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].systolic").value(hasItem(DEFAULT_SYSTOLIC.toString())))
            .andExpect(jsonPath("$.[*].diastolic").value(hasItem(DEFAULT_DIASTOLIC.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BloodPressure.class);
    }
}
