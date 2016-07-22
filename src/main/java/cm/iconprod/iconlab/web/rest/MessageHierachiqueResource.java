package cm.iconprod.iconlab.web.rest;

import com.codahale.metrics.annotation.Timed;
import cm.iconprod.iconlab.domain.MessageHierachique;
import cm.iconprod.iconlab.repository.MessageHierachiqueRepository;
import cm.iconprod.iconlab.repository.search.MessageHierachiqueSearchRepository;
import cm.iconprod.iconlab.web.rest.util.HeaderUtil;
import cm.iconprod.iconlab.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing MessageHierachique.
 */
@RestController
@RequestMapping("/api")
public class MessageHierachiqueResource {

    private final Logger log = LoggerFactory.getLogger(MessageHierachiqueResource.class);
        
    @Inject
    private MessageHierachiqueRepository messageHierachiqueRepository;
    
    @Inject
    private MessageHierachiqueSearchRepository messageHierachiqueSearchRepository;
    
    /**
     * POST  /message-hierachiques : Create a new messageHierachique.
     *
     * @param messageHierachique the messageHierachique to create
     * @return the ResponseEntity with status 201 (Created) and with body the new messageHierachique, or with status 400 (Bad Request) if the messageHierachique has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/message-hierachiques",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MessageHierachique> createMessageHierachique(@Valid @RequestBody MessageHierachique messageHierachique) throws URISyntaxException {
        log.debug("REST request to save MessageHierachique : {}", messageHierachique);
        if (messageHierachique.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("messageHierachique", "idexists", "A new messageHierachique cannot already have an ID")).body(null);
        }
        MessageHierachique result = messageHierachiqueRepository.save(messageHierachique);
        messageHierachiqueSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/message-hierachiques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("messageHierachique", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /message-hierachiques : Updates an existing messageHierachique.
     *
     * @param messageHierachique the messageHierachique to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated messageHierachique,
     * or with status 400 (Bad Request) if the messageHierachique is not valid,
     * or with status 500 (Internal Server Error) if the messageHierachique couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/message-hierachiques",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MessageHierachique> updateMessageHierachique(@Valid @RequestBody MessageHierachique messageHierachique) throws URISyntaxException {
        log.debug("REST request to update MessageHierachique : {}", messageHierachique);
        if (messageHierachique.getId() == null) {
            return createMessageHierachique(messageHierachique);
        }
        MessageHierachique result = messageHierachiqueRepository.save(messageHierachique);
        messageHierachiqueSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("messageHierachique", messageHierachique.getId().toString()))
            .body(result);
    }

    /**
     * GET  /message-hierachiques : get all the messageHierachiques.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of messageHierachiques in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/message-hierachiques",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MessageHierachique>> getAllMessageHierachiques(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of MessageHierachiques");
        Page<MessageHierachique> page = messageHierachiqueRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/message-hierachiques");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /message-hierachiques/:id : get the "id" messageHierachique.
     *
     * @param id the id of the messageHierachique to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the messageHierachique, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/message-hierachiques/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MessageHierachique> getMessageHierachique(@PathVariable Long id) {
        log.debug("REST request to get MessageHierachique : {}", id);
        MessageHierachique messageHierachique = messageHierachiqueRepository.findOne(id);
        return Optional.ofNullable(messageHierachique)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /message-hierachiques/:id : delete the "id" messageHierachique.
     *
     * @param id the id of the messageHierachique to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/message-hierachiques/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMessageHierachique(@PathVariable Long id) {
        log.debug("REST request to delete MessageHierachique : {}", id);
        messageHierachiqueRepository.delete(id);
        messageHierachiqueSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("messageHierachique", id.toString())).build();
    }

    /**
     * SEARCH  /_search/message-hierachiques?query=:query : search for the messageHierachique corresponding
     * to the query.
     *
     * @param query the query of the messageHierachique search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/message-hierachiques",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MessageHierachique>> searchMessageHierachiques(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of MessageHierachiques for query {}", query);
        Page<MessageHierachique> page = messageHierachiqueSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/message-hierachiques");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
