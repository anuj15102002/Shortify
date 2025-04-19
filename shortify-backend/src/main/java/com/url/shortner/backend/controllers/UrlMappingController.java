package com.url.shortner.backend.controllers;

import com.url.shortner.backend.dto.ClickEventDTO;
import com.url.shortner.backend.dto.UrlMappingDTO;
import com.url.shortner.backend.models.Users;
import com.url.shortner.backend.services.UrlMappingService;
import com.url.shortner.backend.services.UserService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.*;


// {"originalUrl":"https://example.com"}
// shortUrl =   -> https://example.com

@RestController
@RequestMapping("/api/urls")
@AllArgsConstructor
public class UrlMappingController {
    private UrlMappingService urlMappingService;
    private UserService userService;

    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UrlMappingDTO> createShortUrl(@RequestBody Map<String, String> request, Principal principal) {
        String orignalUrl = request.get("originalUrl");
        Users user = userService.findByUsername(principal.getName());
        UrlMappingDTO urlMappingDTO = urlMappingService.createShortUrl(orignalUrl, user);
        return ResponseEntity.ok(urlMappingDTO);
    }

    @GetMapping("/myUrls")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UrlMappingDTO>> getUserUrls(Principal principal) {
        Users user = userService.findByUsername(principal.getName());
        List<UrlMappingDTO> urls = urlMappingService.getUrlsByUser(user);
        return ResponseEntity.ok(urls);
    }

    @GetMapping("/analytics/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ClickEventDTO>> getClickEventsByDate(@PathVariable String shortUrl,
                                                                    @RequestParam("startDate") String startDate,
                                                                    @RequestParam("endDate") String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

            LocalDateTime start = LocalDateTime.parse(startDate, formatter);
            LocalDateTime end = LocalDateTime.parse(endDate, formatter);
            List<ClickEventDTO> clickEventDTOs = urlMappingService.getClickEventsByDate(shortUrl, start, end);
            return ResponseEntity.ok(clickEventDTOs);



    }

    @GetMapping("/totalClicks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<LocalDate, Long>> getTotalClickByDate(Principal principal,
                                                                    @RequestParam("startDate") String startDate,
                                                                    @RequestParam("endDate") String endDate){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

            Users user = userService.findByUsername(principal.getName());
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);
            Map<LocalDate, Long> totalClicks = urlMappingService.getTotalClicksByDate(user, start, end);
            return ResponseEntity.ok(totalClicks);

    }
}
