package com.bachar.law.web.rest;

import com.bachar.law.domain.ClientHistory;
import com.bachar.law.repository.ClientHistoryRepository;
import com.bachar.law.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bachar.law.domain.ClientHistory}.
 */
@RestController
@RequestMapping("/api/client-histories")
@Transactional
public class ClientHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(ClientHistoryResource.class);

    private static final String ENTITY_NAME = "clientHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClientHistoryRepository clientHistoryRepository;

    public ClientHistoryResource(ClientHistoryRepository clientHistoryRepository) {
        this.clientHistoryRepository = clientHistoryRepository;
    }

    /**
     * {@code POST  /client-histories} : Create a new clientHistory.
     *
     * @param clientHistory the clientHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clientHistory, or with status {@code 400 (Bad Request)} if the clientHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ClientHistory> createClientHistory(@RequestBody ClientHistory clientHistory) throws URISyntaxException {
        LOG.debug("REST request to save ClientHistory : {}", clientHistory);
        if (clientHistory.getId() != null) {
            throw new BadRequestAlertException("A new clientHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        clientHistory = clientHistoryRepository.save(clientHistory);
        return ResponseEntity.created(new URI("/api/client-histories/" + clientHistory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, clientHistory.getId().toString()))
            .body(clientHistory);
    }

    /**
     * {@code PUT  /client-histories/:id} : Updates an existing clientHistory.
     *
     * @param id the id of the clientHistory to save.
     * @param clientHistory the clientHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientHistory,
     * or with status {@code 400 (Bad Request)} if the clientHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clientHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClientHistory> updateClientHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ClientHistory clientHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to update ClientHistory : {}, {}", id, clientHistory);
        if (clientHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clientHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        clientHistory = clientHistoryRepository.save(clientHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clientHistory.getId().toString()))
            .body(clientHistory);
    }

    /**
     * {@code PATCH  /client-histories/:id} : Partial updates given fields of an existing clientHistory, field will ignore if it is null
     *
     * @param id the id of the clientHistory to save.
     * @param clientHistory the clientHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientHistory,
     * or with status {@code 400 (Bad Request)} if the clientHistory is not valid,
     * or with status {@code 404 (Not Found)} if the clientHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the clientHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClientHistory> partialUpdateClientHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ClientHistory clientHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ClientHistory partially : {}, {}", id, clientHistory);
        if (clientHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clientHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClientHistory> result = clientHistoryRepository
            .findById(clientHistory.getId())
            .map(existingClientHistory -> {
                if (clientHistory.getDescription() != null) {
                    existingClientHistory.setDescription(clientHistory.getDescription());
                }
                if (clientHistory.getDate() != null) {
                    existingClientHistory.setDate(clientHistory.getDate());
                }
                if (clientHistory.getType() != null) {
                    existingClientHistory.setType(clientHistory.getType());
                }
                if (clientHistory.getSubType() != null) {
                    existingClientHistory.setSubType(clientHistory.getSubType());
                }

                return existingClientHistory;
            })
            .map(clientHistoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clientHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /client-histories} : get all the clientHistories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clientHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ClientHistory>> getAllClientHistories(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ClientHistories");
        Page<ClientHistory> page = clientHistoryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /client-histories/:id} : get the "id" clientHistory.
     *
     * @param id the id of the clientHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clientHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientHistory> getClientHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ClientHistory : {}", id);
        Optional<ClientHistory> clientHistory = clientHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(clientHistory);
    }

    /**
     * {@code DELETE  /client-histories/:id} : delete the "id" clientHistory.
     *
     * @param id the id of the clientHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ClientHistory : {}", id);
        clientHistoryRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
