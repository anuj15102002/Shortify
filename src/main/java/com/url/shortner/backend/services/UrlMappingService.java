package com.url.shortner.backend.services;

import com.url.shortner.backend.dto.UrlMappingDTO;
import com.url.shortner.backend.models.UrlMapping;
import com.url.shortner.backend.models.Users;
import com.url.shortner.backend.repositories.UrlMappingRespository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class UrlMappingService {
    private UrlMappingRespository urlMappingRespository;

    public UrlMappingDTO createShortUrl(String orignalUrl, Users user) {
    UrlMappingDTO urlMappingDTO = new UrlMappingDTO();
        String shortUrl = generateShortUrl(orignalUrl);
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(orignalUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setUser(user);
        urlMapping.setCreatedDate(LocalDateTime.now());
        urlMappingRespository.save(urlMapping);
        return convertToUrlMappingDTO(urlMapping);
    }

    private UrlMappingDTO convertToUrlMappingDTO(UrlMapping urlMapping) {
        UrlMappingDTO urlMappingDTO = new UrlMappingDTO();
        urlMappingDTO.setId(urlMapping.getId());
        urlMappingDTO.setOriginalUrl(urlMapping.getOriginalUrl());
        urlMappingDTO.setShortUrl(urlMapping.getShortUrl());
        urlMappingDTO.setUser(urlMapping.getUser());
        urlMappingDTO.setCreatedDate(urlMapping.getCreatedDate());
        urlMappingDTO.setClickCount(urlMapping.getClickCount());
        return urlMappingDTO;

    }

    private String generateShortUrl(String orignalUrl) {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
    StringBuilder stringBuilder = new StringBuilder(8);
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            stringBuilder.append(characters.charAt(random.nextInt(characters.length())));
        }
        return stringBuilder.toString();


    }

    public List<UrlMappingDTO> getUrlsByUser(Users user) {
        return urlMappingRespository.findByUser(user).stream()
                .map(this::convertToUrlMappingDTO).toList();
    }
}
