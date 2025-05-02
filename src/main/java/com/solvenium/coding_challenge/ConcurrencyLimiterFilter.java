package com.solvenium.coding_challenge;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

class ConcurrencyLimiterFilter implements Filter { // intercept HTTP requests before they reach any controllers

    private final Semaphore semaphore;
    private final int maxConcurrentRequests;
    private final long timeoutMillis;

    public ConcurrencyLimiterFilter(
            @Value("${app.concurrency.limit:10}") int maxConcurrentRequests,
            @Value("${app.concurrency.timeout:100}") long timeoutMillis) {
        this.maxConcurrentRequests = maxConcurrentRequests;
        this.timeoutMillis = timeoutMillis;
        this.semaphore = new Semaphore(maxConcurrentRequests, true);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        boolean permitAcquired = false;
        try {
            // Try to acquire a permit with a timeout
            permitAcquired = semaphore.tryAcquire(timeoutMillis, TimeUnit.MILLISECONDS);

            if (permitAcquired) {
                // Permit acquired, process the request
                chain.doFilter(request, response);
            } else {
                // Max concurrent requests reached
                httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                httpResponse.getWriter().write("Server is busy. Please try again later.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            httpResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            httpResponse.getWriter().write("Request processing interrupted");
        } finally {
            // Release the permit if it was acquired
            if (permitAcquired) {
                semaphore.release();
            }
        }
    }
}
