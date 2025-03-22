package com.bachar.law.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FeeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Fee getFeeSample1() {
        return new Fee()
            .id(1L)
            .adjustedValue("adjustedValue1")
            .adjustedValuePlus("adjustedValuePlus1")
            .description("description1")
            .name("name1");
    }

    public static Fee getFeeSample2() {
        return new Fee()
            .id(2L)
            .adjustedValue("adjustedValue2")
            .adjustedValuePlus("adjustedValuePlus2")
            .description("description2")
            .name("name2");
    }

    public static Fee getFeeRandomSampleGenerator() {
        return new Fee()
            .id(longCount.incrementAndGet())
            .adjustedValue(UUID.randomUUID().toString())
            .adjustedValuePlus(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString());
    }
}
