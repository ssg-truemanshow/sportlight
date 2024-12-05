package com.tms.sportlight.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class HostInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "host_id")
  private Integer id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "host_bio")
  private String bio;

  @Column(name = "host_instar")
  private String instar;

  @Column(name = "host_facebook")
  private String facebook;

  @Column(name = "host_twitter")
  private String twitter;

  @Column(name = "host_youtube")
  private String youtube;

  public static HostInfo initHostInfo(User user) {
    return HostInfo.builder()
        .user(user)
        .bio("")
        .instar("")
        .facebook("")
        .twitter("")
        .youtube("")
        .build();
  }

  public void update(String bio, String instar, String facebook, String twitter, String youtube) {
    if(!bio.isEmpty()) {
      this.bio = bio;
    }
    this.instar = instar;
    this.facebook = facebook;
    this.twitter = twitter;
    this.youtube = youtube;
  }

}
