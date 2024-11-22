package com.tms.sportlight.service;

import com.tms.sportlight.domain.FileType;
import com.tms.sportlight.domain.HostRequest;
import com.tms.sportlight.domain.HostRequestStatus;
import com.tms.sportlight.domain.User;
import com.tms.sportlight.dto.HostRequestCheckDTO;
import com.tms.sportlight.dto.HostRequestDTO;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.FileRepository;
import com.tms.sportlight.repository.HostRequestRepository;
import com.tms.sportlight.util.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 강사 요청 관련 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
public class HostRequestService {

    private final HostRequestRepository hostRequestRepository;
    private final FileRepository fileRepository;
    private final FileService fileService;
    private final UserRoleService userRoleService;

    /**
     * 강사 신청서 등록 (회원 전용)
     *
     * @param user               신청 사용자
     * @param requestDTO         강사 신청 정보
     * @param certificationFiles 인증 파일 목록
     */
    @Transactional
    public void registerHostRequest(User user, HostRequestDTO requestDTO,
        List<MultipartFile> certificationFiles) {
        if (hostRequestRepository.findByUser(user).isPresent()) {
            throw new BizException(ErrorCode.DUPLICATE_HOST_REQUEST);
        }

        HostRequest hostRequest = requestDTO.toEntity(user, null);
        HostRequest savedRequest = hostRequestRepository.save(hostRequest);

        fileService.saveHostCertificationFiles(savedRequest.getId(), certificationFiles);

        //아이디를 내보내서 컨트롤러에서 생성한 엔티티에 아이디를 내보내고 프론트에서 그 아이디를 받은 다음에 리다이렉트
    }

    /**
     * 강사 신청서 수정 (회원 전용)
     *
     * @param user               신청 사용자
     * @param requestDTO         수정할 강사 신청 정보
     * @param certificationFiles 인증 파일 목록
     */
    /*@Transactional
    public void updateHostRequest(User user, Integer requestId, HostRequestDTO requestDTO,
        List<MultipartFile> certificationFiles) {
        HostRequest hostRequest = hostRequestRepository.findById(requestId)
            .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_REQUEST));

        if (!hostRequest.getUser().getId().equals(user.getId())) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        String certificationPath = hostRequest.getCertification();
        if (certificationFiles != null && !certificationFiles.isEmpty()) {
            certificationPath = fileService.saveHostCertificationFiles(requestId,
                certificationFiles);
        }

        hostRequest.updateHostRequest(
            requestDTO.getHostBio(),
            certificationPath,
            requestDTO.getPortfolio(),
            hostRequest.getReqStatus()
        );
    }*/


    @Transactional
    public void updateHostRequest(User user, HostRequestDTO requestDTO, List<MultipartFile> files) {
        HostRequest hostRequest = hostRequestRepository.findByUser(user)
            .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_REQUEST));

        if (files != null && !files.isEmpty()) {
            fileService.saveHostCertificationFiles(hostRequest.getId(), files);
        }

    }

    /**
     * 강사 요청 삭제
     *
     * @param user 요청자
     * @throws BizException 강사 요청이 없는 경우
     */
    @Transactional
    public void deleteHostRequest(User user) {
        HostRequest hostRequest = hostRequestRepository.findByUser(user)
            .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_REQUEST));

        hostRequestRepository.delete(hostRequest);
    }


}
