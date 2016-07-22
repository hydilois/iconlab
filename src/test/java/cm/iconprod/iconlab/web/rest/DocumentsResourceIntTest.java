package cm.iconprod.iconlab.web.rest;

import cm.iconprod.iconlab.IconlabApp;
import cm.iconprod.iconlab.domain.Documents;
import cm.iconprod.iconlab.repository.DocumentsRepository;
import cm.iconprod.iconlab.repository.search.DocumentsSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cm.iconprod.iconlab.domain.enumeration.Mode;

/**
 * Test class for the DocumentsResource REST controller.
 *
 * @see DocumentsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IconlabApp.class)
@WebAppConfiguration
@IntegrationTest
public class DocumentsResourceIntTest {

    private static final String DEFAULT_TITRE = "AAAAA";
    private static final String UPDATED_TITRE = "BBBBB";

    private static final byte[] DEFAULT_FICHIER = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FICHIER = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FICHIER_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FICHIER_CONTENT_TYPE = "image/png";

    private static final Mode DEFAULT_MODE = Mode.PUBLIC;
    private static final Mode UPDATED_MODE = Mode.PRIVE;

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    @Inject
    private DocumentsRepository documentsRepository;

    @Inject
    private DocumentsSearchRepository documentsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDocumentsMockMvc;

    private Documents documents;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DocumentsResource documentsResource = new DocumentsResource();
        ReflectionTestUtils.setField(documentsResource, "documentsSearchRepository", documentsSearchRepository);
        ReflectionTestUtils.setField(documentsResource, "documentsRepository", documentsRepository);
        this.restDocumentsMockMvc = MockMvcBuilders.standaloneSetup(documentsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        documentsSearchRepository.deleteAll();
        documents = new Documents();
        documents.setTitre(DEFAULT_TITRE);
        documents.setFichier(DEFAULT_FICHIER);
        documents.setFichierContentType(DEFAULT_FICHIER_CONTENT_TYPE);
        documents.setMode(DEFAULT_MODE);
        documents.setActif(DEFAULT_ACTIF);
    }

    @Test
    @Transactional
    public void createDocuments() throws Exception {
        int databaseSizeBeforeCreate = documentsRepository.findAll().size();

        // Create the Documents

        restDocumentsMockMvc.perform(post("/api/documents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(documents)))
                .andExpect(status().isCreated());

        // Validate the Documents in the database
        List<Documents> documents = documentsRepository.findAll();
        assertThat(documents).hasSize(databaseSizeBeforeCreate + 1);
        Documents testDocuments = documents.get(documents.size() - 1);
        assertThat(testDocuments.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testDocuments.getFichier()).isEqualTo(DEFAULT_FICHIER);
        assertThat(testDocuments.getFichierContentType()).isEqualTo(DEFAULT_FICHIER_CONTENT_TYPE);
        assertThat(testDocuments.getMode()).isEqualTo(DEFAULT_MODE);
        assertThat(testDocuments.isActif()).isEqualTo(DEFAULT_ACTIF);

        // Validate the Documents in ElasticSearch
        Documents documentsEs = documentsSearchRepository.findOne(testDocuments.getId());
        assertThat(documentsEs).isEqualToComparingFieldByField(testDocuments);
    }

    @Test
    @Transactional
    public void checkTitreIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentsRepository.findAll().size();
        // set the field null
        documents.setTitre(null);

        // Create the Documents, which fails.

        restDocumentsMockMvc.perform(post("/api/documents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(documents)))
                .andExpect(status().isBadRequest());

        List<Documents> documents = documentsRepository.findAll();
        assertThat(documents).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFichierIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentsRepository.findAll().size();
        // set the field null
        documents.setFichier(null);

        // Create the Documents, which fails.

        restDocumentsMockMvc.perform(post("/api/documents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(documents)))
                .andExpect(status().isBadRequest());

        List<Documents> documents = documentsRepository.findAll();
        assertThat(documents).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocuments() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        // Get all the documents
        restDocumentsMockMvc.perform(get("/api/documents?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(documents.getId().intValue())))
                .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
                .andExpect(jsonPath("$.[*].fichierContentType").value(hasItem(DEFAULT_FICHIER_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].fichier").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER))))
                .andExpect(jsonPath("$.[*].mode").value(hasItem(DEFAULT_MODE.toString())))
                .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())));
    }

    @Test
    @Transactional
    public void getDocuments() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);

        // Get the documents
        restDocumentsMockMvc.perform(get("/api/documents/{id}", documents.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(documents.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE.toString()))
            .andExpect(jsonPath("$.fichierContentType").value(DEFAULT_FICHIER_CONTENT_TYPE))
            .andExpect(jsonPath("$.fichier").value(Base64Utils.encodeToString(DEFAULT_FICHIER)))
            .andExpect(jsonPath("$.mode").value(DEFAULT_MODE.toString()))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDocuments() throws Exception {
        // Get the documents
        restDocumentsMockMvc.perform(get("/api/documents/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocuments() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);
        documentsSearchRepository.save(documents);
        int databaseSizeBeforeUpdate = documentsRepository.findAll().size();

        // Update the documents
        Documents updatedDocuments = new Documents();
        updatedDocuments.setId(documents.getId());
        updatedDocuments.setTitre(UPDATED_TITRE);
        updatedDocuments.setFichier(UPDATED_FICHIER);
        updatedDocuments.setFichierContentType(UPDATED_FICHIER_CONTENT_TYPE);
        updatedDocuments.setMode(UPDATED_MODE);
        updatedDocuments.setActif(UPDATED_ACTIF);

        restDocumentsMockMvc.perform(put("/api/documents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDocuments)))
                .andExpect(status().isOk());

        // Validate the Documents in the database
        List<Documents> documents = documentsRepository.findAll();
        assertThat(documents).hasSize(databaseSizeBeforeUpdate);
        Documents testDocuments = documents.get(documents.size() - 1);
        assertThat(testDocuments.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testDocuments.getFichier()).isEqualTo(UPDATED_FICHIER);
        assertThat(testDocuments.getFichierContentType()).isEqualTo(UPDATED_FICHIER_CONTENT_TYPE);
        assertThat(testDocuments.getMode()).isEqualTo(UPDATED_MODE);
        assertThat(testDocuments.isActif()).isEqualTo(UPDATED_ACTIF);

        // Validate the Documents in ElasticSearch
        Documents documentsEs = documentsSearchRepository.findOne(testDocuments.getId());
        assertThat(documentsEs).isEqualToComparingFieldByField(testDocuments);
    }

    @Test
    @Transactional
    public void deleteDocuments() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);
        documentsSearchRepository.save(documents);
        int databaseSizeBeforeDelete = documentsRepository.findAll().size();

        // Get the documents
        restDocumentsMockMvc.perform(delete("/api/documents/{id}", documents.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean documentsExistsInEs = documentsSearchRepository.exists(documents.getId());
        assertThat(documentsExistsInEs).isFalse();

        // Validate the database is empty
        List<Documents> documents = documentsRepository.findAll();
        assertThat(documents).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDocuments() throws Exception {
        // Initialize the database
        documentsRepository.saveAndFlush(documents);
        documentsSearchRepository.save(documents);

        // Search the documents
        restDocumentsMockMvc.perform(get("/api/_search/documents?query=id:" + documents.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documents.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].fichierContentType").value(hasItem(DEFAULT_FICHIER_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichier").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER))))
            .andExpect(jsonPath("$.[*].mode").value(hasItem(DEFAULT_MODE.toString())))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())));
    }
}
