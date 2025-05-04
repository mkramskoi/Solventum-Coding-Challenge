package com.solvenium.coding_challenge;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

class ConcurrencyLimiterFilter implements Filter {

    private final Semaphore semaphore;
    private final int maxConcurrentRequests;
    private final long timeoutMillis;
    private final long artificialDelayMillis; // Added for testing

    public ConcurrencyLimiterFilter(
            int maxConcurrentRequests,
            long timeoutMillis) {
        this(maxConcurrentRequests, timeoutMillis, 0); // Default delay is 0
    }

    // Constructor with artificial delay for testing
    public ConcurrencyLimiterFilter(int maxConcurrentRequests, long timeoutMillis, long artificialDelayMillis) {
        this.maxConcurrentRequests = maxConcurrentRequests;
        this.timeoutMillis = timeoutMillis;
        this.artificialDelayMillis = artificialDelayMillis;
        this.semaphore = new Semaphore(maxConcurrentRequests, true);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        boolean permitAcquired = false;
        try {
            permitAcquired = semaphore.tryAcquire(timeoutMillis, TimeUnit.MILLISECONDS);

            if (permitAcquired) {
                // Simulate processing delay for testing
                if (artificialDelayMillis > 0) {
                    Thread.sleep(artificialDelayMillis);
                }
                chain.doFilter(request, response);
            } else {
                httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                httpResponse.getWriter().write("Server is busy. Please try again later.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            httpResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            httpResponse.getWriter().write("Request processing interrupted");
        } finally {
            if (permitAcquired) {
                semaphore.release();
            }
        }
    }
}