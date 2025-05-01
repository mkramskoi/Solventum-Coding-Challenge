package com.solvenium.coding_challenge.services;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UrlShorteningService {

    private Map<String, String> urlMap = new ConcurrentHashMap<String, String>();
    private final String SHORT_URL_PREFIX = "http://";

    public UrlResponse encodeUrl( String url ){
        String dnsPart = url.replaceAll("^(http://|https://)", "");
        String shortUrl = ShortUrlGenerator.generateShortUrl();
        urlMap.put(shortUrl, dnsPart);
        return new UrlResponse(url, shortUrl);
    }

    public UrlResponse decodeUrl( String shortUrl){
        return new UrlResponse(SHORT_URL_PREFIX + urlMap.get(shortUrl), shortUrl);
    }

    public record UrlResponse(String url, String shortUrl) {}

    private String getProtocol(String url) {
        if (url.startsWith("http://")) {
            return "http://";
        } else if (url.startsWith("https://")) {
            return "https://";
        }
        return "";
    }
}
