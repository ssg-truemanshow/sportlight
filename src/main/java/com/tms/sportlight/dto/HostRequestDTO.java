package com.tms.sportlight.dto;

import com.tms.sportlight.domain.HostRequest;
import com.tms.sportlight.domain.HostRequestStatus;
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
public class HostRequestDTO {

    private String hostBio;

    private String certification;

    private String portfolio;

    private HostRequestStatus reqStatus;

    public HostRequest toEntity(User user, String certification) {
        return HostRequest.builder()
            .user(user)
            .hostBio(this.hostBio)
            .certification(certification) // 실제 파일 경로를 저장
            .portfolio(this.portfolio)
            .reqStatus(reqStatus != null ? this.reqStatus : HostRequestStatus.PENDING)
            .build();
    }

}
