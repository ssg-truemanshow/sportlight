package com.tms.sportlight.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

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
public class Event {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "event_id")
  private Integer id;

  private String name;
  private String content;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private String classLink;
  private LocalDateTime regDate;

  @Column(name = "coupon_num")
  private Integer num;
  @Column(name = "remained_coupon_num")
  private Integer remainedNum;
  private boolean status;

  @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Coupon> coupons;

  public void updateEvent(String name, String classLink, Integer num) {
    if (name != null) {
      this.name = name;
    }
    if (classLink != null) {
      this.classLink = classLink;
    }
    if (num != null) {
      this.num = num;
    }
  }

  public void setRemainedNum(int i) {
    this.remainedNum = i;
  }
}
