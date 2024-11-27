package com.tms.sportlight.dto;

import com.tms.sportlight.domain.Course;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageDTO {
    private UserDTO userInfo;
/*    private int couponCount;
    private int reviewCount;
    private int interestCount;
    private int courseCount;*/

/*    public static MyPageDTO createWithDefaultValues(UserDTO userInfo) {
        return MyPageDTO.builder()
            .userInfo(userInfo)
            .couponCount(0)
            .reviewCount(0)
            .interestCount(0)
            .courseCount(0)
            .build();
    }*/
}
