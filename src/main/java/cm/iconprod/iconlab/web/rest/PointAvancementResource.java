package cm.iconprod.iconlab.web.rest;

import cm.iconprod.iconlab.service.PointAvancementService;
import com.codahale.metrics.annotation.Timed;
import cm.iconprod.iconlab.domain.PointAvancement;
import cm.iconprod.iconlab.repository.PointAvancementRepository;
import cm.iconprod.iconlab.repository.search.PointAvancementSearchRepository;
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
 * REST controller for managing PointAvancement.
 */
@RestController
@RequestMapping("/api")
public class PointAvancementResource {

    private final Logger log = LoggerFactory.getLogger(PointAvancementResource.class);

    @Inject
    private PointAvancementRepository pointAvancementRepository;

    @Inject
    private PointAvancementSearchRepository pointAvancementSearchRepository;

    @Inject
    private PointAvancementService pointAvancementService;

    /**
     * POST  /point-avancements : Create a new pointAvancement.
     *
     * @param pointAvancement the pointAvancement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pointAvancement, or with status 400 (Bad Request) if the pointAvancement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/point-avancements",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PointAvancement> createPointAvancement(@Valid @RequestBody PointAvancement pointAvancement) throws URISyntaxException {
        log.debug("REST request to save PointAvancement : {}", pointAvancement);
        if (pointAvancement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("pointAvancement", "idexists", "A new pointAvancement cannot already have an ID")).body(null);
        }
        PointAvancement result = pointAvancementRepository.save(pointAvancement);
        pointAvancementSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/point-avancements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("pointAvancement", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /point-avancements : Updates an existing pointAvancement.
     *
     * @param pointAvancement the pointAvancement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pointAvancement,
     * or with status 400 (Bad Request) if the pointAvancement is not valid,
     * or with status 500 (Internal Server Error) if the pointAvancement couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/point-avancements",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PointAvancement> updatePointAvancement(@Valid @RequestBody PointAvancement pointAvancement) throws URISyntaxException {
        log.debug("REST request to update PointAvancement : {}", pointAvancement);
        if (pointAvancement.getId() == null) {
            return createPointAvancement(pointAvancement);
        }
        PointAvancement result = pointAvancementRepository.save(pointAvancement);
        pointAvancementSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("pointAvancement", pointAvancement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /point-avancements : get all the pointAvancements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pointAvancements in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/point-avancements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PointAvancement>> getAllPointAvancements(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PointAvancements");
        Page<PointAvancement> page = pointAvancementRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/point-avancements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /point-avancements/:id : get the "id" pointAvancement.
     *
     * @param id the id of the pointAvancement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pointAvancement, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/point-avancements/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PointAvancement> getPointAvancement(@PathVariable Long id) {
        log.debug("REST request to get PointAvancement : {}", id);
        PointAvancement pointAvancement = pointAvancementRepository.findOne(id);
        return Optional.ofNullable(pointAvancement)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/point-avancements/tache/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PointAvancement> getAllPointAvancementByProjet(@PathVariable Long id)
        throws URISyntaxException {
        //log.debug("REST request to get a page of Projets");
        List<PointAvancement> pointAvancements = pointAvancementService.findPointAvancementByProjetBelong(id);
        return pointAvancements;
    }

    /**
     * DELETE  /point-avancements/:id : delete the "id" pointAvancement.
     *
     * @param id the id of the pointAvancement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/point-avancements/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePointAvancement(@PathVariable Long id) {
        log.debug("REST request to delete PointAvancement : {}", id);
        pointAvancementRepository.delete(id);
        pointAvancementSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pointAvancement", id.toString())).build();
    }

    /**
     * SEARCH  /_search/point-avancements?query=:query : search for the pointAvancement corresponding
     * to the query.
     *
     * @param query the query of the pointAvancement search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/point-avancements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PointAvancement>> searchPointAvancements(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of PointAvancements for query {}", query);
        Page<PointAvancement> page = pointAvancementSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/point-avancements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
