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

    private int categoryId;
    private String categoryName;
    private String title;
    private String creatorName;
    private String creatorImg;
    private int currentCapacity;
    private int maxCapacity;
    private String communityImg;
    private String address;
    private double latitude;
    private double longitude;
    private LocalDateTime regDate;

//    public CommunityListDTO fromEntity(Community community) {
//        return CommunityListDTO.builder()
//                .categoryId(community.getCategory().getId())
//                .categoryName(community.getCategory().getName())
//                .title(community.getTitle())
//                .creatorName(community.getUser().getUserNickname())
//                .creatorImg("")
//                .currentCapacity(community.getParticipants().size())
//                .maxCapacity(community.getMaxCapacity())
//                .communityImg("")
//                .address(community.getAddress())
//                .latitude(community.getLatitude())
//                .longitude(community.getLongitude())
//                .regDate(community.getRegDate())
//                .build();
//    }
}
