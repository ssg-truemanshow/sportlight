package com.tms.sportlight.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class CommunityUpdateDTO {

    @NotEmpty
    @Size(min = 1, max = 50)
    private String title;
    @NotEmpty
    @Size(max = 5000)
    private String description;
    @Size(min = 2, max = 100)
    private int maxCapacity;
    @DecimalMin("33.0")
    @DecimalMax("44.0")
    private double latitude;
    @DecimalMin("124.0")
    @DecimalMax("133.0")
    private double longitude;
    @NotEmpty
    @Size(min = 1, max = 255)
    private String address;
    @Size(min = 1, max = 255)
    private String detailAddress;
    private MultipartFile iconImage;
}
