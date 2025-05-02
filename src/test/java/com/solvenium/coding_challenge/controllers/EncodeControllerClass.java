package com.solvenium.coding_challenge.controllers;

import com.solvenium.coding_challenge.services.UrlShorteningService;
import com.solvenium.coding_challenge.services.UrlShorteningService.UrlResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EncodeControllerTest {

    private UrlShorteningService urlShorteningService;
    private EncodeController encodeController;

    @BeforeEach
    void setUp() {
        urlShorteningService = mock(UrlShorteningService.class);
        encodeController = new EncodeController(urlShorteningService);
    }

    @Test
    void testEncodeURL_Success() {
        // Arrange
        String inputUrl = "http://example.com";
        UrlResponse mockResponse = new UrlResponse("http://short.url/abc123", inputUrl);
        when(urlShorteningService.encodeUrl(inputUrl)).thenReturn(mockResponse);

        // Act
        ResponseEntity<UrlResponse> response = encodeController.encodeURL(inputUrl);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(urlShorteningService, times(1)).encodeUrl(inputUrl);
    }

    @Test
    void testEncodeURL_InvalidInput() {
        // Arrange
        String inputUrl = "";
        when(urlShorteningService.encodeUrl(inputUrl)).thenThrow(new IllegalArgumentException("Invalid URL"));

        // Act & Assert
        try {
            encodeController.encodeURL(inputUrl);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid URL", e.getMessage());
        }
        verify(urlShorteningService, times(1)).encodeUrl(inputUrl);
    }
}