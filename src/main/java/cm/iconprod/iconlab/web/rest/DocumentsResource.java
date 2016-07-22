package cm.iconprod.iconlab.web.rest;

import com.codahale.metrics.annotation.Timed;
import cm.iconprod.iconlab.domain.Documents;
import cm.iconprod.iconlab.repository.DocumentsRepository;
import cm.iconprod.iconlab.repository.search.DocumentsSearchRepository;
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
 * REST controller for managing Documents.
 */
@RestController
@RequestMapping("/api")
public class DocumentsResource {

    private final Logger log = LoggerFactory.getLogger(DocumentsResource.class);
        
    @Inject
    private DocumentsRepository documentsRepository;
    
    @Inject
    private DocumentsSearchRepository documentsSearchRepository;
    
    /**
     * POST  /documents : Create a new documents.
     *
     * @param documents the documents to create
     * @return the ResponseEntity with status 201 (Created) and with body the new documents, or with status 400 (Bad Request) if the documents has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/documents",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Documents> createDocuments(@Valid @RequestBody Documents documents) throws URISyntaxException {
        log.debug("REST request to save Documents : {}", documents);
        if (documents.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("documents", "idexists", "A new documents cannot already have an ID")).body(null);
        }
        Documents result = documentsRepository.save(documents);
        documentsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("documents", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /documents : Updates an existing documents.
     *
     * @param documents the documents to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated documents,
     * or with status 400 (Bad Request) if the documents is not valid,
     * or with status 500 (Internal Server Error) if the documents couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/documents",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Documents> updateDocuments(@Valid @RequestBody Documents documents) throws URISyntaxException {
        log.debug("REST request to update Documents : {}", documents);
        if (documents.getId() == null) {
            return createDocuments(documents);
        }
        Documents result = documentsRepository.save(documents);
        documentsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("documents", documents.getId().toString()))
            .body(result);
    }

    /**
     * GET  /documents : get all the documents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of documents in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/documents",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Documents>> getAllDocuments(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Documents");
        Page<Documents> page = documentsRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/documents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /documents/:id : get the "id" documents.
     *
     * @param id the id of the documents to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the documents, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/documents/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Documents> getDocuments(@PathVariable Long id) {
        log.debug("REST request to get Documents : {}", id);
        Documents documents = documentsRepository.findOne(id);
        return Optional.ofNullable(documents)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /documents/:id : delete the "id" documents.
     *
     * @param id the id of the documents to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/documents/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDocuments(@PathVariable Long id) {
        log.debug("REST request to delete Documents : {}", id);
        documentsRepository.delete(id);
        documentsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("documents", id.toString())).build();
    }

    /**
     * SEARCH  /_search/documents?query=:query : search for the documents corresponding
     * to the query.
     *
     * @param query the query of the documents search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/documents",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Documents>> searchDocuments(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Documents for query {}", query);
        Page<Documents> page = documentsSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/documents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
