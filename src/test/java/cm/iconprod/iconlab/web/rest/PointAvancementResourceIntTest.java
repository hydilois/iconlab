package cm.iconprod.iconlab.web.rest;

import cm.iconprod.iconlab.IconlabApp;
import cm.iconprod.iconlab.domain.PointAvancement;
import cm.iconprod.iconlab.repository.PointAvancementRepository;
import cm.iconprod.iconlab.repository.search.PointAvancementSearchRepository;

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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PointAvancementResource REST controller.
 *
 * @see PointAvancementResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IconlabApp.class)
@WebAppConfiguration
@IntegrationTest
public class PointAvancementResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_LIBELLE = "AAAAA";
    private static final String UPDATED_LIBELLE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final byte[] DEFAULT_FICHIER = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FICHIER = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FICHIER_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FICHIER_CONTENT_TYPE = "image/png";

    private static final ZonedDateTime DEFAULT_DATE_PUB = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_PUB = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_PUB_STR = dateTimeFormatter.format(DEFAULT_DATE_PUB);

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    @Inject
    private PointAvancementRepository pointAvancementRepository;

    @Inject
    private PointAvancementSearchRepository pointAvancementSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPointAvancementMockMvc;

    private PointAvancement pointAvancement;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PointAvancementResource pointAvancementResource = new PointAvancementResource();
        ReflectionTestUtils.setField(pointAvancementResource, "pointAvancementSearchRepository", pointAvancementSearchRepository);
        ReflectionTestUtils.setField(pointAvancementResource, "pointAvancementRepository", pointAvancementRepository);
        this.restPointAvancementMockMvc = MockMvcBuilders.standaloneSetup(pointAvancementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        pointAvancementSearchRepository.deleteAll();
        pointAvancement = new PointAvancement();
        pointAvancement.setLibelle(DEFAULT_LIBELLE);
        pointAvancement.setDescription(DEFAULT_DESCRIPTION);
        pointAvancement.setFichier(DEFAULT_FICHIER);
        pointAvancement.setFichierContentType(DEFAULT_FICHIER_CONTENT_TYPE);
        pointAvancement.setDatePub(DEFAULT_DATE_PUB);
        pointAvancement.setActif(DEFAULT_ACTIF);
    }

    @Test
    @Transactional
    public void createPointAvancement() throws Exception {
        int databaseSizeBeforeCreate = pointAvancementRepository.findAll().size();

        // Create the PointAvancement

        restPointAvancementMockMvc.perform(post("/api/point-avancements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pointAvancement)))
                .andExpect(status().isCreated());

        // Validate the PointAvancement in the database
        List<PointAvancement> pointAvancements = pointAvancementRepository.findAll();
        assertThat(pointAvancements).hasSize(databaseSizeBeforeCreate + 1);
        PointAvancement testPointAvancement = pointAvancements.get(pointAvancements.size() - 1);
        assertThat(testPointAvancement.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testPointAvancement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPointAvancement.getFichier()).isEqualTo(DEFAULT_FICHIER);
        assertThat(testPointAvancement.getFichierContentType()).isEqualTo(DEFAULT_FICHIER_CONTENT_TYPE);
        assertThat(testPointAvancement.getDatePub()).isEqualTo(DEFAULT_DATE_PUB);
        assertThat(testPointAvancement.isActif()).isEqualTo(DEFAULT_ACTIF);

        // Validate the PointAvancement in ElasticSearch
        PointAvancement pointAvancementEs = pointAvancementSearchRepository.findOne(testPointAvancement.getId());
        assertThat(pointAvancementEs).isEqualToComparingFieldByField(testPointAvancement);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = pointAvancementRepository.findAll().size();
        // set the field null
        pointAvancement.setLibelle(null);

        // Create the PointAvancement, which fails.

        restPointAvancementMockMvc.perform(post("/api/point-avancements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pointAvancement)))
                .andExpect(status().isBadRequest());

        List<PointAvancement> pointAvancements = pointAvancementRepository.findAll();
        assertThat(pointAvancements).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPointAvancements() throws Exception {
        // Initialize the database
        pointAvancementRepository.saveAndFlush(pointAvancement);

        // Get all the pointAvancements
        restPointAvancementMockMvc.perform(get("/api/point-avancements?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pointAvancement.getId().intValue())))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].fichierContentType").value(hasItem(DEFAULT_FICHIER_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].fichier").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER))))
                .andExpect(jsonPath("$.[*].datePub").value(hasItem(DEFAULT_DATE_PUB_STR)))
                .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())));
    }

    @Test
    @Transactional
    public void getPointAvancement() throws Exception {
        // Initialize the database
        pointAvancementRepository.saveAndFlush(pointAvancement);

        // Get the pointAvancement
        restPointAvancementMockMvc.perform(get("/api/point-avancements/{id}", pointAvancement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(pointAvancement.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.fichierContentType").value(DEFAULT_FICHIER_CONTENT_TYPE))
            .andExpect(jsonPath("$.fichier").value(Base64Utils.encodeToString(DEFAULT_FICHIER)))
            .andExpect(jsonPath("$.datePub").value(DEFAULT_DATE_PUB_STR))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPointAvancement() throws Exception {
        // Get the pointAvancement
        restPointAvancementMockMvc.perform(get("/api/point-avancements/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePointAvancement() throws Exception {
        // Initialize the database
        pointAvancementRepository.saveAndFlush(pointAvancement);
        pointAvancementSearchRepository.save(pointAvancement);
        int databaseSizeBeforeUpdate = pointAvancementRepository.findAll().size();

        // Update the pointAvancement
        PointAvancement updatedPointAvancement = new PointAvancement();
        updatedPointAvancement.setId(pointAvancement.getId());
        updatedPointAvancement.setLibelle(UPDATED_LIBELLE);
        updatedPointAvancement.setDescription(UPDATED_DESCRIPTION);
        updatedPointAvancement.setFichier(UPDATED_FICHIER);
        updatedPointAvancement.setFichierContentType(UPDATED_FICHIER_CONTENT_TYPE);
        updatedPointAvancement.setDatePub(UPDATED_DATE_PUB);
        updatedPointAvancement.setActif(UPDATED_ACTIF);

        restPointAvancementMockMvc.perform(put("/api/point-avancements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPointAvancement)))
                .andExpect(status().isOk());

        // Validate the PointAvancement in the database
        List<PointAvancement> pointAvancements = pointAvancementRepository.findAll();
        assertThat(pointAvancements).hasSize(databaseSizeBeforeUpdate);
        PointAvancement testPointAvancement = pointAvancements.get(pointAvancements.size() - 1);
        assertThat(testPointAvancement.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testPointAvancement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPointAvancement.getFichier()).isEqualTo(UPDATED_FICHIER);
        assertThat(testPointAvancement.getFichierContentType()).isEqualTo(UPDATED_FICHIER_CONTENT_TYPE);
        assertThat(testPointAvancement.getDatePub()).isEqualTo(UPDATED_DATE_PUB);
        assertThat(testPointAvancement.isActif()).isEqualTo(UPDATED_ACTIF);

        // Validate the PointAvancement in ElasticSearch
        PointAvancement pointAvancementEs = pointAvancementSearchRepository.findOne(testPointAvancement.getId());
        assertThat(pointAvancementEs).isEqualToComparingFieldByField(testPointAvancement);
    }

    @Test
    @Transactional
    public void deletePointAvancement() throws Exception {
        // Initialize the database
        pointAvancementRepository.saveAndFlush(pointAvancement);
        pointAvancementSearchRepository.save(pointAvancement);
        int databaseSizeBeforeDelete = pointAvancementRepository.findAll().size();

        // Get the pointAvancement
        restPointAvancementMockMvc.perform(delete("/api/point-avancements/{id}", pointAvancement.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean pointAvancementExistsInEs = pointAvancementSearchRepository.exists(pointAvancement.getId());
        assertThat(pointAvancementExistsInEs).isFalse();

        // Validate the database is empty
        List<PointAvancement> pointAvancements = pointAvancementRepository.findAll();
        assertThat(pointAvancements).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPointAvancement() throws Exception {
        // Initialize the database
        pointAvancementRepository.saveAndFlush(pointAvancement);
        pointAvancementSearchRepository.save(pointAvancement);

        // Search the pointAvancement
        restPointAvancementMockMvc.perform(get("/api/_search/point-avancements?query=id:" + pointAvancement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pointAvancement.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].fichierContentType").value(hasItem(DEFAULT_FICHIER_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichier").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER))))
            .andExpect(jsonPath("$.[*].datePub").value(hasItem(DEFAULT_DATE_PUB_STR)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())));
    }
}
