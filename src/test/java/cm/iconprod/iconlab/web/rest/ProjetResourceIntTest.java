package cm.iconprod.iconlab.web.rest;

import cm.iconprod.iconlab.IconlabApp;
import cm.iconprod.iconlab.domain.Projet;
import cm.iconprod.iconlab.repository.ProjetRepository;
import cm.iconprod.iconlab.repository.search.ProjetSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ProjetResource REST controller.
 *
 * @see ProjetResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IconlabApp.class)
@WebAppConfiguration
@IntegrationTest
public class ProjetResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAA";
    private static final String UPDATED_NOM = "BBBBB";
    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final byte[] DEFAULT_FICHIER_PROJET = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FICHIER_PROJET = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FICHIER_PROJET_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FICHIER_PROJET_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    @Inject
    private ProjetRepository projetRepository;

    @Inject
    private ProjetSearchRepository projetSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProjetMockMvc;

    private Projet projet;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjetResource projetResource = new ProjetResource();
        ReflectionTestUtils.setField(projetResource, "projetSearchRepository", projetSearchRepository);
        ReflectionTestUtils.setField(projetResource, "projetRepository", projetRepository);
        this.restProjetMockMvc = MockMvcBuilders.standaloneSetup(projetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        projetSearchRepository.deleteAll();
        projet = new Projet();
        projet.setNom(DEFAULT_NOM);
        projet.setCode(DEFAULT_CODE);
        projet.setDescription(DEFAULT_DESCRIPTION);
        projet.setFichierProjet(DEFAULT_FICHIER_PROJET);
        projet.setFichierProjetContentType(DEFAULT_FICHIER_PROJET_CONTENT_TYPE);
        projet.setDateDebut(DEFAULT_DATE_DEBUT);
        projet.setDateFin(DEFAULT_DATE_FIN);
        projet.setActif(DEFAULT_ACTIF);
    }

    @Test
    @Transactional
    public void createProjet() throws Exception {
        int databaseSizeBeforeCreate = projetRepository.findAll().size();

        // Create the Projet

        restProjetMockMvc.perform(post("/api/projets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projet)))
                .andExpect(status().isCreated());

        // Validate the Projet in the database
        List<Projet> projets = projetRepository.findAll();
        assertThat(projets).hasSize(databaseSizeBeforeCreate + 1);
        Projet testProjet = projets.get(projets.size() - 1);
        assertThat(testProjet.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testProjet.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProjet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProjet.getFichierProjet()).isEqualTo(DEFAULT_FICHIER_PROJET);
        assertThat(testProjet.getFichierProjetContentType()).isEqualTo(DEFAULT_FICHIER_PROJET_CONTENT_TYPE);
        assertThat(testProjet.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testProjet.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testProjet.isActif()).isEqualTo(DEFAULT_ACTIF);

        // Validate the Projet in ElasticSearch
        Projet projetEs = projetSearchRepository.findOne(testProjet.getId());
        assertThat(projetEs).isEqualToComparingFieldByField(testProjet);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = projetRepository.findAll().size();
        // set the field null
        projet.setNom(null);

        // Create the Projet, which fails.

        restProjetMockMvc.perform(post("/api/projets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projet)))
                .andExpect(status().isBadRequest());

        List<Projet> projets = projetRepository.findAll();
        assertThat(projets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjets() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projets
        restProjetMockMvc.perform(get("/api/projets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(projet.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].fichierProjetContentType").value(hasItem(DEFAULT_FICHIER_PROJET_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].fichierProjet").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER_PROJET))))
                .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
                .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())));
    }

    @Test
    @Transactional
    public void getProjet() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get the projet
        restProjetMockMvc.perform(get("/api/projets/{id}", projet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(projet.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.fichierProjetContentType").value(DEFAULT_FICHIER_PROJET_CONTENT_TYPE))
            .andExpect(jsonPath("$.fichierProjet").value(Base64Utils.encodeToString(DEFAULT_FICHIER_PROJET)))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProjet() throws Exception {
        // Get the projet
        restProjetMockMvc.perform(get("/api/projets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjet() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);
        projetSearchRepository.save(projet);
        int databaseSizeBeforeUpdate = projetRepository.findAll().size();

        // Update the projet
        Projet updatedProjet = new Projet();
        updatedProjet.setId(projet.getId());
        updatedProjet.setNom(UPDATED_NOM);
        updatedProjet.setCode(UPDATED_CODE);
        updatedProjet.setDescription(UPDATED_DESCRIPTION);
        updatedProjet.setFichierProjet(UPDATED_FICHIER_PROJET);
        updatedProjet.setFichierProjetContentType(UPDATED_FICHIER_PROJET_CONTENT_TYPE);
        updatedProjet.setDateDebut(UPDATED_DATE_DEBUT);
        updatedProjet.setDateFin(UPDATED_DATE_FIN);
        updatedProjet.setActif(UPDATED_ACTIF);

        restProjetMockMvc.perform(put("/api/projets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProjet)))
                .andExpect(status().isOk());

        // Validate the Projet in the database
        List<Projet> projets = projetRepository.findAll();
        assertThat(projets).hasSize(databaseSizeBeforeUpdate);
        Projet testProjet = projets.get(projets.size() - 1);
        assertThat(testProjet.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testProjet.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProjet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProjet.getFichierProjet()).isEqualTo(UPDATED_FICHIER_PROJET);
        assertThat(testProjet.getFichierProjetContentType()).isEqualTo(UPDATED_FICHIER_PROJET_CONTENT_TYPE);
        assertThat(testProjet.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testProjet.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testProjet.isActif()).isEqualTo(UPDATED_ACTIF);

        // Validate the Projet in ElasticSearch
        Projet projetEs = projetSearchRepository.findOne(testProjet.getId());
        assertThat(projetEs).isEqualToComparingFieldByField(testProjet);
    }

    @Test
    @Transactional
    public void deleteProjet() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);
        projetSearchRepository.save(projet);
        int databaseSizeBeforeDelete = projetRepository.findAll().size();

        // Get the projet
        restProjetMockMvc.perform(delete("/api/projets/{id}", projet.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean projetExistsInEs = projetSearchRepository.exists(projet.getId());
        assertThat(projetExistsInEs).isFalse();

        // Validate the database is empty
        List<Projet> projets = projetRepository.findAll();
        assertThat(projets).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProjet() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);
        projetSearchRepository.save(projet);

        // Search the projet
        restProjetMockMvc.perform(get("/api/_search/projets?query=id:" + projet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projet.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].fichierProjetContentType").value(hasItem(DEFAULT_FICHIER_PROJET_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichierProjet").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER_PROJET))))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())));
    }
}
