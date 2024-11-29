package com.tms.sportlight.dto;

import com.tms.sportlight.domain.UploadFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FileDTO {

    private int id;
    private String origName;
    private String storeName;
    private String path;

    public static FileDTO from(UploadFile file) {
        return FileDTO.builder()
                .id(file.getId())
                .storeName(file.getStoreName())
                .origName(file.getOrigName())
                .path(file.getPath())
                .build();
    }
}
