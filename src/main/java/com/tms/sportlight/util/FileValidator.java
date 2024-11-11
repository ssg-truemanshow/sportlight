package com.tms.sportlight.util;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FileValidator {
    @Value("${file.allowed-mime-types}")
    private String[] ALLOWED_MIME_TYPES;
    @Value("${file.maximum-filename-length}")
    private int MAXIMUM_FILENAME_LENGTH;

    /**
     * 첨부 파일에 대한 유효성 검증
     * 1. 유효한 파일인지
     * 2. 업로드 허용 MIME Type 인지
     *
     * @param file 검증할 MultipartFile
     * @return true: 유효한 파일, false: 유효하지 않은 파일
     */
    public boolean isValidAttachFile(MultipartFile file) {
        if (!isValidFile(file)) {
            return false;
        }
        String mimeType = detectMIMEType(file);
        if (!isAllowedMIMEType(mimeType)) {
            return false;
        }
        return true;
    }

    /**
     * 이미지 파일에 대한 유효성 검증
     * 1. 유효한 파일인지
     * 2. 업로드 허용 MIME Type 인지
     * 3. 이미지 MIME Type 인지
     *
     * @param file 검증할 MultipartFile
     * @return true: 유효한 이미지 파일, false: 유효하지 않은 파일
     */
    public boolean isValidImageFile(MultipartFile file) {
        if (!isValidFile(file)) {
            return false;
        }
        String mimeType = detectMIMEType(file);
        if (!isAllowedMIMEType(mimeType) || !isImageMimeType(mimeType)) {
            return false;
        }
        return true;
    }

    /**
     * 유효한 파일인지 확인
     * 1. null 체크
     * 2. empty 체크
     * 3. size > 0 체크
     * 4. 원본 파일이름 null 체크
     *
     * @param file 파일 폼 데이터
     * @return 유효한 파일인지 여부
     */
    private boolean isValidFile(MultipartFile file) {
        return file != null && !file.isEmpty() && file.getSize() > 0 && file.getOriginalFilename() != null;
    }

    /**
     * MIME Type이 이미지 타입인지 확인
     *
     * @param mimeType 확인할 mimeType
     * @return true: Image MIME Type, false: Image MIME Type 아님
     */
    private boolean isImageMimeType(String mimeType) {
        if (!StringUtils.hasText(mimeType)) {
            return false;
        }
        return mimeType.startsWith("image");
    }

    /**
     * 허용하는 MIME Type 인지 확인
     *
     * @param mimeType 확인할 mimeType
     * @return true: allowed MIME type, false: disallow MIME type
     */
    private boolean isAllowedMIMEType(String mimeType) {
        if (StringUtils.hasText(mimeType)) {
            for (String allowedMimeType : ALLOWED_MIME_TYPES) {
                if (mimeType.equals(allowedMimeType)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 파일의 MIME Type 판별
     * Tika 라이브러리 사용
     *
     * @param file MIME Type을 판별한 MultipartFile
     * @return 판별된 MIME Type
     */
    private String detectMIMEType(MultipartFile file) {
        Tika tika = new Tika();
        String mimeType;
        try (InputStream inputStream = file.getInputStream()) {
            mimeType = tika.detect(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return mimeType;
    }
}
