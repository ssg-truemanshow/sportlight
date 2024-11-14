package com.tms.sportlight.dto;

import com.tms.sportlight.domain.Category;
import com.tms.sportlight.domain.Community;
import com.tms.sportlight.domain.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CommunityCreateDTO {

    @Positive
    private int categoryId;
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

    public Community toEntity(User user, Category category) {
        return Community.builder()
                .user(user)
                .category(category)
                .title(title)
                .description(description)
                .maxCapacity(maxCapacity)
                .latitude(latitude)
                .longitude(longitude)
                .address(address)
                .detailAddress(detailAddress)
                .regDate(LocalDateTime.now())
                .deleted(false)
                .build();
    }
}
