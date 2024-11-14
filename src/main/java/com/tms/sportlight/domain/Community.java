package com.tms.sportlight.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "community_title")
    private String title;

    @Column(name = "community_description")
    private String description;

    private int maxCapacity;
    private double latitude;
    private double longitude;
    private String address;
    private String detailAddress;
    private LocalDateTime regDate;
    private LocalDateTime delDate;
    private boolean deleted;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<CommunityParticipant> participants;

    public void update(String title, String description, int maxCapacity, double latitude, double longitude, String address, String detailAddress) {
        if(Objects.nonNull(title)) {
            this.title = title;
        }
        if(Objects.nonNull(description)) {
            this.description = description;
        }
        this.maxCapacity = maxCapacity;
        this.latitude = latitude;
        this.longitude = longitude;
        if(Objects.nonNull(address)) {
            this.address = address;
        }
        if(Objects.nonNull(detailAddress)) {
            this.detailAddress = detailAddress;
        }
    }

    public void delete() {
        this.deleted = true;
        this.delDate = LocalDateTime.now();
    }

}
