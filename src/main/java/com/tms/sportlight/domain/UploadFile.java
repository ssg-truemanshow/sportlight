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
    @Column(name = "file_id")
    private Integer id;

    @Column(name = "file_type")
    private FileType type;

    @Column(name = "file_identifier")
    private int identifier;

    private String storeName;
    private String origName;

    @Column(name = "file_path")
    private String path;

    private LocalDateTime regDate;

    private boolean deleted;

    public void delete() {
        this.deleted = true;
    }
}
