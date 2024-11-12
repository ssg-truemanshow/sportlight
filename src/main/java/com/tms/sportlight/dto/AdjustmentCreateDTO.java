package com.tms.sportlight.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdjustmentCreateDTO {

    private double requestAmount;
    private String applicant;
    private String rrn;
    private String bankName;
    private String depositor;
    private String accountNumber;

}
