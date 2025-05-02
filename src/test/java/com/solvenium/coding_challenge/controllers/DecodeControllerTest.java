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

class DecodeControllerTest {

    private UrlShorteningService urlShorteningService;
    private DecodeController decodeController;

    @BeforeEach
    void setUp() {
        urlShorteningService = mock(UrlShorteningService.class);
        decodeController = new DecodeController(urlShorteningService);
    }

    @Test
    void testEncodeURL_Success() {
        // Arrange
        String inputUrl = "http://short.url/abc123";
        UrlResponse mockResponse = new UrlResponse("http://example.com", inputUrl);
        when(urlShorteningService.decodeUrl(inputUrl)).thenReturn(mockResponse);

        // Act
        ResponseEntity<UrlResponse> response = decodeController.encodeURL(inputUrl);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(urlShorteningService, times(1)).decodeUrl(inputUrl);
    }

    @Test
    void testEncodeURL_InvalidInput() {
        // Arrange
        String inputUrl = "";
        when(urlShorteningService.decodeUrl(inputUrl)).thenThrow(new IllegalArgumentException("Invalid URL"));

        // Act & Assert
        try {
            decodeController.encodeURL(inputUrl);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid URL", e.getMessage());
        }
        verify(urlShorteningService, times(1)).decodeUrl(inputUrl);
    }
}
