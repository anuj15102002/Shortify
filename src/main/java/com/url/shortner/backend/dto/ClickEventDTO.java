package com.url.shortner.backend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ClickEventDTO {
    LocalDate createdDate;
    Long clickCount;
}
