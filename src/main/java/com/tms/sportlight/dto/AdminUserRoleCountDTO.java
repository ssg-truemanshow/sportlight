package com.tms.sportlight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserRoleCountDTO {
    private Long userAndHostRoleCount;
    private Long userRoleOnlyCount;
}
