package com.solvenium.coding_challenge.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/encode")
public class EncodeController {

    @GetMapping(value="", produces = {"application/json"})
    public ResponseEntity<String> encodeURL(
            @RequestParam(value = "url", required = true) String url
    ) {
        return new ResponseEntity<>(url, HttpStatus.OK);
    }
}
