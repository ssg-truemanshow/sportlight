package com.tms.sportlight.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Entity
@Table(name = "notification")
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "notification_id", nullable = false)
  private long notificationId;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User userId;  //회원

  @Column(name = "noti_title", nullable = false)
  private String notiTitle;

  @Column(name = "noti_content", nullable = false)
  private String notiContent;

  @Column(name = "noti_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private NotiType notiType;  //이넘 수정 필요

  @Column(name = "noti_readornot", nullable = false)
  private boolean notiReadOrNot;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "noti_target_grade", nullable = false)
  @Enumerated(EnumType.STRING)
  private NotiGrade notiGrade;


  public void changeReadState() {
    this.notiReadOrNot = true;
  }
}
