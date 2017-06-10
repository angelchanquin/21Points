package com.jhipster.health.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jhipster.health.domain.BloodPressure;

import com.jhipster.health.repository.BloodPressureRepository;
import com.jhipster.health.repository.search.BloodPressureSearchRepository;
import com.jhipster.health.web.rest.util.HeaderUtil;
import com.jhipster.health.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing BloodPressure.
 */
@RestController
@RequestMapping("/api")
public class BloodPressureResource {

    private final Logger log = LoggerFactory.getLogger(BloodPressureResource.class);

    private static final String ENTITY_NAME = "blood_pressure";

    private final BloodPressureRepository bloodPressureRepository;

    private final BloodPressureSearchRepository bloodPressureSearchRepository;

    public BloodPressureResource(BloodPressureRepository bloodPressureRepository, BloodPressureSearchRepository bloodPressureSearchRepository) {
        this.bloodPressureRepository = bloodPressureRepository;
        this.bloodPressureSearchRepository = bloodPressureSearchRepository;
    }

    /**
     * POST  /blood-pressures : Create a new blood_pressure.
     *
     * @param bloodPressure the BloodPressure to create
     * @return the ResponseEntity with status 201 (Created) and with body the new blood_pressure, or with status 400 (Bad Request) if the blood_pressure has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/blood-pressures")
    @Timed
    public ResponseEntity<BloodPressure> createBloodPressure(@Valid @RequestBody BloodPressure bloodPressure) throws URISyntaxException {
        log.debug("REST request to save BloodPressure : {}", bloodPressure);
        if (bloodPressure.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new blood_pressure cannot already have an ID")).body(null);
        }
        BloodPressure result = bloodPressureRepository.save(bloodPressure);
        bloodPressureSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/blood-pressures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /blood-pressures : Updates an existing BloodPressure.
     *
     * @param bloodPressure the BloodPressure to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated BloodPressure,
     * or with status 400 (Bad Request) if the BloodPressure is not valid,
     * or with status 500 (Internal Server Error) if the BloodPressure couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/blood-pressures")
    @Timed
    public ResponseEntity<BloodPressure> updateBloodPressure(@Valid @RequestBody BloodPressure bloodPressure) throws URISyntaxException {
        log.debug("REST request to update BloodPressure : {}", bloodPressure);
        if (bloodPressure.getId() == null) {
            return createBloodPressure(bloodPressure);
        }
        BloodPressure result = bloodPressureRepository.save(bloodPressure);
        bloodPressureSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bloodPressure.getId().toString()))
            .body(result);
    }

    /**
     * GET  /blood-pressures : get all the BloodPressures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of BloodPressures in body
     */
    @GetMapping("/blood-pressures")
    @Timed
    public ResponseEntity<List<BloodPressure>> getAllBloodPressures(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of BloodPressures");
        Page<BloodPressure> page = bloodPressureRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/blood-pressures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /blood-pressures/:id : get the "id" BloodPressure.
     *
     * @param id the id of the BloodPressure to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the BloodPressure, or with status 404 (Not Found)
     */
    @GetMapping("/blood-pressures/{id}")
    @Timed
    public ResponseEntity<BloodPressure> getBloodPressure(@PathVariable Long id) {
        log.debug("REST request to get BloodPressure : {}", id);
        BloodPressure bloodPressure = bloodPressureRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bloodPressure));
    }

    /**
     * DELETE  /blood-pressures/:id : delete the "id" BloodPressure.
     *
     * @param id the id of the BloodPressure to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/blood-pressures/{id}")
    @Timed
    public ResponseEntity<Void> deleteBloodPressure(@PathVariable Long id) {
        log.debug("REST request to delete BloodPressure : {}", id);
        bloodPressureRepository.delete(id);
        bloodPressureSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/blood-pressures?query=:query : search for the BloodPressure corresponding
     * to the query.
     *
     * @param query the query of the BloodPressure search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/blood-pressures")
    @Timed
    public ResponseEntity<List<BloodPressure>> searchBloodPressures(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of BloodPressures for query {}", query);
        Page<BloodPressure> page = bloodPressureSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/blood-pressures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
