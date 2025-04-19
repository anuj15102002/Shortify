package com.url.shortner.backend.security.JWT.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${frontendUrl}")
    private String frontendUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(frontendUrl)
                .allowedMethods("GET","POST","UPDATE","DELETE","OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }




}
