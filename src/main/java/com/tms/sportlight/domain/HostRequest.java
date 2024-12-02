package com.tms.sportlight.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "host_req_id")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String hostBio;

    private String portfolio;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    private LocalDateTime rejDate;

    @Enumerated(EnumType.STRING)
    private HostRequestStatus reqStatus;

    @PrePersist
    public void onCreate() {
        this.regDate = LocalDateTime.now();
        this.reqStatus = HostRequestStatus.PENDING;
    }

    public void updateHostRequest(String hostBio, String portfolio, List<MultipartFile> certification) {
        if (hostBio != null) this.hostBio = hostBio;
        if (portfolio != null) this.portfolio = portfolio;

        if (this.reqStatus == HostRequestStatus.REJECTED) {
            this.reqStatus = HostRequestStatus.PENDING;
            this.rejDate = null;
        }
        this.modDate = LocalDateTime.now();
    }

    public void updateStatus(HostRequestStatus status) {
        if(Objects.nonNull(status)) {
            this.reqStatus = status;
        }
    }


}
