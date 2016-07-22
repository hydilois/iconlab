package cm.iconprod.iconlab.web.rest;

import cm.iconprod.iconlab.IconlabApp;
import cm.iconprod.iconlab.domain.MessageHierachique;
import cm.iconprod.iconlab.repository.MessageHierachiqueRepository;
import cm.iconprod.iconlab.repository.search.MessageHierachiqueSearchRepository;

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
 * Test class for the MessageHierachiqueResource REST controller.
 *
 * @see MessageHierachiqueResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IconlabApp.class)
@WebAppConfiguration
@IntegrationTest
public class MessageHierachiqueResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_CONTENU = "AAAAA";
    private static final String UPDATED_CONTENU = "BBBBB";

    private static final byte[] DEFAULT_FICHIER = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FICHIER = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FICHIER_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FICHIER_CONTENT_TYPE = "image/png";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    @Inject
    private MessageHierachiqueRepository messageHierachiqueRepository;

    @Inject
    private MessageHierachiqueSearchRepository messageHierachiqueSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMessageHierachiqueMockMvc;

    private MessageHierachique messageHierachique;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MessageHierachiqueResource messageHierachiqueResource = new MessageHierachiqueResource();
        ReflectionTestUtils.setField(messageHierachiqueResource, "messageHierachiqueSearchRepository", messageHierachiqueSearchRepository);
        ReflectionTestUtils.setField(messageHierachiqueResource, "messageHierachiqueRepository", messageHierachiqueRepository);
        this.restMessageHierachiqueMockMvc = MockMvcBuilders.standaloneSetup(messageHierachiqueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        messageHierachiqueSearchRepository.deleteAll();
        messageHierachique = new MessageHierachique();
        messageHierachique.setContenu(DEFAULT_CONTENU);
        messageHierachique.setFichier(DEFAULT_FICHIER);
        messageHierachique.setFichierContentType(DEFAULT_FICHIER_CONTENT_TYPE);
        messageHierachique.setDate(DEFAULT_DATE);
        messageHierachique.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createMessageHierachique() throws Exception {
        int databaseSizeBeforeCreate = messageHierachiqueRepository.findAll().size();

        // Create the MessageHierachique

        restMessageHierachiqueMockMvc.perform(post("/api/message-hierachiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(messageHierachique)))
                .andExpect(status().isCreated());

        // Validate the MessageHierachique in the database
        List<MessageHierachique> messageHierachiques = messageHierachiqueRepository.findAll();
        assertThat(messageHierachiques).hasSize(databaseSizeBeforeCreate + 1);
        MessageHierachique testMessageHierachique = messageHierachiques.get(messageHierachiques.size() - 1);
        assertThat(testMessageHierachique.getContenu()).isEqualTo(DEFAULT_CONTENU);
        assertThat(testMessageHierachique.getFichier()).isEqualTo(DEFAULT_FICHIER);
        assertThat(testMessageHierachique.getFichierContentType()).isEqualTo(DEFAULT_FICHIER_CONTENT_TYPE);
        assertThat(testMessageHierachique.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testMessageHierachique.isStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the MessageHierachique in ElasticSearch
        MessageHierachique messageHierachiqueEs = messageHierachiqueSearchRepository.findOne(testMessageHierachique.getId());
        assertThat(messageHierachiqueEs).isEqualToComparingFieldByField(testMessageHierachique);
    }

    @Test
    @Transactional
    public void checkContenuIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageHierachiqueRepository.findAll().size();
        // set the field null
        messageHierachique.setContenu(null);

        // Create the MessageHierachique, which fails.

        restMessageHierachiqueMockMvc.perform(post("/api/message-hierachiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(messageHierachique)))
                .andExpect(status().isBadRequest());

        List<MessageHierachique> messageHierachiques = messageHierachiqueRepository.findAll();
        assertThat(messageHierachiques).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageHierachiqueRepository.findAll().size();
        // set the field null
        messageHierachique.setDate(null);

        // Create the MessageHierachique, which fails.

        restMessageHierachiqueMockMvc.perform(post("/api/message-hierachiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(messageHierachique)))
                .andExpect(status().isBadRequest());

        List<MessageHierachique> messageHierachiques = messageHierachiqueRepository.findAll();
        assertThat(messageHierachiques).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMessageHierachiques() throws Exception {
        // Initialize the database
        messageHierachiqueRepository.saveAndFlush(messageHierachique);

        // Get all the messageHierachiques
        restMessageHierachiqueMockMvc.perform(get("/api/message-hierachiques?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(messageHierachique.getId().intValue())))
                .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU.toString())))
                .andExpect(jsonPath("$.[*].fichierContentType").value(hasItem(DEFAULT_FICHIER_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].fichier").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER))))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void getMessageHierachique() throws Exception {
        // Initialize the database
        messageHierachiqueRepository.saveAndFlush(messageHierachique);

        // Get the messageHierachique
        restMessageHierachiqueMockMvc.perform(get("/api/message-hierachiques/{id}", messageHierachique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(messageHierachique.getId().intValue()))
            .andExpect(jsonPath("$.contenu").value(DEFAULT_CONTENU.toString()))
            .andExpect(jsonPath("$.fichierContentType").value(DEFAULT_FICHIER_CONTENT_TYPE))
            .andExpect(jsonPath("$.fichier").value(Base64Utils.encodeToString(DEFAULT_FICHIER)))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMessageHierachique() throws Exception {
        // Get the messageHierachique
        restMessageHierachiqueMockMvc.perform(get("/api/message-hierachiques/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMessageHierachique() throws Exception {
        // Initialize the database
        messageHierachiqueRepository.saveAndFlush(messageHierachique);
        messageHierachiqueSearchRepository.save(messageHierachique);
        int databaseSizeBeforeUpdate = messageHierachiqueRepository.findAll().size();

        // Update the messageHierachique
        MessageHierachique updatedMessageHierachique = new MessageHierachique();
        updatedMessageHierachique.setId(messageHierachique.getId());
        updatedMessageHierachique.setContenu(UPDATED_CONTENU);
        updatedMessageHierachique.setFichier(UPDATED_FICHIER);
        updatedMessageHierachique.setFichierContentType(UPDATED_FICHIER_CONTENT_TYPE);
        updatedMessageHierachique.setDate(UPDATED_DATE);
        updatedMessageHierachique.setStatus(UPDATED_STATUS);

        restMessageHierachiqueMockMvc.perform(put("/api/message-hierachiques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMessageHierachique)))
                .andExpect(status().isOk());

        // Validate the MessageHierachique in the database
        List<MessageHierachique> messageHierachiques = messageHierachiqueRepository.findAll();
        assertThat(messageHierachiques).hasSize(databaseSizeBeforeUpdate);
        MessageHierachique testMessageHierachique = messageHierachiques.get(messageHierachiques.size() - 1);
        assertThat(testMessageHierachique.getContenu()).isEqualTo(UPDATED_CONTENU);
        assertThat(testMessageHierachique.getFichier()).isEqualTo(UPDATED_FICHIER);
        assertThat(testMessageHierachique.getFichierContentType()).isEqualTo(UPDATED_FICHIER_CONTENT_TYPE);
        assertThat(testMessageHierachique.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMessageHierachique.isStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the MessageHierachique in ElasticSearch
        MessageHierachique messageHierachiqueEs = messageHierachiqueSearchRepository.findOne(testMessageHierachique.getId());
        assertThat(messageHierachiqueEs).isEqualToComparingFieldByField(testMessageHierachique);
    }

    @Test
    @Transactional
    public void deleteMessageHierachique() throws Exception {
        // Initialize the database
        messageHierachiqueRepository.saveAndFlush(messageHierachique);
        messageHierachiqueSearchRepository.save(messageHierachique);
        int databaseSizeBeforeDelete = messageHierachiqueRepository.findAll().size();

        // Get the messageHierachique
        restMessageHierachiqueMockMvc.perform(delete("/api/message-hierachiques/{id}", messageHierachique.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean messageHierachiqueExistsInEs = messageHierachiqueSearchRepository.exists(messageHierachique.getId());
        assertThat(messageHierachiqueExistsInEs).isFalse();

        // Validate the database is empty
        List<MessageHierachique> messageHierachiques = messageHierachiqueRepository.findAll();
        assertThat(messageHierachiques).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMessageHierachique() throws Exception {
        // Initialize the database
        messageHierachiqueRepository.saveAndFlush(messageHierachique);
        messageHierachiqueSearchRepository.save(messageHierachique);

        // Search the messageHierachique
        restMessageHierachiqueMockMvc.perform(get("/api/_search/message-hierachiques?query=id:" + messageHierachique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageHierachique.getId().intValue())))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU.toString())))
            .andExpect(jsonPath("$.[*].fichierContentType").value(hasItem(DEFAULT_FICHIER_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fichier").value(hasItem(Base64Utils.encodeToString(DEFAULT_FICHIER))))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }
}
