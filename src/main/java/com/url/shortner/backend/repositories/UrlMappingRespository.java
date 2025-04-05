package com.url.shortner.backend.repositories;
import com.url.shortner.backend.dto.UrlMappingDTO;
import com.url.shortner.backend.models.UrlMapping;
import com.url.shortner.backend.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlMappingRespository extends JpaRepository<UrlMapping, Long> {

    UrlMapping findByShortUrl(String shortUrl);
    List<UrlMapping> findByUser(Users user);
}
