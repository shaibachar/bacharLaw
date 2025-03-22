package com.bachar.law.domain;

import static com.bachar.law.domain.AgreementTestSamples.*;
import static com.bachar.law.domain.FeeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bachar.law.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AgreementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Agreement.class);
        Agreement agreement1 = getAgreementSample1();
        Agreement agreement2 = new Agreement();
        assertThat(agreement1).isNotEqualTo(agreement2);

        agreement2.setId(agreement1.getId());
        assertThat(agreement1).isEqualTo(agreement2);

        agreement2 = getAgreementSample2();
        assertThat(agreement1).isNotEqualTo(agreement2);
    }

    @Test
    void feesTest() {
        Agreement agreement = getAgreementRandomSampleGenerator();
        Fee feeBack = getFeeRandomSampleGenerator();

        agreement.addFees(feeBack);
        assertThat(agreement.getFees()).containsOnly(feeBack);
        assertThat(feeBack.getAgreement()).isEqualTo(agreement);

        agreement.removeFees(feeBack);
        assertThat(agreement.getFees()).doesNotContain(feeBack);
        assertThat(feeBack.getAgreement()).isNull();

        agreement.fees(new HashSet<>(Set.of(feeBack)));
        assertThat(agreement.getFees()).containsOnly(feeBack);
        assertThat(feeBack.getAgreement()).isEqualTo(agreement);

        agreement.setFees(new HashSet<>());
        assertThat(agreement.getFees()).doesNotContain(feeBack);
        assertThat(feeBack.getAgreement()).isNull();
    }
}
