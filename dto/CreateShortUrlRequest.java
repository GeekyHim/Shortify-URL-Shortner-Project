package com.geekyhim.shortify.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateShortUrlRequest {

    @NotBlank(message = "URL cannot be empty")
    private String url;

    private Integer expiryInDays;
}