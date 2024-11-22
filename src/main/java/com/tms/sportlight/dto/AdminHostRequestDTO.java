package com.tms.sportlight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminHostRequestDTO {
    private Integer hostRequestId;
    private String loginId;
    private String userNickname;
    private String userName;
    private String reqStatus;
    private String hostBio;
    private String certification;
    private String portfolio;
    private String regDate;
}
