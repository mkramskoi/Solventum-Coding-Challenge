package com.solvenium.coding_challenge.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ShortUrlGeneratorTest {

    @Test
    void testGenerateShortUrl_LengthAndPrefix() {
        // Act
        String shortUrl = ShortUrlGenerator.generateShortUrl();

        // Assert
        String expectedPrefix = "http://short.est/";
        assertTrue(shortUrl.startsWith(expectedPrefix), "Short URL should start with the expected prefix");
        assertEquals(expectedPrefix.length() + 10, shortUrl.length(), "Short URL should have the correct length");
    }

    @Test
    void testGenerateShortUrl_UniqueValues() {
        // Act
        String shortUrl1 = ShortUrlGenerator.generateShortUrl();
        String shortUrl2 = ShortUrlGenerator.generateShortUrl();

        // Assert
        assertTrue(!shortUrl1.equals(shortUrl2), "Generated short URLs should be unique");
    }
}
