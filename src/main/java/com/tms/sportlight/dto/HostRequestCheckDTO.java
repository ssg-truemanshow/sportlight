package com.tms.sportlight.dto;

import com.tms.sportlight.domain.HostRequest;
import com.tms.sportlight.domain.HostRequestStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostRequestCheckDTO {

    private Integer id;
    private String hostBio;
    private String certification; // 저장된 파일 경로 반환
    private String portfolio;
    private HostRequestStatus reqStatus;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private LocalDateTime rejDate;

    public static HostRequestCheckDTO fromEntity(HostRequest hostRequest) {
        return HostRequestCheckDTO.builder()
            .id(hostRequest.getId())
            .hostBio(hostRequest.getHostBio())
            .certification(hostRequest.getCertification())
            .portfolio(hostRequest.getPortfolio())
            .reqStatus(hostRequest.getReqStatus())
            .regDate(hostRequest.getRegDate())
            .modDate(hostRequest.getModDate())
            .rejDate(hostRequest.getRejDate())
            .build();
    }

}
