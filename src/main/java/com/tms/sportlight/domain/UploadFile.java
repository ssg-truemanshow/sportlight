package com.tms.sportlight.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UploadFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "file_type")
    private FileType type;

    @Column(name = "file_identifier")
    private int identifier;

    @Column(name = "file_name")
    private String name;

    @Column(name = "file_path")
    private String path;

    private boolean deleted;
}
