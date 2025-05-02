package com.solvenium.coding_challenge.services;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UrlShorteningService {

    // This map stores the short URL as the key and the original URL as the value
    private Map<String, String> reverseUrlMap = new ConcurrentHashMap<String, String>();
    // This map stores the original URL as the key and the short URL as the value
    private Map<String, String> urlMap = new ConcurrentHashMap<String, String>();
    private final String SHORT_URL_PREFIX = "http://";

    public UrlResponse encodeUrl( String url ){

        if (! isValidUrl(url)){
            return new UrlResponse("URL is not valid", null);
        }
        String dnsPart = url.replaceAll("^(http://|https://)", "");
        String shortUrl = ShortUrlGenerator.generateShortUrl();

        // to prevent regenerating new shortUrl for the same original URL
        if(reverseUrlMap.containsKey(dnsPart)){
            return new UrlResponse(url, reverseUrlMap.get(dnsPart));
        }
        urlMap.put(shortUrl, dnsPart);
        reverseUrlMap.put(dnsPart, shortUrl);

        return new UrlResponse(url, shortUrl);
    }

    public UrlResponse decodeUrl( String shortUrl){
        String originalDns = urlMap.get(shortUrl);
        if (originalDns == null) {
            return new UrlResponse("URL not found", shortUrl);
        }
        return new UrlResponse(SHORT_URL_PREFIX + originalDns, shortUrl);
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

    private boolean isValidUrl(String url) {
        String urlRegex = "^(https?://)([\\w.-]+)+(:\\d+)?(/([\\w/_.]*)?)?$";
        return url != null && url.matches(urlRegex);
    }
}
