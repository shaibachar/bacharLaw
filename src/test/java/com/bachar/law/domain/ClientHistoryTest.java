package com.bachar.law.domain;

import static com.bachar.law.domain.ClientHistoryTestSamples.*;
import static com.bachar.law.domain.ClientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bachar.law.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClientHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientHistory.class);
        ClientHistory clientHistory1 = getClientHistorySample1();
        ClientHistory clientHistory2 = new ClientHistory();
        assertThat(clientHistory1).isNotEqualTo(clientHistory2);

        clientHistory2.setId(clientHistory1.getId());
        assertThat(clientHistory1).isEqualTo(clientHistory2);

        clientHistory2 = getClientHistorySample2();
        assertThat(clientHistory1).isNotEqualTo(clientHistory2);
    }

    @Test
    void clientTest() {
        ClientHistory clientHistory = getClientHistoryRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        clientHistory.setClient(clientBack);
        assertThat(clientHistory.getClient()).isEqualTo(clientBack);

        clientHistory.client(null);
        assertThat(clientHistory.getClient()).isNull();
    }
}
