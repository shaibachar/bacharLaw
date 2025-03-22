package com.bachar.law.web.rest;

import static com.bachar.law.domain.AgreementAsserts.*;
import static com.bachar.law.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bachar.law.IntegrationTest;
import com.bachar.law.domain.Agreement;
import com.bachar.law.repository.AgreementRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AgreementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AgreementResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/agreements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AgreementRepository agreementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgreementMockMvc;

    private Agreement agreement;

    private Agreement insertedAgreement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agreement createEntity() {
        return new Agreement().name(DEFAULT_NAME).startDate(DEFAULT_START_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agreement createUpdatedEntity() {
        return new Agreement().name(UPDATED_NAME).startDate(UPDATED_START_DATE);
    }

    @BeforeEach
    public void initTest() {
        agreement = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAgreement != null) {
            agreementRepository.delete(insertedAgreement);
            insertedAgreement = null;
        }
    }

    @Test
    @Transactional
    void createAgreement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Agreement
        var returnedAgreement = om.readValue(
            restAgreementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agreement)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Agreement.class
        );

        // Validate the Agreement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAgreementUpdatableFieldsEquals(returnedAgreement, getPersistedAgreement(returnedAgreement));

        insertedAgreement = returnedAgreement;
    }

    @Test
    @Transactional
    void createAgreementWithExistingId() throws Exception {
        // Create the Agreement with an existing ID
        agreement.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgreementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agreement)))
            .andExpect(status().isBadRequest());

        // Validate the Agreement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAgreements() throws Exception {
        // Initialize the database
        insertedAgreement = agreementRepository.saveAndFlush(agreement);

        // Get all the agreementList
        restAgreementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agreement.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())));
    }

    @Test
    @Transactional
    void getAgreement() throws Exception {
        // Initialize the database
        insertedAgreement = agreementRepository.saveAndFlush(agreement);

        // Get the agreement
        restAgreementMockMvc
            .perform(get(ENTITY_API_URL_ID, agreement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agreement.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAgreement() throws Exception {
        // Get the agreement
        restAgreementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAgreement() throws Exception {
        // Initialize the database
        insertedAgreement = agreementRepository.saveAndFlush(agreement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agreement
        Agreement updatedAgreement = agreementRepository.findById(agreement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAgreement are not directly saved in db
        em.detach(updatedAgreement);
        updatedAgreement.name(UPDATED_NAME).startDate(UPDATED_START_DATE);

        restAgreementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAgreement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAgreement))
            )
            .andExpect(status().isOk());

        // Validate the Agreement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAgreementToMatchAllProperties(updatedAgreement);
    }

    @Test
    @Transactional
    void putNonExistingAgreement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agreement.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgreementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agreement.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agreement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agreement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgreement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agreement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgreementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agreement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agreement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgreement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agreement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgreementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agreement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agreement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAgreementWithPatch() throws Exception {
        // Initialize the database
        insertedAgreement = agreementRepository.saveAndFlush(agreement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agreement using partial update
        Agreement partialUpdatedAgreement = new Agreement();
        partialUpdatedAgreement.setId(agreement.getId());

        partialUpdatedAgreement.startDate(UPDATED_START_DATE);

        restAgreementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgreement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgreement))
            )
            .andExpect(status().isOk());

        // Validate the Agreement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgreementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAgreement, agreement),
            getPersistedAgreement(agreement)
        );
    }

    @Test
    @Transactional
    void fullUpdateAgreementWithPatch() throws Exception {
        // Initialize the database
        insertedAgreement = agreementRepository.saveAndFlush(agreement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agreement using partial update
        Agreement partialUpdatedAgreement = new Agreement();
        partialUpdatedAgreement.setId(agreement.getId());

        partialUpdatedAgreement.name(UPDATED_NAME).startDate(UPDATED_START_DATE);

        restAgreementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgreement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgreement))
            )
            .andExpect(status().isOk());

        // Validate the Agreement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgreementUpdatableFieldsEquals(partialUpdatedAgreement, getPersistedAgreement(partialUpdatedAgreement));
    }

    @Test
    @Transactional
    void patchNonExistingAgreement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agreement.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgreementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agreement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agreement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agreement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgreement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agreement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgreementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agreement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agreement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgreement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agreement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgreementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(agreement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agreement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAgreement() throws Exception {
        // Initialize the database
        insertedAgreement = agreementRepository.saveAndFlush(agreement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the agreement
        restAgreementMockMvc
            .perform(delete(ENTITY_API_URL_ID, agreement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return agreementRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Agreement getPersistedAgreement(Agreement agreement) {
        return agreementRepository.findById(agreement.getId()).orElseThrow();
    }

    protected void assertPersistedAgreementToMatchAllProperties(Agreement expectedAgreement) {
        assertAgreementAllPropertiesEquals(expectedAgreement, getPersistedAgreement(expectedAgreement));
    }

    protected void assertPersistedAgreementToMatchUpdatableProperties(Agreement expectedAgreement) {
        assertAgreementAllUpdatablePropertiesEquals(expectedAgreement, getPersistedAgreement(expectedAgreement));
    }
}
