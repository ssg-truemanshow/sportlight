package com.tms.sportlight.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    //프로필 이미지
    private MultipartFile userImage;

    @NotBlank
    @Size(min = 2, max = 15)
    private String userNickname;

    private String userIntroduce;

    private Boolean marketingAgreement;

    private Boolean personalAgreement;


}
