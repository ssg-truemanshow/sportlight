package com.tms.sportlight.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Adjustment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private double requestAmount;
    private double totalAmount;
    private String applicant;
    private String rrn;
    private String bankName;
    private String depositor;
    private String accountNumber;
    private LocalDateTime reqDate;
    private LocalDateTime approvedDate;
    private double adjustedCharge;
    private double vat;
    private double withholdingTax;
    private AdjustmentStatus status;
}
