package com.bachar.law.domain;

import static com.bachar.law.domain.ClientTestSamples.*;
import static com.bachar.law.domain.PaymentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bachar.law.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payment.class);
        Payment payment1 = getPaymentSample1();
        Payment payment2 = new Payment();
        assertThat(payment1).isNotEqualTo(payment2);

        payment2.setId(payment1.getId());
        assertThat(payment1).isEqualTo(payment2);

        payment2 = getPaymentSample2();
        assertThat(payment1).isNotEqualTo(payment2);
    }

    @Test
    void clientTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        payment.setClient(clientBack);
        assertThat(payment.getClient()).isEqualTo(clientBack);

        payment.client(null);
        assertThat(payment.getClient()).isNull();
    }
}
