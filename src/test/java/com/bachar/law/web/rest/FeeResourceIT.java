package com.bachar.law.web.rest;

import static com.bachar.law.domain.FeeAsserts.*;
import static com.bachar.law.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bachar.law.IntegrationTest;
import com.bachar.law.domain.Fee;
import com.bachar.law.repository.FeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FeeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FeeResourceIT {

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_ADJUSTED_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_ADJUSTED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_ADJUSTED_VALUE_PLUS = "AAAAAAAAAA";
    private static final String UPDATED_ADJUSTED_VALUE_PLUS = "BBBBBBBBBB";

    private static final Float DEFAULT_AMOUNT = 1F;
    private static final Float UPDATED_AMOUNT = 2F;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Float DEFAULT_VALUE = 1F;
    private static final Float UPDATED_VALUE = 2F;

    private static final String ENTITY_API_URL = "/api/fees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FeeRepository feeRepository;

    @Mock
    private FeeRepository feeRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeeMockMvc;

    private Fee fee;

    private Fee insertedFee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fee createEntity() {
        return new Fee()
            .active(DEFAULT_ACTIVE)
            .adjustedValue(DEFAULT_ADJUSTED_VALUE)
            .adjustedValuePlus(DEFAULT_ADJUSTED_VALUE_PLUS)
            .amount(DEFAULT_AMOUNT)
            .description(DEFAULT_DESCRIPTION)
            .name(DEFAULT_NAME)
            .startDate(DEFAULT_START_DATE)
            .value(DEFAULT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fee createUpdatedEntity() {
        return new Fee()
            .active(UPDATED_ACTIVE)
            .adjustedValue(UPDATED_ADJUSTED_VALUE)
            .adjustedValuePlus(UPDATED_ADJUSTED_VALUE_PLUS)
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .value(UPDATED_VALUE);
    }

    @BeforeEach
    public void initTest() {
        fee = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedFee != null) {
            feeRepository.delete(insertedFee);
            insertedFee = null;
        }
    }

    @Test
    @Transactional
    void createFee() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Fee
        var returnedFee = om.readValue(
            restFeeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fee)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Fee.class
        );

        // Validate the Fee in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFeeUpdatableFieldsEquals(returnedFee, getPersistedFee(returnedFee));

        insertedFee = returnedFee;
    }

    @Test
    @Transactional
    void createFeeWithExistingId() throws Exception {
        // Create the Fee with an existing ID
        fee.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fee)))
            .andExpect(status().isBadRequest());

        // Validate the Fee in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFees() throws Exception {
        // Initialize the database
        insertedFee = feeRepository.saveAndFlush(fee);

        // Get all the feeList
        restFeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fee.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].adjustedValue").value(hasItem(DEFAULT_ADJUSTED_VALUE)))
            .andExpect(jsonPath("$.[*].adjustedValuePlus").value(hasItem(DEFAULT_ADJUSTED_VALUE_PLUS)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeesWithEagerRelationshipsIsEnabled() throws Exception {
        when(feeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFeeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(feeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(feeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFeeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(feeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFee() throws Exception {
        // Initialize the database
        insertedFee = feeRepository.saveAndFlush(fee);

        // Get the fee
        restFeeMockMvc
            .perform(get(ENTITY_API_URL_ID, fee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fee.getId().intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE))
            .andExpect(jsonPath("$.adjustedValue").value(DEFAULT_ADJUSTED_VALUE))
            .andExpect(jsonPath("$.adjustedValuePlus").value(DEFAULT_ADJUSTED_VALUE_PLUS))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingFee() throws Exception {
        // Get the fee
        restFeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFee() throws Exception {
        // Initialize the database
        insertedFee = feeRepository.saveAndFlush(fee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fee
        Fee updatedFee = feeRepository.findById(fee.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFee are not directly saved in db
        em.detach(updatedFee);
        updatedFee
            .active(UPDATED_ACTIVE)
            .adjustedValue(UPDATED_ADJUSTED_VALUE)
            .adjustedValuePlus(UPDATED_ADJUSTED_VALUE_PLUS)
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .value(UPDATED_VALUE);

        restFeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFee.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(updatedFee))
            )
            .andExpect(status().isOk());

        // Validate the Fee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFeeToMatchAllProperties(updatedFee);
    }

    @Test
    @Transactional
    void putNonExistingFee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fee.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeeMockMvc
            .perform(put(ENTITY_API_URL_ID, fee.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fee)))
            .andExpect(status().isBadRequest());

        // Validate the Fee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fee.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fee.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fee)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeeWithPatch() throws Exception {
        // Initialize the database
        insertedFee = feeRepository.saveAndFlush(fee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fee using partial update
        Fee partialUpdatedFee = new Fee();
        partialUpdatedFee.setId(fee.getId());

        partialUpdatedFee.adjustedValue(UPDATED_ADJUSTED_VALUE).adjustedValuePlus(UPDATED_ADJUSTED_VALUE_PLUS).name(UPDATED_NAME);

        restFeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFee.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFee))
            )
            .andExpect(status().isOk());

        // Validate the Fee in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFee, fee), getPersistedFee(fee));
    }

    @Test
    @Transactional
    void fullUpdateFeeWithPatch() throws Exception {
        // Initialize the database
        insertedFee = feeRepository.saveAndFlush(fee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fee using partial update
        Fee partialUpdatedFee = new Fee();
        partialUpdatedFee.setId(fee.getId());

        partialUpdatedFee
            .active(UPDATED_ACTIVE)
            .adjustedValue(UPDATED_ADJUSTED_VALUE)
            .adjustedValuePlus(UPDATED_ADJUSTED_VALUE_PLUS)
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .value(UPDATED_VALUE);

        restFeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFee.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFee))
            )
            .andExpect(status().isOk());

        // Validate the Fee in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeeUpdatableFieldsEquals(partialUpdatedFee, getPersistedFee(partialUpdatedFee));
    }

    @Test
    @Transactional
    void patchNonExistingFee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fee.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeeMockMvc
            .perform(patch(ENTITY_API_URL_ID, fee.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fee)))
            .andExpect(status().isBadRequest());

        // Validate the Fee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fee.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        fee.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fee)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFee() throws Exception {
        // Initialize the database
        insertedFee = feeRepository.saveAndFlush(fee);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the fee
        restFeeMockMvc.perform(delete(ENTITY_API_URL_ID, fee.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return feeRepository.count();
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

    protected Fee getPersistedFee(Fee fee) {
        return feeRepository.findById(fee.getId()).orElseThrow();
    }

    protected void assertPersistedFeeToMatchAllProperties(Fee expectedFee) {
        assertFeeAllPropertiesEquals(expectedFee, getPersistedFee(expectedFee));
    }

    protected void assertPersistedFeeToMatchUpdatableProperties(Fee expectedFee) {
        assertFeeAllUpdatablePropertiesEquals(expectedFee, getPersistedFee(expectedFee));
    }
}
