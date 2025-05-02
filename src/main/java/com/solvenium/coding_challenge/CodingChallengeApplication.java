package com.solvenium.coding_challenge;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

@SpringBootApplication
public class CodingChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodingChallengeApplication.class, args);
	}

	@Bean
    public ConcurrencyLimiterFilter concurrencyLimiterFilter(
            @Value("${app.concurrency.limit:10}") int maxConcurrentRequests,
            @Value("${app.concurrency.timeout:100}") long timeoutMillis) {
        return new ConcurrencyLimiterFilter(maxConcurrentRequests, timeoutMillis);
    }

    @Bean
    public FilterRegistrationBean<ConcurrencyLimiterFilter> concurrencyLimiterFilterRegistration(ConcurrencyLimiterFilter filter) {
        FilterRegistrationBean<ConcurrencyLimiterFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*"); // Apply to all endpoints
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE); // Execute this filter first
        return registrationBean;
    }

}
