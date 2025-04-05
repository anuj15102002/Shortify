package com.url.shortner.backend.controllers;

import com.url.shortner.backend.dto.UrlMappingDTO;
import com.url.shortner.backend.models.Users;
import com.url.shortner.backend.services.UrlMappingService;
import com.url.shortner.backend.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.Role;
import java.security.Principal;
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
    public ResponseEntity<UrlMappingDTO> createShortUrl(@RequestBody Map<String,String> request, Principal principal)
    {
        String orignalUrl = request.get("originalUrl");
        Users user = userService.findByUsername(principal.getName());
        UrlMappingDTO urlMappingDTO = urlMappingService.createShortUrl(orignalUrl, user);
        return ResponseEntity.ok(urlMappingDTO);
    }
}
