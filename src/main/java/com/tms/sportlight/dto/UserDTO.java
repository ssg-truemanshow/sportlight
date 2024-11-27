package com.tms.sportlight.dto;

import com.tms.sportlight.domain.UploadFile;
import com.tms.sportlight.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long userId;
    private String userImage;
    private String loginId;
    private String userNickname;
    private String userIntroduce;
    private String userName;
    private String userPhone;
    private Boolean marketingAgreement;
    private Boolean personalAgreement;

}
