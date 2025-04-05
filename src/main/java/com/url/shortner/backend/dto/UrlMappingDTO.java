package com.url.shortner.backend.dto;

import com.url.shortner.backend.models.Users;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UrlMappingDTO {
    private long id;
    private String originalUrl;
    private String shortUrl;
    private int clickCount;
    private LocalDateTime createdDate;
    private Users user;
}
