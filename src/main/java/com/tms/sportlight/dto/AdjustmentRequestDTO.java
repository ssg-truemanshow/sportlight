package com.tms.sportlight.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdjustmentRequestDTO {

    @NotNull
    @Positive
    private Double requestAmount;
    @NotEmpty
    @Size(max = 20)
    private String applicant;
    @NotEmpty
    @Size(max = 13)
    private String rrn;
    @NotEmpty
    @Size(max = 20)
    private String bankName;
    @NotEmpty
    @Size(max = 20)
    private String depositor;
    @NotEmpty
    @Size(max = 20)
    private String accountNumber;

}
