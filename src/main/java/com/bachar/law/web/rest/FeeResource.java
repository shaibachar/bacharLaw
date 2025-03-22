package com.bachar.law.web.rest;

import com.bachar.law.domain.Fee;
import com.bachar.law.repository.FeeRepository;
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
 * REST controller for managing {@link com.bachar.law.domain.Fee}.
 */
@RestController
@RequestMapping("/api/fees")
@Transactional
public class FeeResource {

    private static final Logger LOG = LoggerFactory.getLogger(FeeResource.class);

    private static final String ENTITY_NAME = "fee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeeRepository feeRepository;

    public FeeResource(FeeRepository feeRepository) {
        this.feeRepository = feeRepository;
    }

    /**
     * {@code POST  /fees} : Create a new fee.
     *
     * @param fee the fee to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fee, or with status {@code 400 (Bad Request)} if the fee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Fee> createFee(@RequestBody Fee fee) throws URISyntaxException {
        LOG.debug("REST request to save Fee : {}", fee);
        if (fee.getId() != null) {
            throw new BadRequestAlertException("A new fee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        fee = feeRepository.save(fee);
        return ResponseEntity.created(new URI("/api/fees/" + fee.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, fee.getId().toString()))
            .body(fee);
    }

    /**
     * {@code PUT  /fees/:id} : Updates an existing fee.
     *
     * @param id the id of the fee to save.
     * @param fee the fee to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fee,
     * or with status {@code 400 (Bad Request)} if the fee is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fee couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Fee> updateFee(@PathVariable(value = "id", required = false) final Long id, @RequestBody Fee fee)
        throws URISyntaxException {
        LOG.debug("REST request to update Fee : {}, {}", id, fee);
        if (fee.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fee.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        fee = feeRepository.save(fee);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fee.getId().toString()))
            .body(fee);
    }

    /**
     * {@code PATCH  /fees/:id} : Partial updates given fields of an existing fee, field will ignore if it is null
     *
     * @param id the id of the fee to save.
     * @param fee the fee to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fee,
     * or with status {@code 400 (Bad Request)} if the fee is not valid,
     * or with status {@code 404 (Not Found)} if the fee is not found,
     * or with status {@code 500 (Internal Server Error)} if the fee couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Fee> partialUpdateFee(@PathVariable(value = "id", required = false) final Long id, @RequestBody Fee fee)
        throws URISyntaxException {
        LOG.debug("REST request to partial update Fee partially : {}, {}", id, fee);
        if (fee.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fee.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Fee> result = feeRepository
            .findById(fee.getId())
            .map(existingFee -> {
                if (fee.getActive() != null) {
                    existingFee.setActive(fee.getActive());
                }
                if (fee.getAdjustedValue() != null) {
                    existingFee.setAdjustedValue(fee.getAdjustedValue());
                }
                if (fee.getAdjustedValuePlus() != null) {
                    existingFee.setAdjustedValuePlus(fee.getAdjustedValuePlus());
                }
                if (fee.getAmount() != null) {
                    existingFee.setAmount(fee.getAmount());
                }
                if (fee.getDescription() != null) {
                    existingFee.setDescription(fee.getDescription());
                }
                if (fee.getName() != null) {
                    existingFee.setName(fee.getName());
                }
                if (fee.getStartDate() != null) {
                    existingFee.setStartDate(fee.getStartDate());
                }
                if (fee.getValue() != null) {
                    existingFee.setValue(fee.getValue());
                }

                return existingFee;
            })
            .map(feeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fee.getId().toString())
        );
    }

    /**
     * {@code GET  /fees} : get all the fees.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fees in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Fee>> getAllFees(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Fees");
        Page<Fee> page;
        if (eagerload) {
            page = feeRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = feeRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fees/:id} : get the "id" fee.
     *
     * @param id the id of the fee to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fee, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Fee> getFee(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Fee : {}", id);
        Optional<Fee> fee = feeRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(fee);
    }

    /**
     * {@code DELETE  /fees/:id} : delete the "id" fee.
     *
     * @param id the id of the fee to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFee(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Fee : {}", id);
        feeRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
