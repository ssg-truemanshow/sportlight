package com.tms.sportlight.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

//    @OneToMany(mappedBy = "user", orphanRemoval = true)
//    private List<CommunityParticipant> participants;

}
