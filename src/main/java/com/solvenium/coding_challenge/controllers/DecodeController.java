package com.solvenium.coding_challenge.controllers;

import com.solvenium.coding_challenge.services.UrlShorteningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/decode")
public class DecodeController {

    private final UrlShorteningService urlShorteningService;

    @Autowired
    public DecodeController(UrlShorteningService urlShorteningService){
        this.urlShorteningService = urlShorteningService;
    }
    @GetMapping(value="", produces = {"application/json"})
    public ResponseEntity<UrlShorteningService.UrlResponse> encodeURL(
            @RequestParam(required = true) String url
    ) {
        return new ResponseEntity<>(urlShorteningService.decodeUrl(url), HttpStatus.OK);
    }
}
