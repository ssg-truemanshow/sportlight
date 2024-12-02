package com.tms.sportlight.dto;

import com.tms.sportlight.domain.HostRequest;
import com.tms.sportlight.domain.HostRequestStatus;
import com.tms.sportlight.domain.UploadFile;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostRequestCheckDTO {

    private HostRequestStatus reqStatus;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private LocalDateTime rejDate;

    public static HostRequestCheckDTO fromEntity(HostRequest hostRequest) {
        return HostRequestCheckDTO.builder()
            .reqStatus(hostRequest.getReqStatus())
            .regDate(hostRequest.getRegDate())
            .modDate(hostRequest.getModDate())
            .rejDate(hostRequest.getRejDate())
            .build();
    }

}
