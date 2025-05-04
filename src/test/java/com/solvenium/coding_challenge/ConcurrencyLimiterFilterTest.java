package com.solvenium.coding_challenge;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import static org.mockito.Mockito.*;

class ConcurrencyLimiterFilterTest {

    private ConcurrencyLimiterFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;

    @BeforeEach
    void setUp() {
        filter = new ConcurrencyLimiterFilter(2, 100); // 2 max concurrent requests, 100ms timeout
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        try {
            when(response.getWriter()).thenReturn(mock(PrintWriter.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        chain = mock(FilterChain.class);
    }

    @Test
    void testDoFilter_PermitAcquired() throws IOException, ServletException {
        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(anyInt());
    }

    @Test
    void testDoFilter_MaxConcurrentRequestsReached() throws IOException, ServletException, InterruptedException {
        filter = new ConcurrencyLimiterFilter(2, 100, 1000);
        CountDownLatch latch = new CountDownLatch(1);

        Thread thread1 = new Thread(() -> {
            try {
                latch.await(); // Wait for the latch to be released
                System.out.println("Thread 1: Acquiring permit...");
                filter.doFilter(request, response, chain);
            } catch (IOException | ServletException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                latch.await(); // Wait for the latch to be released
                System.out.println("Thread 2: Acquiring permit...");
                filter.doFilter(request, response, chain);
            } catch (IOException | ServletException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();

        // Release the latch to allow both threads to proceed
        latch.countDown();

        // Wait for the first two threads to acquire permits
        Thread.sleep(50);

        // Third request should fail
        filter.doFilter(request, response, chain);

        thread1.join();
        thread2.join();

        verify(response).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        verify(response).getWriter();
    }

    @Test
    void testDoFilter_InterruptedException() throws IOException, ServletException, InterruptedException {
        Semaphore semaphore = mock(Semaphore.class);
        when(semaphore.tryAcquire(anyLong(), any())).thenThrow(new InterruptedException());
        filter = new ConcurrencyLimiterFilter(1, 100);
        ReflectionTestUtils.setField(filter, "semaphore", semaphore);
        filter.doFilter(request, response, chain);

        verify(response).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        verify(response).getWriter();
    }
}