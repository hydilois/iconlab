package cm.iconprod.iconlab.web.rest;

import cm.iconprod.iconlab.service.StatistikDashboardService;
import cm.iconprod.iconlab.service.TacheService;
import com.codahale.metrics.annotation.Timed;
import cm.iconprod.iconlab.domain.Tache;
import cm.iconprod.iconlab.repository.TacheRepository;
import cm.iconprod.iconlab.repository.search.TacheSearchRepository;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Tache.
 */
@RestController
@RequestMapping("/api")
public class TacheResource {

    private final Logger log = LoggerFactory.getLogger(TacheResource.class);

    @Inject
    private TacheRepository tacheRepository;

    @Inject
    private TacheSearchRepository tacheSearchRepository;
    @Inject
    private StatistikDashboardService statService;

    @Inject
    private TacheService tacheService;

    /**
     * POST  /taches : Create a new tache.
     *
     * @param tache the tache to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tache, or with status 400 (Bad Request) if the tache has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/taches",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tache> createTache(@RequestBody Tache tache) throws URISyntaxException {
        log.debug("REST request to save Tache : {}", tache);
        if (tache.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tache", "idexists", "A new tache cannot already have an ID")).body(null);
        }
        Tache result = tacheRepository.save(tache);
        tacheSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/taches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tache", result.getId().toString()))
            .body(result);
    }
    /**
     * PUT  /taches : Updates an existing tache.
     *
     * @param tache the tache to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tache,
     * or with status 400 (Bad Request) if the tache is not valid,
     * or with status 500 (Internal Server Error) if the tache couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/taches",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tache> updateTache(@RequestBody Tache tache) throws URISyntaxException {
        log.debug("REST request to update Tache : {}", tache);
        if (tache.getId() == null) {
            return createTache(tache);
        }
        Tache result = tacheRepository.save(tache);
        tacheSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tache", tache.getId().toString()))
            .body(result);
    }

    /**
     * GET  /taches : get all the taches.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of taches in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/taches",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Tache>> getAllTaches(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Taches");
        Page<Tache> page = tacheRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/taches");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/taches/projet/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Tache> getAllTachesByProjet(@PathVariable Long id)
        throws URISyntaxException {
        //log.debug("REST request to get a page of Projets");
        List<Tache> taches = tacheService.findTacheByProjetBelong(id);
        return taches;
    }

    @RequestMapping(value = "/tachesGantt/projet/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TacheService.ParserGantt> getAllTachesByProjetGantt(@PathVariable Long id)
        throws URISyntaxException {
        //log.debug("REST request to get a page of Projets");
        List<TacheService.ParserGantt> taches1 = tacheService.findTacheByProjetBelongGantt(id);


        return taches1;
    }

    @RequestMapping(value = "/taches/statData",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Integer> getStatData()  throws URISyntaxException{
        System.out.println(statService.getStatData().size());
        List<Integer> liste = statService.getStatData();
        return liste ;
    }

    /**
     * GET  /taches/:id : get the "id" tache.
     *
     * @param id the id of the tache to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tache, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/taches/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tache> getTache(@PathVariable Long id) {
        log.debug("REST request to get Tache : {}", id);
        Tache tache = tacheRepository.findOne(id);
        return Optional.ofNullable(tache)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /taches/:id : delete the "id" tache.
     *
     * @param id the id of the tache to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/taches/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTache(@PathVariable Long id) {
        log.debug("REST request to delete Tache : {}", id);
        tacheRepository.delete(id);
        tacheSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tache", id.toString())).build();
    }

    /**
     * SEARCH  /_search/taches?query=:query : search for the tache corresponding
     * to the query.
     *
     * @param query the query of the tache search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/taches",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Tache>> searchTaches(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Taches for query {}", query);
        Page<Tache> page = tacheSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/taches");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
