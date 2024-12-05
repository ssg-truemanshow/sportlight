package com.tms.sportlight.controller;

import com.tms.sportlight.domain.UploadFile;
import com.tms.sportlight.dto.Id;
import com.tms.sportlight.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class DownloadController {

    private final FileService fileService;

    @GetMapping("/files/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable Id id) {
        UploadFile uploadFile = fileService.getUploadFile(id.getId());
        byte[] byteArray = fileService.loadFileAsBytes(uploadFile);
        String encodedFileName = UriUtils.encode(uploadFile.getOrigName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=" + encodedFileName;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(byteArray.length);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
        return new ResponseEntity<>(byteArray, headers, HttpStatus.OK);
    }
}
