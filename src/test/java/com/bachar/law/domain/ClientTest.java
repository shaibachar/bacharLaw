package com.bachar.law.domain;

import static com.bachar.law.domain.ClientHistoryTestSamples.*;
import static com.bachar.law.domain.ClientTestSamples.*;
import static com.bachar.law.domain.FeeTestSamples.*;
import static com.bachar.law.domain.PaymentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bachar.law.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Client.class);
        Client client1 = getClientSample1();
        Client client2 = new Client();
        assertThat(client1).isNotEqualTo(client2);

        client2.setId(client1.getId());
        assertThat(client1).isEqualTo(client2);

        client2 = getClientSample2();
        assertThat(client1).isNotEqualTo(client2);
    }

    @Test
    void historiesTest() {
        Client client = getClientRandomSampleGenerator();
        ClientHistory clientHistoryBack = getClientHistoryRandomSampleGenerator();

        client.addHistories(clientHistoryBack);
        assertThat(client.getHistories()).containsOnly(clientHistoryBack);
        assertThat(clientHistoryBack.getClient()).isEqualTo(client);

        client.removeHistories(clientHistoryBack);
        assertThat(client.getHistories()).doesNotContain(clientHistoryBack);
        assertThat(clientHistoryBack.getClient()).isNull();

        client.histories(new HashSet<>(Set.of(clientHistoryBack)));
        assertThat(client.getHistories()).containsOnly(clientHistoryBack);
        assertThat(clientHistoryBack.getClient()).isEqualTo(client);

        client.setHistories(new HashSet<>());
        assertThat(client.getHistories()).doesNotContain(clientHistoryBack);
        assertThat(clientHistoryBack.getClient()).isNull();
    }

    @Test
    void paymentsTest() {
        Client client = getClientRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        client.addPayments(paymentBack);
        assertThat(client.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getClient()).isEqualTo(client);

        client.removePayments(paymentBack);
        assertThat(client.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getClient()).isNull();

        client.payments(new HashSet<>(Set.of(paymentBack)));
        assertThat(client.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getClient()).isEqualTo(client);

        client.setPayments(new HashSet<>());
        assertThat(client.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getClient()).isNull();
    }

    @Test
    void feesTest() {
        Client client = getClientRandomSampleGenerator();
        Fee feeBack = getFeeRandomSampleGenerator();

        client.addFees(feeBack);
        assertThat(client.getFees()).containsOnly(feeBack);
        assertThat(feeBack.getClient()).isEqualTo(client);

        client.removeFees(feeBack);
        assertThat(client.getFees()).doesNotContain(feeBack);
        assertThat(feeBack.getClient()).isNull();

        client.fees(new HashSet<>(Set.of(feeBack)));
        assertThat(client.getFees()).containsOnly(feeBack);
        assertThat(feeBack.getClient()).isEqualTo(client);

        client.setFees(new HashSet<>());
        assertThat(client.getFees()).doesNotContain(feeBack);
        assertThat(feeBack.getClient()).isNull();
    }
}
