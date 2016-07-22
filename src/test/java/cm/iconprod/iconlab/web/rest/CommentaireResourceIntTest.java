package cm.iconprod.iconlab.web.rest;

import cm.iconprod.iconlab.IconlabApp;
import cm.iconprod.iconlab.domain.Commentaire;
import cm.iconprod.iconlab.repository.CommentaireRepository;
import cm.iconprod.iconlab.repository.search.CommentaireSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CommentaireResource REST controller.
 *
 * @see CommentaireResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IconlabApp.class)
@WebAppConfiguration
@IntegrationTest
public class CommentaireResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_CONTENU = "AAAAA";
    private static final String UPDATED_CONTENU = "BBBBB";

    private static final ZonedDateTime DEFAULT_DATE_POST = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_POST = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_POST_STR = dateTimeFormatter.format(DEFAULT_DATE_POST);

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    @Inject
    private CommentaireRepository commentaireRepository;

    @Inject
    private CommentaireSearchRepository commentaireSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCommentaireMockMvc;

    private Commentaire commentaire;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommentaireResource commentaireResource = new CommentaireResource();
        ReflectionTestUtils.setField(commentaireResource, "commentaireSearchRepository", commentaireSearchRepository);
        ReflectionTestUtils.setField(commentaireResource, "commentaireRepository", commentaireRepository);
        this.restCommentaireMockMvc = MockMvcBuilders.standaloneSetup(commentaireResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        commentaireSearchRepository.deleteAll();
        commentaire = new Commentaire();
        commentaire.setContenu(DEFAULT_CONTENU);
        commentaire.setDatePost(DEFAULT_DATE_POST);
        commentaire.setActif(DEFAULT_ACTIF);
    }

    @Test
    @Transactional
    public void createCommentaire() throws Exception {
        int databaseSizeBeforeCreate = commentaireRepository.findAll().size();

        // Create the Commentaire

        restCommentaireMockMvc.perform(post("/api/commentaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commentaire)))
                .andExpect(status().isCreated());

        // Validate the Commentaire in the database
        List<Commentaire> commentaires = commentaireRepository.findAll();
        assertThat(commentaires).hasSize(databaseSizeBeforeCreate + 1);
        Commentaire testCommentaire = commentaires.get(commentaires.size() - 1);
        assertThat(testCommentaire.getContenu()).isEqualTo(DEFAULT_CONTENU);
        assertThat(testCommentaire.getDatePost()).isEqualTo(DEFAULT_DATE_POST);
        assertThat(testCommentaire.isActif()).isEqualTo(DEFAULT_ACTIF);

        // Validate the Commentaire in ElasticSearch
        Commentaire commentaireEs = commentaireSearchRepository.findOne(testCommentaire.getId());
        assertThat(commentaireEs).isEqualToComparingFieldByField(testCommentaire);
    }

    @Test
    @Transactional
    public void checkContenuIsRequired() throws Exception {
        int databaseSizeBeforeTest = commentaireRepository.findAll().size();
        // set the field null
        commentaire.setContenu(null);

        // Create the Commentaire, which fails.

        restCommentaireMockMvc.perform(post("/api/commentaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commentaire)))
                .andExpect(status().isBadRequest());

        List<Commentaire> commentaires = commentaireRepository.findAll();
        assertThat(commentaires).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCommentaires() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get all the commentaires
        restCommentaireMockMvc.perform(get("/api/commentaires?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(commentaire.getId().intValue())))
                .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU.toString())))
                .andExpect(jsonPath("$.[*].datePost").value(hasItem(DEFAULT_DATE_POST_STR)))
                .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())));
    }

    @Test
    @Transactional
    public void getCommentaire() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);

        // Get the commentaire
        restCommentaireMockMvc.perform(get("/api/commentaires/{id}", commentaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(commentaire.getId().intValue()))
            .andExpect(jsonPath("$.contenu").value(DEFAULT_CONTENU.toString()))
            .andExpect(jsonPath("$.datePost").value(DEFAULT_DATE_POST_STR))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCommentaire() throws Exception {
        // Get the commentaire
        restCommentaireMockMvc.perform(get("/api/commentaires/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommentaire() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);
        commentaireSearchRepository.save(commentaire);
        int databaseSizeBeforeUpdate = commentaireRepository.findAll().size();

        // Update the commentaire
        Commentaire updatedCommentaire = new Commentaire();
        updatedCommentaire.setId(commentaire.getId());
        updatedCommentaire.setContenu(UPDATED_CONTENU);
        updatedCommentaire.setDatePost(UPDATED_DATE_POST);
        updatedCommentaire.setActif(UPDATED_ACTIF);

        restCommentaireMockMvc.perform(put("/api/commentaires")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCommentaire)))
                .andExpect(status().isOk());

        // Validate the Commentaire in the database
        List<Commentaire> commentaires = commentaireRepository.findAll();
        assertThat(commentaires).hasSize(databaseSizeBeforeUpdate);
        Commentaire testCommentaire = commentaires.get(commentaires.size() - 1);
        assertThat(testCommentaire.getContenu()).isEqualTo(UPDATED_CONTENU);
        assertThat(testCommentaire.getDatePost()).isEqualTo(UPDATED_DATE_POST);
        assertThat(testCommentaire.isActif()).isEqualTo(UPDATED_ACTIF);

        // Validate the Commentaire in ElasticSearch
        Commentaire commentaireEs = commentaireSearchRepository.findOne(testCommentaire.getId());
        assertThat(commentaireEs).isEqualToComparingFieldByField(testCommentaire);
    }

    @Test
    @Transactional
    public void deleteCommentaire() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);
        commentaireSearchRepository.save(commentaire);
        int databaseSizeBeforeDelete = commentaireRepository.findAll().size();

        // Get the commentaire
        restCommentaireMockMvc.perform(delete("/api/commentaires/{id}", commentaire.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean commentaireExistsInEs = commentaireSearchRepository.exists(commentaire.getId());
        assertThat(commentaireExistsInEs).isFalse();

        // Validate the database is empty
        List<Commentaire> commentaires = commentaireRepository.findAll();
        assertThat(commentaires).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCommentaire() throws Exception {
        // Initialize the database
        commentaireRepository.saveAndFlush(commentaire);
        commentaireSearchRepository.save(commentaire);

        // Search the commentaire
        restCommentaireMockMvc.perform(get("/api/_search/commentaires?query=id:" + commentaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU.toString())))
            .andExpect(jsonPath("$.[*].datePost").value(hasItem(DEFAULT_DATE_POST_STR)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())));
    }
}
