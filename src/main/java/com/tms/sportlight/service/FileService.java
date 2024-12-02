package com.tms.sportlight.service;

import com.tms.sportlight.domain.FileType;
import com.tms.sportlight.domain.UploadFile;
import com.tms.sportlight.dto.FileDTO;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.FileRepository;
import com.tms.sportlight.util.FileStore;
import com.tms.sportlight.util.FileValidator;
import com.tms.sportlight.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileStore fileStore;
    private final FileValidator fileValidator;
    private final ImageUtil imageUtil;

    public UploadFile getRecentFile(FileType type, int identifier) {
        return fileRepository.findRecentFile(type, identifier).orElse(null);
    }

    /**
     * 파일 메타 데이터 리스트 조회
     *
     * @param type 파일 타입
     * @param identifier 파일 타입별 식별자
     * @return 파일 메타 데이터
     */
    public List<FileDTO> getFileList(FileType type, int identifier) {
        return fileRepository.findByFileTypeAndFileIdentifier(type, identifier)
                .stream()
                .map(FileDTO::from)
                .toList();
    }

    /**
     * 클래스 메인 이미지 파일 업로드
     *
     * @param courseId 클래스 id
     * @param file 클래스 메인 이미지
     */
    public void saveCourseMainImageFile(int courseId, MultipartFile file) {
        if(file == null || !fileValidator.isValidImageFile(file)) {
            return;
        }
        List<UploadFile> fileList = fileRepository.findByFileTypeAndFileIdentifier(FileType.COURSE_THUMB, courseId);
        fileList.forEach(UploadFile::delete);
        uploadThumbFile(file, FileType.COURSE_THUMB, courseId);
    }

    /**
     * 클래스 설명 이미지 파일 업로드
     *
     * @param courseId 클래스 id
     * @param files 클래스 이미지 파일 리스트
     */
    public void saveCourseImageFiles(int courseId, List<MultipartFile> files) {
        if(files == null || files.isEmpty()) {
            return;
        }
        for (MultipartFile file : files) {
            if (!fileValidator.isValidImageFile(file)) {
                continue;
            }
            uploadFile(file, FileType.COURSE_IMG, courseId);
        }
    }
    
    public String getUserIconFile(int userId) {
        try {
            return fileRepository.findRecentUserFile(FileType.USER_PROFILE_ICON, userId)
                .map(UploadFile::getPath)
                .orElse(null);
        } catch (Exception e) {
            log.error("Failed to get user icon file for userId: " + userId, e);
            return null;
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
        List<UploadFile> fileList = fileRepository.findByFileTypeAndFileIdentifier(FileType.USER_PROFILE_ICON, userId);
        fileList.forEach(UploadFile::delete);
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
        List<UploadFile> fileList = fileRepository.findByFileTypeAndFileIdentifier(FileType.COMMUNITY_PROFILE_ICON, communityId);
        fileList.forEach(UploadFile::delete);
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
        String origName = fileValidator.getValidFileName(sourceFile.getOriginalFilename());
        String storeName = generateFileName(type.getPath(), origName);
        byte[] thumbnail = imageUtil.createThumbImg(sourceFile);
        String path = fileStore.putFileToBucket(thumbnail, storeName);
        UploadFile uploadFile = UploadFile.builder()
                .type(type)
                .identifier(identifier)
                .storeName(storeName)
                .origName(origName)
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
        String origName = fileValidator.getValidFileName(sourceFile.getOriginalFilename());
        String storeName = generateFileName(type.getPath(), origName);
        byte[] icon = imageUtil.createIconImg(sourceFile);
        String path = fileStore.putFileToBucket(icon, storeName);
        UploadFile uploadFile = UploadFile.builder()
                .type(type)
                .identifier(identifier)
                .storeName(storeName)
                .origName(origName)
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
        String origName = fileValidator.getValidFileName(file.getOriginalFilename());
        String storeName = generateFileName(type.getPath(), origName);
        String path = fileStore.putFileToBucket(file, storeName);
        UploadFile uploadFile = UploadFile.builder()
                .type(type)
                .identifier(identifier)
                .storeName(storeName)
                .origName(origName)
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
     * @param path 경로
     * @param originalFilename 원 파일명
     * @return 파일 이름
     */
    private String generateFileName(String path, String originalFilename) {
        String ext = extracted(originalFilename);
        String name;
        if(StringUtils.isEmpty(ext)) {
            name = UUID.randomUUID().toString();
        } else {
            name = UUID.randomUUID() + "." + ext;
        }
        return path + name;
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

    /**
     * 파일 정보 삭제
     *
     * @param id 삭제할 파일 id
     */
    public void deleteFile(int id) {
        UploadFile uploadFile = get(id);
        uploadFile.delete();
    }

    private UploadFile get(int id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_FILE));
    }
}
