package com.tms.sportlight.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MonthlySale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "monthly_sale_id")
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime dsdate;

    @Column(nullable = false, precision = 10, scale = 2)
    private Double totalAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private Double vat;

    @Column(nullable = false, precision = 10, scale = 2)
    private Double additionalRevenue;

    @Column(nullable = false, precision = 10, scale = 2)
    private Double netRevenue;
}
