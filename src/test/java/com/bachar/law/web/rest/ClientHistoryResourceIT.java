package com.bachar.law.web.rest;

import static com.bachar.law.domain.ClientHistoryAsserts.*;
import static com.bachar.law.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bachar.law.IntegrationTest;
import com.bachar.law.domain.ClientHistory;
import com.bachar.law.domain.enumeration.ClientHistorySubType;
import com.bachar.law.domain.enumeration.ClientHistoryType;
import com.bachar.law.repository.ClientHistoryRepository;
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
 * Integration tests for the {@link ClientHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientHistoryResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ClientHistoryType DEFAULT_TYPE = ClientHistoryType.PAYMENT;
    private static final ClientHistoryType UPDATED_TYPE = ClientHistoryType.CLIENT;

    private static final ClientHistorySubType DEFAULT_SUB_TYPE = ClientHistorySubType.CREATE;
    private static final ClientHistorySubType UPDATED_SUB_TYPE = ClientHistorySubType.UPDATE;

    private static final String ENTITY_API_URL = "/api/client-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClientHistoryRepository clientHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientHistoryMockMvc;

    private ClientHistory clientHistory;

    private ClientHistory insertedClientHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientHistory createEntity() {
        return new ClientHistory().description(DEFAULT_DESCRIPTION).date(DEFAULT_DATE).type(DEFAULT_TYPE).subType(DEFAULT_SUB_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientHistory createUpdatedEntity() {
        return new ClientHistory().description(UPDATED_DESCRIPTION).date(UPDATED_DATE).type(UPDATED_TYPE).subType(UPDATED_SUB_TYPE);
    }

    @BeforeEach
    public void initTest() {
        clientHistory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedClientHistory != null) {
            clientHistoryRepository.delete(insertedClientHistory);
            insertedClientHistory = null;
        }
    }

    @Test
    @Transactional
    void createClientHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ClientHistory
        var returnedClientHistory = om.readValue(
            restClientHistoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientHistory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClientHistory.class
        );

        // Validate the ClientHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertClientHistoryUpdatableFieldsEquals(returnedClientHistory, getPersistedClientHistory(returnedClientHistory));

        insertedClientHistory = returnedClientHistory;
    }

    @Test
    @Transactional
    void createClientHistoryWithExistingId() throws Exception {
        // Create the ClientHistory with an existing ID
        clientHistory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientHistory)))
            .andExpect(status().isBadRequest());

        // Validate the ClientHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllClientHistories() throws Exception {
        // Initialize the database
        insertedClientHistory = clientHistoryRepository.saveAndFlush(clientHistory);

        // Get all the clientHistoryList
        restClientHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].subType").value(hasItem(DEFAULT_SUB_TYPE.toString())));
    }

    @Test
    @Transactional
    void getClientHistory() throws Exception {
        // Initialize the database
        insertedClientHistory = clientHistoryRepository.saveAndFlush(clientHistory);

        // Get the clientHistory
        restClientHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, clientHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clientHistory.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.subType").value(DEFAULT_SUB_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingClientHistory() throws Exception {
        // Get the clientHistory
        restClientHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClientHistory() throws Exception {
        // Initialize the database
        insertedClientHistory = clientHistoryRepository.saveAndFlush(clientHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clientHistory
        ClientHistory updatedClientHistory = clientHistoryRepository.findById(clientHistory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClientHistory are not directly saved in db
        em.detach(updatedClientHistory);
        updatedClientHistory.description(UPDATED_DESCRIPTION).date(UPDATED_DATE).type(UPDATED_TYPE).subType(UPDATED_SUB_TYPE);

        restClientHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClientHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedClientHistory))
            )
            .andExpect(status().isOk());

        // Validate the ClientHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClientHistoryToMatchAllProperties(updatedClientHistory);
    }

    @Test
    @Transactional
    void putNonExistingClientHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClientHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClientHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClientHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClientHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedClientHistory = clientHistoryRepository.saveAndFlush(clientHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clientHistory using partial update
        ClientHistory partialUpdatedClientHistory = new ClientHistory();
        partialUpdatedClientHistory.setId(clientHistory.getId());

        restClientHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClientHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClientHistory))
            )
            .andExpect(status().isOk());

        // Validate the ClientHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedClientHistory, clientHistory),
            getPersistedClientHistory(clientHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateClientHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedClientHistory = clientHistoryRepository.saveAndFlush(clientHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clientHistory using partial update
        ClientHistory partialUpdatedClientHistory = new ClientHistory();
        partialUpdatedClientHistory.setId(clientHistory.getId());

        partialUpdatedClientHistory.description(UPDATED_DESCRIPTION).date(UPDATED_DATE).type(UPDATED_TYPE).subType(UPDATED_SUB_TYPE);

        restClientHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClientHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClientHistory))
            )
            .andExpect(status().isOk());

        // Validate the ClientHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientHistoryUpdatableFieldsEquals(partialUpdatedClientHistory, getPersistedClientHistory(partialUpdatedClientHistory));
    }

    @Test
    @Transactional
    void patchNonExistingClientHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clientHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClientHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClientHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientHistoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(clientHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClientHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClientHistory() throws Exception {
        // Initialize the database
        insertedClientHistory = clientHistoryRepository.saveAndFlush(clientHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the clientHistory
        restClientHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, clientHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return clientHistoryRepository.count();
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

    protected ClientHistory getPersistedClientHistory(ClientHistory clientHistory) {
        return clientHistoryRepository.findById(clientHistory.getId()).orElseThrow();
    }

    protected void assertPersistedClientHistoryToMatchAllProperties(ClientHistory expectedClientHistory) {
        assertClientHistoryAllPropertiesEquals(expectedClientHistory, getPersistedClientHistory(expectedClientHistory));
    }

    protected void assertPersistedClientHistoryToMatchUpdatableProperties(ClientHistory expectedClientHistory) {
        assertClientHistoryAllUpdatablePropertiesEquals(expectedClientHistory, getPersistedClientHistory(expectedClientHistory));
    }
}
