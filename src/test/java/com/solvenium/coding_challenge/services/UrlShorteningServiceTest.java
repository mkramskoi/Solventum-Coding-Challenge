package com.solvenium.coding_challenge.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UrlShorteningServiceTest {

    private UrlShorteningService urlShorteningService;

    @BeforeEach
    void setUp() {
        urlShorteningService = new UrlShorteningService();
    }

    @Test
    void testEncodeUrl_Success() {
        // Arrange
        String originalUrl = "http://example.com";

        // Act
        UrlShorteningService.UrlResponse response = urlShorteningService.encodeUrl(originalUrl);

        // Assert
        assertEquals(originalUrl, response.url());
        assertEquals("http://", response.shortUrl().substring(0, 7));
    }

    @Test
    void testDecodeUrl_Success() {
        // Arrange
        String originalUrl = "http://example.com";
        UrlShorteningService.UrlResponse encodeResponse = urlShorteningService.encodeUrl(originalUrl);

        // Act
        UrlShorteningService.UrlResponse decodeResponse = urlShorteningService.decodeUrl(encodeResponse.shortUrl());

        // Assert
        assertEquals(originalUrl, decodeResponse.url());
        assertEquals(encodeResponse.shortUrl(), decodeResponse.shortUrl());
    }

    @Test
    void testEncodeUrl_ValidUrl() {
        // Arrange
        String originalUrl = "http://example.com";

        // Act
        UrlShorteningService.UrlResponse response = urlShorteningService.encodeUrl(originalUrl);

        // Assert
        assertNotNull(response);
        assertEquals(originalUrl, response.url());
        assertNotNull(response.shortUrl());
    }

    @Test
    void testEncodeUrl_InvalidUrl() {
        // Arrange
        String invalidUrl = "invalid_url";

        // Act
        UrlShorteningService.UrlResponse response = urlShorteningService.encodeUrl(invalidUrl);

        // Assert
        assertNotNull(response);
        assertEquals("URL is not valid", response.url());
        assertEquals(null, response.shortUrl());
    }

    @Test
    void testDecodeUrl_ValidShortUrl() {
        // Arrange
        String originalUrl = "http://example.com";
        UrlShorteningService.UrlResponse encodeResponse = urlShorteningService.encodeUrl(originalUrl);

        // Act
        UrlShorteningService.UrlResponse decodeResponse = urlShorteningService.decodeUrl(encodeResponse.shortUrl());

        // Assert
        assertNotNull(decodeResponse);
        assertEquals(originalUrl, decodeResponse.url());
        assertEquals(encodeResponse.shortUrl(), decodeResponse.shortUrl());
    }

    @Test
    void testDecodeUrl_InvalidShortUrl() {
        // Arrange
        String invalidShortUrl = "http://short.est/invalid";

        // Act
        UrlShorteningService.UrlResponse response = urlShorteningService.decodeUrl(invalidShortUrl);

        // Assert
        assertNotNull(response);
        assertEquals("URL not found", response.url());
        assertEquals(invalidShortUrl, response.shortUrl());
    }

    @Test
    void testEncodeUrl_SameUrlTwice() {
        // Arrange
        String originalUrl = "http://example.com";

        // Act
        UrlShorteningService.UrlResponse firstResponse = urlShorteningService.encodeUrl(originalUrl);
        UrlShorteningService.UrlResponse secondResponse = urlShorteningService.encodeUrl(originalUrl);

        // Assert
        assertEquals(firstResponse.shortUrl(), secondResponse.shortUrl());
    }
}