package com.tms.sportlight.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserInterestId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "category_id")
    private Integer categoryId;
}

