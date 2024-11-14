package com.tms.sportlight.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FileStore {

    @Value("${ncp.storage.bucket}")
    private String bucketName;
    private final AmazonS3Client objectStorageClient;

    /**
     * NCP Object Storage에 파일 업로드
     *
     * @param file 업로드할 멀티파트 파일
     * @param fileName 저장할 파일명
     * @return 파일 접근 경로
     */
    public String putFileToBucket(MultipartFile file, String fileName) {
        ObjectMetadata objectMetadata = generateObjectMetadata(file);
        try {
            PutObjectRequest request = new PutObjectRequest(bucketName, fileName, file.getInputStream(), objectMetadata);
            objectStorageClient.putObject(request);
        } catch (IOException e) {
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return objectStorageClient.getUrl(bucketName, fileName).toString();
    }

    /**
     * NCP Object Storage에 파일 업로드
     *
     * @param file 업로드할 byte array
     * @param fileName 저장할 파일명
     * @return 파일 접근 경로
     */
    public String putFileToBucket(byte[] file, String fileName) {
        ObjectMetadata objectMetadata = generateObjectMetadata(file);
        try(ByteArrayInputStream bis = new ByteArrayInputStream(file)) {
            PutObjectRequest request = new PutObjectRequest(bucketName, fileName, bis, objectMetadata);
            objectStorageClient.putObject(request);
        } catch (IOException e) {
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return objectStorageClient.getUrl(bucketName, fileName).toString();
    }

    public void deleteFileFromBucket(String fileName) {
        DeleteObjectRequest request = new DeleteObjectRequest(bucketName, fileName);
        objectStorageClient.deleteObject(request);
    }

    private ObjectMetadata generateObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        return objectMetadata;
    }

    private ObjectMetadata generateObjectMetadata(byte[] file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.length);
        return objectMetadata;
    }
}
