package com.bachar.law.domain;

import static com.bachar.law.domain.AgreementTestSamples.*;
import static com.bachar.law.domain.ClientTestSamples.*;
import static com.bachar.law.domain.FeeTestSamples.*;
import static com.bachar.law.domain.FeeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bachar.law.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fee.class);
        Fee fee1 = getFeeSample1();
        Fee fee2 = new Fee();
        assertThat(fee1).isNotEqualTo(fee2);

        fee2.setId(fee1.getId());
        assertThat(fee1).isEqualTo(fee2);

        fee2 = getFeeSample2();
        assertThat(fee1).isNotEqualTo(fee2);
    }

    @Test
    void linkedFeesTest() {
        Fee fee = getFeeRandomSampleGenerator();
        Fee feeBack = getFeeRandomSampleGenerator();

        fee.addLinkedFees(feeBack);
        assertThat(fee.getLinkedFees()).containsOnly(feeBack);

        fee.removeLinkedFees(feeBack);
        assertThat(fee.getLinkedFees()).doesNotContain(feeBack);

        fee.linkedFees(new HashSet<>(Set.of(feeBack)));
        assertThat(fee.getLinkedFees()).containsOnly(feeBack);

        fee.setLinkedFees(new HashSet<>());
        assertThat(fee.getLinkedFees()).doesNotContain(feeBack);
    }

    @Test
    void linkedToTest() {
        Fee fee = getFeeRandomSampleGenerator();
        Fee feeBack = getFeeRandomSampleGenerator();

        fee.addLinkedTo(feeBack);
        assertThat(fee.getLinkedTos()).containsOnly(feeBack);
        assertThat(feeBack.getLinkedFees()).containsOnly(fee);

        fee.removeLinkedTo(feeBack);
        assertThat(fee.getLinkedTos()).doesNotContain(feeBack);
        assertThat(feeBack.getLinkedFees()).doesNotContain(fee);

        fee.linkedTos(new HashSet<>(Set.of(feeBack)));
        assertThat(fee.getLinkedTos()).containsOnly(feeBack);
        assertThat(feeBack.getLinkedFees()).containsOnly(fee);

        fee.setLinkedTos(new HashSet<>());
        assertThat(fee.getLinkedTos()).doesNotContain(feeBack);
        assertThat(feeBack.getLinkedFees()).doesNotContain(fee);
    }

    @Test
    void clientTest() {
        Fee fee = getFeeRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        fee.setClient(clientBack);
        assertThat(fee.getClient()).isEqualTo(clientBack);

        fee.client(null);
        assertThat(fee.getClient()).isNull();
    }

    @Test
    void agreementTest() {
        Fee fee = getFeeRandomSampleGenerator();
        Agreement agreementBack = getAgreementRandomSampleGenerator();

        fee.setAgreement(agreementBack);
        assertThat(fee.getAgreement()).isEqualTo(agreementBack);

        fee.agreement(null);
        assertThat(fee.getAgreement()).isNull();
    }
}
