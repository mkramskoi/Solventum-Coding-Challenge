package com.solvenium.coding_challenge.services;

import java.util.Base64;
import java.security.SecureRandom;

public class ShortUrlGenerator {

    private static final String BASE = "http://short.est/";
    private static final int SHORT_URL_LENGTH = 10;

    private static String generateBase64String(int length) {
        byte[] randomBytes = new byte[length];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes).substring(0, length);
    }

    public static String generateShortUrl(){
        return BASE + generateBase64String(SHORT_URL_LENGTH);
    }
}

