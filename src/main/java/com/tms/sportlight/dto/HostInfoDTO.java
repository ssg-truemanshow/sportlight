package com.tms.sportlight.dto;

import com.tms.sportlight.domain.HostInfo;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HostInfoDTO {

    private String userNickname;
    @NotNull
    private String hostBio;
    private String hostInsta;
    private String hostFacebook;
    private String hostTwitter;
    private String hostYoutube;

    public static HostInfoDTO from(HostInfo hostInfo) {
        return HostInfoDTO.builder()
                .userNickname(hostInfo.getUser().getUserNickname())
                .hostBio(hostInfo.getBio())
                .hostInsta(hostInfo.getInstar())
                .hostFacebook(hostInfo.getFacebook())
                .hostTwitter(hostInfo.getTwitter())
                .hostYoutube(hostInfo.getYoutube())
                .build();
    }
}
