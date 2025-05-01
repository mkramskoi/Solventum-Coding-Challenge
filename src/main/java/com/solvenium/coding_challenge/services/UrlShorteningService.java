package com.solvenium.coding_challenge.services;

import org.springframework.stereotype.Service;

@Service
public class UrlShorteningService {

    public UrlResponse encodeUrl( String url){
        return new UrlResponse(url, url);
    }

    public UrlResponse decodeUrl( String url){
        return new UrlResponse(url, url);
    }

    public record UrlResponse(String url, String encodedUrl) {}
}
