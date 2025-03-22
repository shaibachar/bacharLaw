package com.bachar.law.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AgreementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Agreement getAgreementSample1() {
        return new Agreement().id(1L).name("name1");
    }

    public static Agreement getAgreementSample2() {
        return new Agreement().id(2L).name("name2");
    }

    public static Agreement getAgreementRandomSampleGenerator() {
        return new Agreement().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
