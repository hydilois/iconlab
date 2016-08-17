package cm.iconprod.iconlab.web.rest;

import com.codahale.metrics.annotation.Timed;
import cm.iconprod.iconlab.domain.Commentaire;
import cm.iconprod.iconlab.repository.CommentaireRepository;
import cm.iconprod.iconlab.repository.search.CommentaireSearchRepository;
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
 * REST controller for managing Commentaire.
 */
@RestController
@RequestMapping("/api")
public class CommentaireResource {

    private final Logger log = LoggerFactory.getLogger(CommentaireResource.class);
        
    @Inject
    private CommentaireRepository commentaireRepository;
    
    @Inject
    private CommentaireSearchRepository commentaireSearchRepository;
    
    /**
     * POST  /commentaires : Create a new commentaire.
     *
     * @param commentaire the commentaire to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commentaire, or with status 400 (Bad Request) if the commentaire has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/commentaires",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Commentaire> createCommentaire(@Valid @RequestBody Commentaire commentaire) throws URISyntaxException {
        log.debug("REST request to save Commentaire : {}", commentaire);
        if (commentaire.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("commentaire", "idexists", "A new commentaire cannot already have an ID")).body(null);
        }
        Commentaire result = commentaireRepository.save(commentaire);
        commentaireSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/commentaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("commentaire", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /commentaires : Updates an existing commentaire.
     *
     * @param commentaire the commentaire to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commentaire,
     * or with status 400 (Bad Request) if the commentaire is not valid,
     * or with status 500 (Internal Server Error) if the commentaire couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/commentaires",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Commentaire> updateCommentaire(@Valid @RequestBody Commentaire commentaire) throws URISyntaxException {
        log.debug("REST request to update Commentaire : {}", commentaire);
        if (commentaire.getId() == null) {
            return createCommentaire(commentaire);
        }
        Commentaire result = commentaireRepository.save(commentaire);
        commentaireSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("commentaire", commentaire.getId().toString()))
            .body(result);
    }

    /**
     * GET  /commentaires : get all the commentaires.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of commentaires in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/commentaires",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Commentaire>> getAllCommentaires(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Commentaires");
        Page<Commentaire> page = commentaireRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/commentaires");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /commentaires/:id : get the "id" commentaire.
     *
     * @param id the id of the commentaire to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commentaire, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/commentaires/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Commentaire> getCommentaire(@PathVariable Long id) {
        log.debug("REST request to get Commentaire : {}", id);
        Commentaire commentaire = commentaireRepository.findOne(id);
        return Optional.ofNullable(commentaire)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    //Recherche des commentaires par projet
    @RequestMapping(value = "/commentaires/project/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Commentaire> getCommentaireByProject(@PathVariable Long id) {
        log.debug("REST request to get commentare By Project : {}", id);
        System.out.println("Je suis la");
        return commentaireRepository.findCommentaireByProject(id);
    }
    
    /*Recherche des commentaires par tache*/
//    @RequestMapping(value = "/commentaires/tache/{id}",
//        method = RequestMethod.GET,
//        produces = MediaType.APPLICATION_JSON_VALUE)
//    @Timed
//    public List<Commentaire> getCommentaireByTask(@PathVariable Long id) {
//        return commentaireRepository.findCommentaireByTache(id);
//    }

    /**
     * DELETE  /commentaires/:id : delete the "id" commentaire.
     *
     * @param id the id of the commentaire to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/commentaires/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCommentaire(@PathVariable Long id) {
        log.debug("REST request to delete Commentaire : {}", id);
        commentaireRepository.delete(id);
        commentaireSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("commentaire", id.toString())).build();
    }

    /**
     * SEARCH  /_search/commentaires?query=:query : search for the commentaire corresponding
     * to the query.
     *
     * @param query the query of the commentaire search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/commentaires",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Commentaire>> searchCommentaires(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Commentaires for query {}", query);
        Page<Commentaire> page = commentaireSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/commentaires");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
