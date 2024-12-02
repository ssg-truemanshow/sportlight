package com.tms.sportlight.dto;

import com.tms.sportlight.domain.HostRequest;
import com.tms.sportlight.domain.HostRequestStatus;
import com.tms.sportlight.domain.UploadFile;
import com.tms.sportlight.domain.User;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostRequestDTO {

    private String hostBio;

    private List<MultipartFile> certification;

    private String portfolio;

    private HostRequestStatus reqStatus;

    public HostRequest toEntity(User user, String certification) {
        return HostRequest.builder()
            .user(user)
            .hostBio(this.hostBio)
            .portfolio(this.portfolio)
            .reqStatus(reqStatus != null ? this.reqStatus : HostRequestStatus.PENDING)
            .build();
    }

}
