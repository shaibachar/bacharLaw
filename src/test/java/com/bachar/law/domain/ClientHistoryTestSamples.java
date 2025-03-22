package com.bachar.law.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClientHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ClientHistory getClientHistorySample1() {
        return new ClientHistory().id(1L).description("description1");
    }

    public static ClientHistory getClientHistorySample2() {
        return new ClientHistory().id(2L).description("description2");
    }

    public static ClientHistory getClientHistoryRandomSampleGenerator() {
        return new ClientHistory().id(longCount.incrementAndGet()).description(UUID.randomUUID().toString());
    }
}
