package com.geekyhim.shortify.controller;

import com.geekyhim.shortify.dto.CreateShortUrlRequest;
import com.geekyhim.shortify.dto.CreateShortUrlResponse;
import com.geekyhim.shortify.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/urls")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PostMapping
    public CreateShortUrlResponse createShortUrl(
            @Valid @RequestBody CreateShortUrlRequest request
    ) {

        return urlService.createShortUrl(request);
    }

//    @GetMapping("/{shortCode}")
//    public ResponseEntity<Void> redirectToOriginalUrl(
//            @PathVariable String shortCode
//    ) {
//
//        String originalUrl = urlService.getOriginalUrl(shortCode);
//
//        return ResponseEntity
//                .status(HttpStatus.FOUND)
//                .location(URI.create(originalUrl))
//                .build();
//    }

}