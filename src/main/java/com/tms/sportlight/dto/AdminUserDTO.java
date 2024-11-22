package com.tms.sportlight.dto;

import com.tms.sportlight.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDTO {
    private Long id;
    private String loginId;
    private String userName;
    private String userGender;
    private UserRole role;
    private LocalDateTime regDate;
}
