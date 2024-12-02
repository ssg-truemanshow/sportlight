package com.tms.sportlight.dto;

import com.tms.sportlight.domain.Course;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageDTO {
    private UserDTO userInfo;
    private int interestCount;
    private int reviewCount;
    private int couponCount;
    private int communityCount;
    private String hostRequestStatus;
}
