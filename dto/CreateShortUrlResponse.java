package com.geekyhim.shortify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CreateShortUrlResponse {

    private String originalUrl;

    private String shortUrl;
}