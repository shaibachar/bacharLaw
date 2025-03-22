package com.bachar.law.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Client getClientSample1() {
        return new Client().id(1L).address("address1").description("description1").email("email1").fullName("fullName1").phone("phone1");
    }

    public static Client getClientSample2() {
        return new Client().id(2L).address("address2").description("description2").email("email2").fullName("fullName2").phone("phone2");
    }

    public static Client getClientRandomSampleGenerator() {
        return new Client()
            .id(longCount.incrementAndGet())
            .address(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .fullName(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString());
    }
}
