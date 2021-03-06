package cm.iconprod.iconlab.web.rest;

import cm.iconprod.iconlab.IconlabApp;
import cm.iconprod.iconlab.domain.Tache;
import cm.iconprod.iconlab.repository.TacheRepository;
import cm.iconprod.iconlab.repository.search.TacheSearchRepository;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cm.iconprod.iconlab.domain.enumeration.Role;

/**
 * Test class for the TacheResource REST controller.
 *
 * @see TacheResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IconlabApp.class)
@WebAppConfiguration
@IntegrationTest
public class TacheResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final byte[] DEFAULT_FICHIER_JOINT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FICHIER_JOINT = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FICHIER_JOINT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FICHIER_JOINT_CONTENT_TYPE = "image/png";

    private static final Role DEFAULT_ROLE = Role.GRH;
    private static final Role UPDATED_ROLE = Role.FINANCE;

    private static final ZonedDateTime DEFAULT_FROMT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FROMT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FROMT_STR = dateTimeFormatter.format(DEFAULT_FROMT);

    private static final ZonedDateTime DEFAULT_TOT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TOT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TOT_STR = dateTimeFormatter.format(DEFAULT_TOT);

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;
    private static final String DEFAULT_COLOR = "AAAAA";
    private static final String UPDATED_COLOR = "BBBBB";
    private static final String DEFAULT_DATA = "AAAAA";
    private static final String UPDATED_DATA = "BBBBB";

    private static final Boolean DEFAULT_MOVABLE = false;
    private static final Boolean UPDATED_MOVABLE = true;

    private static final Integer DEFAULT_PROGRESS = 1;
    private static final Integer UPDATED_PROGRESS = 2;

    private static final LocalDate DEFAULT_LCT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LCT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EST = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EST = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private TacheRepository tacheRepository;

    @Inject
    private TacheSearchRepository tacheSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTacheMockMvc;

    private Tache tache;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TacheResource tacheResource = new TacheResource();
        ReflectionTestUtils.setField(tacheResource, "tacheSearchRepository", tacheSearchRepository);
        ReflectionTestUtils.setField(tacheResource, "tacheRepository", tacheRepository);
        this.restTacheMockMvc = MockMvcBuilders.standaloneSetup(tacheResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tacheSearchRepository.deleteAll();
        tache = new Tache();
        tache.setName(DEFAULT_NAME);
        tache.setDescription(DEFAULT_DESCRIPTION);
        tache.setFichierJoint(DEFAULT_FICHIER_JOINT);
        tache.setFichierJointContentType(DEFAULT_FICHIER_JOINT_CONTENT_TYPE);
        tache.setRole(DEFAULT_ROLE);
        tache.setFromt(DEFAULT_FROMT);
        tache.setTot(DEFAULT_TOT);
        tache.setActif(DEFAULT_ACTIF);
        tache.setColor(DEFAULT_COLOR);
        tache.setData(DEFAULT_DATA);
        tache.setMovable(DEFAULT_MOVABLE);
        tache.setProgress(DEFAULT_PROGRESS);
        tache.setLct(DEFAULT_LCT);
        tache.setEst(DEFAULT_EST);
    }

    @Test
    @Transactional
    public void createTache() throws Exception {
        int databaseSizeBeforeCreate = tacheRepository.findAll().size();

        // Create the Tache

        restTacheMockMvc.perform(post("/api/taches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tache)))
            .andExpect(status().isCreated());

        // Validate the Tache in the database
        List<Tache> taches = tacheRepository.findAll();
        assertThat(taches).hasSize(databaseSizeBeforeCreate + 1);
        Tache testTache = taches.get(taches.size() - 1);
        assertThat(testTache.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTache.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTache.getFichierJoint()).isEqualTo(DEFAULT_FICHIER_JOINT);
        assertThat(testTache.getFichierJointContentType()).isEqualTo(DEFAULT_FICHIER_JOINT_CONTENT_TYPE);
        assertThat(testTache.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testTache.getFromt()).isEqualTo(DEFAULT_FROMT);
        assertThat(testTache.getTot()).isEqualTo(DEFAULT_TOT);
        assertThat(testTache.isActif()).isEqualTo(DEFAULT_ACTIF);
        assertThat(testTache.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testTache.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testTache.isMovable()).isEqualTo(DEFAULT_MOVABLE);
        assertThat(testTache.getProgress()).isEqualTo(DEFAULT_PROGRESS);
        assertThat(testTache.getLct()).isEqualTo(DEFAULT_LCT);
        assertThat(testTache.getEst()).isEqualTo(DEFAULT_EST);

        // Validate the Tache in ElasticSearch
        Tache tacheEs = tacheSearchRepository.findOne(testTache.getId());
        assertThat(tacheEs).isEqualToComparingFieldByField(testTache);
    }

    @Test
    @Transactional
    public void getAllTaches() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);

        // Get all the taches
        restTacheMockMvc.perform(get("/api/taches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tache.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].fichierJointContentType").value(hasItem(DEFAULT_FICHIER_JOINT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichierJoint").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER_JOINT))))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].fromt").value(hasItem(DEFAULT_FROMT_STR)))
            .andExpect(jsonPath("$.[*].tot").value(hasItem(DEFAULT_TOT_STR)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
            .andExpect(jsonPath("$.[*].movable").value(hasItem(DEFAULT_MOVABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].progress").value(hasItem(DEFAULT_PROGRESS)))
            .andExpect(jsonPath("$.[*].lct").value(hasItem(DEFAULT_LCT.toString())))
            .andExpect(jsonPath("$.[*].est").value(hasItem(DEFAULT_EST.toString())));
    }

    @Test
    @Transactional
    public void getTache() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);

        // Get the tache
        restTacheMockMvc.perform(get("/api/taches/{id}", tache.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tache.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.fichierJointContentType").value(DEFAULT_FICHIER_JOINT_CONTENT_TYPE))
            .andExpect(jsonPath("$.fichierJoint").value(Base64Utils.encodeToString(DEFAULT_FICHIER_JOINT)))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.fromt").value(DEFAULT_FROMT_STR))
            .andExpect(jsonPath("$.tot").value(DEFAULT_TOT_STR))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF.booleanValue()))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR.toString()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()))
            .andExpect(jsonPath("$.movable").value(DEFAULT_MOVABLE.booleanValue()))
            .andExpect(jsonPath("$.progress").value(DEFAULT_PROGRESS))
            .andExpect(jsonPath("$.lct").value(DEFAULT_LCT.toString()))
            .andExpect(jsonPath("$.est").value(DEFAULT_EST.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTache() throws Exception {
        // Get the tache
        restTacheMockMvc.perform(get("/api/taches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTache() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);
        tacheSearchRepository.save(tache);
        int databaseSizeBeforeUpdate = tacheRepository.findAll().size();

        // Update the tache
        Tache updatedTache = new Tache();
        updatedTache.setId(tache.getId());
        updatedTache.setName(UPDATED_NAME);
        updatedTache.setDescription(UPDATED_DESCRIPTION);
        updatedTache.setFichierJoint(UPDATED_FICHIER_JOINT);
        updatedTache.setFichierJointContentType(UPDATED_FICHIER_JOINT_CONTENT_TYPE);
        updatedTache.setRole(UPDATED_ROLE);
        updatedTache.setFromt(UPDATED_FROMT);
        updatedTache.setTot(UPDATED_TOT);
        updatedTache.setActif(UPDATED_ACTIF);
        updatedTache.setColor(UPDATED_COLOR);
        updatedTache.setData(UPDATED_DATA);
        updatedTache.setMovable(UPDATED_MOVABLE);
        updatedTache.setProgress(UPDATED_PROGRESS);
        updatedTache.setLct(UPDATED_LCT);
        updatedTache.setEst(UPDATED_EST);

        restTacheMockMvc.perform(put("/api/taches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTache)))
            .andExpect(status().isOk());

        // Validate the Tache in the database
        List<Tache> taches = tacheRepository.findAll();
        assertThat(taches).hasSize(databaseSizeBeforeUpdate);
        Tache testTache = taches.get(taches.size() - 1);
        assertThat(testTache.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTache.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTache.getFichierJoint()).isEqualTo(UPDATED_FICHIER_JOINT);
        assertThat(testTache.getFichierJointContentType()).isEqualTo(UPDATED_FICHIER_JOINT_CONTENT_TYPE);
        assertThat(testTache.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testTache.getFromt()).isEqualTo(UPDATED_FROMT);
        assertThat(testTache.getTot()).isEqualTo(UPDATED_TOT);
        assertThat(testTache.isActif()).isEqualTo(UPDATED_ACTIF);
        assertThat(testTache.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testTache.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testTache.isMovable()).isEqualTo(UPDATED_MOVABLE);
        assertThat(testTache.getProgress()).isEqualTo(UPDATED_PROGRESS);
        assertThat(testTache.getLct()).isEqualTo(UPDATED_LCT);
        assertThat(testTache.getEst()).isEqualTo(UPDATED_EST);

        // Validate the Tache in ElasticSearch
        Tache tacheEs = tacheSearchRepository.findOne(testTache.getId());
        assertThat(tacheEs).isEqualToComparingFieldByField(testTache);
    }

    @Test
    @Transactional
    public void deleteTache() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);
        tacheSearchRepository.save(tache);
        int databaseSizeBeforeDelete = tacheRepository.findAll().size();

        // Get the tache
        restTacheMockMvc.perform(delete("/api/taches/{id}", tache.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean tacheExistsInEs = tacheSearchRepository.exists(tache.getId());
        assertThat(tacheExistsInEs).isFalse();

        // Validate the database is empty
        List<Tache> taches = tacheRepository.findAll();
        assertThat(taches).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTache() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);
        tacheSearchRepository.save(tache);

        // Search the tache
        restTacheMockMvc.perform(get("/api/_search/taches?query=id:" + tache.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tache.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].fichierJointContentType").value(hasItem(DEFAULT_FICHIER_JOINT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichierJoint").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER_JOINT))))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].fromt").value(hasItem(DEFAULT_FROMT_STR)))
            .andExpect(jsonPath("$.[*].tot").value(hasItem(DEFAULT_TOT_STR)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
            .andExpect(jsonPath("$.[*].movable").value(hasItem(DEFAULT_MOVABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].progress").value(hasItem(DEFAULT_PROGRESS)))
            .andExpect(jsonPath("$.[*].lct").value(hasItem(DEFAULT_LCT.toString())))
            .andExpect(jsonPath("$.[*].est").value(hasItem(DEFAULT_EST.toString())));
    }

/*
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NOM = "AAAAA";
    private static final String UPDATED_NOM = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final byte[] DEFAULT_FICHIER_JOINT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FICHIER_JOINT = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FICHIER_JOINT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FICHIER_JOINT_CONTENT_TYPE = "image/png";

    private static final Role DEFAULT_ROLE = Role.GRH;
    private static final Role UPDATED_ROLE = Role.FINANCE;

    private static final ZonedDateTime DEFAULT_DATE_DEBUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_DEBUT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_DEBUT_STR = dateTimeFormatter.format(DEFAULT_DATE_DEBUT);

    private static final ZonedDateTime DEFAULT_DATE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_FIN_STR = dateTimeFormatter.format(DEFAULT_DATE_FIN);

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    @Inject
    private TacheRepository tacheRepository;

    @Inject
    private TacheSearchRepository tacheSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTacheMockMvc;

    private Tache tache;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TacheResource tacheResource = new TacheResource();
        ReflectionTestUtils.setField(tacheResource, "tacheSearchRepository", tacheSearchRepository);
        ReflectionTestUtils.setField(tacheResource, "tacheRepository", tacheRepository);
        this.restTacheMockMvc = MockMvcBuilders.standaloneSetup(tacheResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tacheSearchRepository.deleteAll();
        tache = new Tache();
        tache.setNom(DEFAULT_NOM);
        tache.setDescription(DEFAULT_DESCRIPTION);
        tache.setFichierJoint(DEFAULT_FICHIER_JOINT);
        tache.setFichierJointContentType(DEFAULT_FICHIER_JOINT_CONTENT_TYPE);
        tache.setRole(DEFAULT_ROLE);
        tache.setDateDebut(DEFAULT_DATE_DEBUT);
        tache.setDateFin(DEFAULT_DATE_FIN);
        tache.setActif(DEFAULT_ACTIF);
    }

    @Test
    @Transactional
    public void createTache() throws Exception {
        int databaseSizeBeforeCreate = tacheRepository.findAll().size();

        // Create the Tache

        restTacheMockMvc.perform(post("/api/taches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tache)))
                .andExpect(status().isCreated());

        // Validate the Tache in the database
        List<Tache> taches = tacheRepository.findAll();
        assertThat(taches).hasSize(databaseSizeBeforeCreate + 1);
        Tache testTache = taches.get(taches.size() - 1);
        assertThat(testTache.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testTache.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTache.getFichierJoint()).isEqualTo(DEFAULT_FICHIER_JOINT);
        assertThat(testTache.getFichierJointContentType()).isEqualTo(DEFAULT_FICHIER_JOINT_CONTENT_TYPE);
        assertThat(testTache.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testTache.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testTache.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testTache.isActif()).isEqualTo(DEFAULT_ACTIF);

        // Validate the Tache in ElasticSearch
        Tache tacheEs = tacheSearchRepository.findOne(testTache.getId());
        assertThat(tacheEs).isEqualToComparingFieldByField(testTache);
    }

    @Test
    @Transactional
    public void getAllTaches() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);

        // Get all the taches
        restTacheMockMvc.perform(get("/api/taches?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tache.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].fichierJointContentType").value(hasItem(DEFAULT_FICHIER_JOINT_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].fichierJoint").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER_JOINT))))
                .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
                .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT_STR)))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN_STR)))
                .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())));
    }

    @Test
    @Transactional
    public void getTache() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);

        // Get the tache
        restTacheMockMvc.perform(get("/api/taches/{id}", tache.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tache.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.fichierJointContentType").value(DEFAULT_FICHIER_JOINT_CONTENT_TYPE))
            .andExpect(jsonPath("$.fichierJoint").value(Base64Utils.encodeToString(DEFAULT_FICHIER_JOINT)))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT_STR))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN_STR))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTache() throws Exception {
        // Get the tache
        restTacheMockMvc.perform(get("/api/taches/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTache() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);
        tacheSearchRepository.save(tache);
        int databaseSizeBeforeUpdate = tacheRepository.findAll().size();

        // Update the tache
        Tache updatedTache = new Tache();
        updatedTache.setId(tache.getId());
        updatedTache.setNom(UPDATED_NOM);
        updatedTache.setDescription(UPDATED_DESCRIPTION);
        updatedTache.setFichierJoint(UPDATED_FICHIER_JOINT);
        updatedTache.setFichierJointContentType(UPDATED_FICHIER_JOINT_CONTENT_TYPE);
        updatedTache.setRole(UPDATED_ROLE);
        updatedTache.setDateDebut(UPDATED_DATE_DEBUT);
        updatedTache.setDateFin(UPDATED_DATE_FIN);
        updatedTache.setActif(UPDATED_ACTIF);

        restTacheMockMvc.perform(put("/api/taches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTache)))
                .andExpect(status().isOk());

        // Validate the Tache in the database
        List<Tache> taches = tacheRepository.findAll();
        assertThat(taches).hasSize(databaseSizeBeforeUpdate);
        Tache testTache = taches.get(taches.size() - 1);
        assertThat(testTache.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testTache.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTache.getFichierJoint()).isEqualTo(UPDATED_FICHIER_JOINT);
        assertThat(testTache.getFichierJointContentType()).isEqualTo(UPDATED_FICHIER_JOINT_CONTENT_TYPE);
        assertThat(testTache.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testTache.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testTache.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testTache.isActif()).isEqualTo(UPDATED_ACTIF);

        // Validate the Tache in ElasticSearch
        Tache tacheEs = tacheSearchRepository.findOne(testTache.getId());
        assertThat(tacheEs).isEqualToComparingFieldByField(testTache);
    }

    @Test
    @Transactional
    public void deleteTache() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);
        tacheSearchRepository.save(tache);
        int databaseSizeBeforeDelete = tacheRepository.findAll().size();

        // Get the tache
        restTacheMockMvc.perform(delete("/api/taches/{id}", tache.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean tacheExistsInEs = tacheSearchRepository.exists(tache.getId());
        assertThat(tacheExistsInEs).isFalse();

        // Validate the database is empty
        List<Tache> taches = tacheRepository.findAll();
        assertThat(taches).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTache() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);
        tacheSearchRepository.save(tache);

        // Search the tache
        restTacheMockMvc.perform(get("/api/_search/taches?query=id:" + tache.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tache.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].fichierJointContentType").value(hasItem(DEFAULT_FICHIER_JOINT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichierJoint").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER_JOINT))))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT_STR)))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN_STR)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())));
    }*/
}
