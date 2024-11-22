package com.tms.sportlight.dto;

import com.tms.sportlight.domain.Community;
import jakarta.annotation.security.DenyAll;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommunityListDTO {

    private int id;
    private int categoryId;
    private String categoryName;
    private String title;
    private String creatorName;
    private int currentCapacity;
    private int maxCapacity;
    private String communityImg;
    private String address;
    private double latitude;
    private double longitude;
    private double distance;
    private LocalDateTime regDate;

}
