package com.geekyhim.shortify.service;

import com.geekyhim.shortify.dto.CreateShortUrlRequest;
import com.geekyhim.shortify.dto.CreateShortUrlResponse;
import com.geekyhim.shortify.entity.Url;
import com.geekyhim.shortify.repository.UrlRepository;
import com.geekyhim.shortify.util.Base62Encoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class UrlService {

    private final StringRedisTemplate redisTemplate;
    private final UrlRepository urlRepository;

    public CreateShortUrlResponse createShortUrl(CreateShortUrlRequest request) {

        Url url = Url.builder()
                .originalUrl(request.getUrl())
                .createdAt(LocalDateTime.now())
                .clickCount(0L)
                .build();

        // Redis atomic counter
        Long counter = redisTemplate.opsForValue()
                .increment("url_counter");

        String shortCode = Base62Encoder.encode(counter);

        url.setShortCode(shortCode);

        // Expiry logic
        if (request.getExpiryInDays() != null) {
            url.setExpiryDate(
                    LocalDateTime.now().plusDays(request.getExpiryInDays())
            );
        }

        Url savedUrl = urlRepository.save(url);

        return CreateShortUrlResponse.builder()
                .originalUrl(savedUrl.getOriginalUrl())
                .shortUrl("http://localhost:8080/" + shortCode)
                .build();
    }


    public String getOriginalUrl(String shortCode) {

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new RuntimeException("Short URL not found"));

        // Expiry check
        if (url.getExpiryDate() != null &&
                url.getExpiryDate().isBefore(LocalDateTime.now())) {

            throw new RuntimeException("Short URL has expired");
        }

        // Analytics update
        url.setClickCount(url.getClickCount() + 1);

        url.setLastAccessedAt(LocalDateTime.now());

        urlRepository.save(url);

        return url.getOriginalUrl();
    }
}


//    public CreateShortUrlResponse createShortUrl(CreateShortUrlRequest request) {
//
//        // alrdy hai toh why make again
////        Optional<Url> existingUrl =
////                urlRepository.findByOriginalUrl(request.getUrl());
////
////        if (existingUrl.isPresent()) {
////
////            return CreateShortUrlResponse.builder()
////                    .originalUrl(existingUrl.get().getOriginalUrl())
////                    .shortUrl("http://localhost:8080/" +
////                            existingUrl.get().getShortCode())
////                    .build();
////        } -> went to counter approach
//
//
//        // make a short url
//        Url url = Url.builder()
//                .originalUrl(request.getUrl())
//                .createdAt(LocalDateTime.now())
//                .clickCount(0L)
//                .build();
//
//        // Save first to generate ID
//        Url savedUrl = urlRepository.save(url);
//
//        // Generate short code
//        String shortCode = Base62Encoder.encode(savedUrl.getId());
//
//        savedUrl.setShortCode(shortCode);
//
//        // Expiry logic
//        if (request.getExpiryInDays() != null) {
//            savedUrl.setExpiryDate(
//                    LocalDateTime.now().plusDays(request.getExpiryInDays())
//            );
//        }
//
//        urlRepository.save(savedUrl);
//
//        return CreateShortUrlResponse.builder()
//                .originalUrl(savedUrl.getOriginalUrl())
//                .shortUrl("http://localhost:8080/" + shortCode)
//                .build();
//    }
