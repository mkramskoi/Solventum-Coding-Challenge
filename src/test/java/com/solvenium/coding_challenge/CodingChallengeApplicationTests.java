package com.solvenium.coding_challenge;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CodingChallengeApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        // Verify that the application context loads successfully
        assertNotNull(applicationContext, "Application context should not be null");
    }

    @Test
    void testConcurrencyLimiterFilterBeanExists() {
        // Verify that the ConcurrencyLimiterFilter bean is registered
        Object filterBean = applicationContext.getBean(ConcurrencyLimiterFilter.class);
        assertNotNull(filterBean, "ConcurrencyLimiterFilter bean should be registered in the application context");
    }
}