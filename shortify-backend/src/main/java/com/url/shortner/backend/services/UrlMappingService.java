package com.url.shortner.backend.services;

import com.url.shortner.backend.dto.ClickEventDTO;
import com.url.shortner.backend.dto.UrlMappingDTO;
import com.url.shortner.backend.models.ClickEvents;
import com.url.shortner.backend.models.UrlMapping;
import com.url.shortner.backend.models.Users;
import com.url.shortner.backend.repositories.ClickEventRepository;
import com.url.shortner.backend.repositories.UrlMappingRespository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UrlMappingService {
    private UrlMappingRespository urlMappingRespository;
    private ClickEventRepository clickEventRepository;

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

    public List<ClickEventDTO> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end) {
        UrlMapping urlMapping = urlMappingRespository.findByShortUrl(shortUrl);
        if(urlMapping == null) return null;
        return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, start, end).stream()
                .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting()))
                .entrySet().stream()
                .map(entry -> {
                    ClickEventDTO clickEVentDTO = new ClickEventDTO();
                    clickEVentDTO.setCreatedDate(entry.getKey());
                    clickEVentDTO.setClickCount(entry.getValue());
                    return clickEVentDTO;
                }).collect(Collectors.toList());
    }

    public Map<LocalDate, Long> getTotalClicksByDate(Users user, LocalDate start, LocalDate end) {

    List<UrlMapping> urlMappings = urlMappingRespository.findByUser(user);
    List<ClickEvents> clickEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappings ,start.atStartOfDay(), end.plusDays(1).atStartOfDay());
    return clickEvents.stream().
            collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting()));
    }

    public UrlMapping getOriginalUrl(String shortUrl) {
        UrlMapping urlMapping = urlMappingRespository.findByShortUrl(shortUrl);
        if(urlMapping != null)
        {
            urlMapping.setClickCount(urlMapping.getClickCount() + 1);
            urlMappingRespository.save(urlMapping);

            //Record the ClickEvents
            ClickEvents clickEvents = new ClickEvents();
            clickEvents.setClickDate(LocalDateTime.now());
            clickEvents.setUrlMapping(urlMapping);
            clickEventRepository.save(clickEvents);
        }
        return urlMapping;
    }
}
