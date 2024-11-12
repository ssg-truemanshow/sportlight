package com.tms.sportlight.service;

import com.tms.sportlight.domain.FileType;
import com.tms.sportlight.domain.UploadFile;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.FileRepository;
import com.tms.sportlight.util.FileStore;
import com.tms.sportlight.util.FileValidator;
import com.tms.sportlight.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.apache.tika.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileStore fileStore;
    private final FileValidator fileValidator;
    private final ImageUtil imageUtil;

    /**
     * 클래스 이미지 파일 업로드
     *
     * @param courseId 클래스 id
     * @param files 클래스 이미지 파일 리스트
     */
    public void saveCourseImageFiles(int courseId, List<MultipartFile> files) {
        if(files == null || files.isEmpty()) {
            return;
        } else if(!fileValidator.isValidImageFile(files.get(0))) {
            throw new BizException(ErrorCode.INVALID_INPUT_FILE);
        }
        uploadThumbFile(files.get(0), FileType.COURSE_THUMB, courseId);
        for(int i = 1; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            if(!fileValidator.isValidImageFile(file)) {
                continue;
            }
            uploadFile(file, FileType.COURSE_IMG, courseId);
        }
    }

    /**
     * 회원 프로필 아이콘 파일 업로드
     *
     * @param userId 회원 id
     * @param file 프로필 이미지 파일
     */
    public void saveUserIconFile(int userId, MultipartFile file) {
        if (file == null || !fileValidator.isValidImageFile(file)) {
            return;
        }
        uploadIconFile(file, FileType.USER_PROFILE_ICON, userId);
    }

    /**
     * 커뮤니티 프로필 아이콘 파일 업로드
     *
     * @param communityId 커뮤니티 id
     * @param file 프로필 이미지 파일
     */
    public void saveCommunityIconFile(int communityId, MultipartFile file) {
        if (file == null || !fileValidator.isValidImageFile(file)) {
            return;
        }
        uploadIconFile(file, FileType.COMMUNITY_PROFILE_ICON, communityId);
    }

    /**
     * 강사 자격 인증 파일 업로드
     *
     * @param requestId 강자 전환 요청 id
     * @param files 인증 파일 리스트
     */
    public void saveHostCertificationFiles(int requestId, List<MultipartFile> files) {
        if(files == null || files.isEmpty()) {
            return;
        }
        for(MultipartFile file : files) {
            if(!fileValidator.isValidAttachFile(file)) {
                continue;
            }
            uploadFile(file, FileType.HOST_CERTIFICATION_FILE, requestId);
        }
    }

    /**
     * 소스 파일에 대한 썸네일 이미지 생성, 업로드, 파일 정보 엔티티 저장
     *
     * @param sourceFile 원본 이미지 파일
     * @param type 파일 타입
     * @param identifier 파일 식별자
     */
    private void uploadThumbFile(MultipartFile sourceFile, FileType type, int identifier) {
        String storeName = generateFileName(sourceFile.getOriginalFilename());
        byte[] thumbnail = imageUtil.createThumbImg(sourceFile);
        String path = fileStore.putFileToBucket(thumbnail, storeName);
        UploadFile uploadFile = UploadFile.builder()
                .type(type)
                .identifier(identifier)
                .name(storeName)
                .path(path)
                .deleted(false)
                .regDate(LocalDateTime.now())
                .build();
        fileRepository.save(uploadFile);
    }

    /**
     * 소스 파일에 대한 아이콘 이미지 생성, 업로드, 파일 정보 엔티티 저장
     *
     * @param sourceFile 원본 이미지 파일
     * @param type 파일 타입
     * @param identifier 파일 식별자
     */
    private void uploadIconFile(MultipartFile sourceFile, FileType type, int identifier) {
        String storeName = generateFileName(sourceFile.getOriginalFilename());
        byte[] icon = imageUtil.createIconImg(sourceFile);
        String path = fileStore.putFileToBucket(icon, storeName);
        UploadFile uploadFile = UploadFile.builder()
                .type(type)
                .identifier(identifier)
                .name(storeName)
                .path(path)
                .deleted(false)
                .regDate(LocalDateTime.now())
                .build();
        fileRepository.save(uploadFile);
    }

    /**
     * 파일 업로드, 파일 정보 엔티티 저장
     *
     * @param file 업로드 파일
     * @param type 파일 타입
     * @param identifier 파일 식별자
     */
    private void uploadFile(MultipartFile file, FileType type, int identifier) {
        String storeName = generateFileName(file.getOriginalFilename());
        String path = fileStore.putFileToBucket(file, storeName);
        UploadFile uploadFile = UploadFile.builder()
                .type(type)
                .identifier(identifier)
                .name(storeName)
                .path(path)
                .deleted(false)
                .regDate(LocalDateTime.now())
                .build();
        fileRepository.save(uploadFile);
    }

    /**
     * 스토리지에 저장될 이름 생성
     * 랜덤하게 생성한 UUID 사용
     *
     * @param originalFilename 원 파일명
     * @return 파일 이름
     */
    private String generateFileName(String originalFilename) {
        String ext = extracted(originalFilename);
        if(StringUtils.isEmpty(ext)) {
            return UUID.randomUUID().toString();
        }
        return UUID.randomUUID() + "," + ext;
    }

    /**
     * 파일 이름에서 확장자(ext) 추출
     * 파일명이 null 이거나 빈 문자열인 경우, '.'이 없는 경우 null 반환
     * '.' 위치 기반으로 확장자 추출
     *
     * @param fileName 파일 이름
     * @return 추출한 확장자 or null
     */
    private String extracted(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return null;
        }
        int pos = fileName.lastIndexOf(".");
        if (pos == -1) {
            return null;
        }
        return fileName.substring(pos + 1);
    }

}
