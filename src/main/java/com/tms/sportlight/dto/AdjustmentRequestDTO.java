package com.tms.sportlight.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
public class AdjustmentRequestDTO {

    @Min(value = 10000)
    private double requestAmount;
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
