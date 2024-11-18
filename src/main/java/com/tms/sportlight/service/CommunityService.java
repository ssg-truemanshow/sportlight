package com.tms.sportlight.service;

import com.tms.sportlight.domain.*;
import com.tms.sportlight.dto.CommunityCreateDTO;
import com.tms.sportlight.dto.CommunityListDTO;
import com.tms.sportlight.dto.CommunityUpdateDTO;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.CommunityRepository;
import com.tms.sportlight.repository.JpaUserCommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CategoryService categoryService;
    private final FileService fileService;
    private final JpaUserCommunityRepository jpaUserCommunityRepository;


    /**
     * 커뮤니티 단건 조회
     *
     * @param id 커뮤니티 id
     * @return 커뮤니티 엔티티
     */
    @Transactional(readOnly = true)
    public Community getCommunity(int id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_COMMUNITY));
    }

    /**
     * 커뮤니티 개설
     *
     * @param user 개설자 회원
     * @param createDTO 커뮤니티 개설 DTO
     * @return 개설한 커뮤니티 id
     */
    public int save(User user, CommunityCreateDTO createDTO) {
        if(!user.getRoles().contains(UserRole.COMMUNITY_CREATOR)) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        Category category = categoryService.get(createDTO.getCategoryId());
        int id = communityRepository.save(createDTO.toEntity(user, category));
        fileService.saveCommunityIconFile(id, createDTO.getIconImage());
        return id;
    }

    /**
     * 커뮤니티 정보 업데이트
     *
     * @param id 커뮤니티 id
     * @param user 수정 회원
     * @param updateDTO 커뮤니티 수정 dto
     */
    public void updateCommunity(int id, User user, CommunityUpdateDTO updateDTO) {
        Community community = getCommunity(id);
        if(!verifyCommunityCreator(community, user)) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        } else if(getParticipantCount(id) > updateDTO.getMaxCapacity()) {
            throw new BizException(ErrorCode.EXCEED_COMMUNITY_MAX_CAPACITY);
        }
        community.update(updateDTO.getTitle(), updateDTO.getDescription(), updateDTO.getMaxCapacity(),
                updateDTO.getLatitude(), updateDTO.getLongitude(), updateDTO.getAddress(), updateDTO.getDetailAddress());
        if(updateDTO.getIconImage() != null) {
            fileService.saveCommunityIconFile(id, updateDTO.getIconImage());
        }
    }

    /**
     * 커뮤니티 삭제
     * 1. 커뮤니티 개설자인지
     * 2. 참가자가 본인 한명인지
     *
     * @param id 커뮤니티 id
     * @param user 삭제 회원
     */
    public void deleteCommunity(int id, User user) {
        Community community = getCommunity(id);
        if(!verifyCommunityCreator(community, user)) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        } else if(isTheOnlyParticipant(community.getId(), user)) {
            jpaUserCommunityRepository.deleteByCommunityId(community.getId());
            community.delete();
        } else {
            throw new BizException(ErrorCode.EXIST_TWO_OR_MORE_PARTICIPANTS);
        }
    }

    public List<CommunityListDTO> getCommunityListDTOList() {
        return communityRepository.findListDTOList();
    }

    /**
     * 커뮤니티 참가자 수 조회
     *
     * @param communityId 커뮤니티 id
     * @return 커뮤니티 참가자 수
     */
    public int getParticipantCount(int communityId) {
        return jpaUserCommunityRepository.countByCommunityId(communityId);
    }

    /**
     * 회원이 커뮤니티 참가자인지 확인
     *
     * @param user 확인할 회원
     * @param communityId 커뮤니티 id
     * @return true: 참가자, false: 참가자가 아님
     */
    public boolean isCommunityParticipant(User user, int communityId) {
        return jpaUserCommunityRepository.existsByUserIdAndCommunityId(user.getId(), communityId);
    }

    /**
     * 커뮤니티 참가자 리스트 조회
     *
     * @param communityId 커뮤니티 id
     * @return 커뮤니티 참가자 리스트
     */
    public List<CommunityParticipant> getCommunityParticipantList(int communityId) {
        return jpaUserCommunityRepository.findByCommunityId(communityId);
    }

    /**
     * 회원의 커뮤니티 입장 처리
     * 1. 커뮤니티가 존재하는지
     * 2. 커뮤니티 정원이 초과하지 않는지
     * 3. 회원이 이미 커뮤니티의 참가자인지
     *
     * @param communityId
     * @param user
     */
    public void enterCommunity(int communityId, User user) {
        Community community = getCommunity(communityId);
        if(community.getMaxCapacity() <= getParticipantCount(communityId)) {
            throw new BizException(ErrorCode.EXCEED_COMMUNITY_MAX_CAPACITY);
        } else if(isCommunityParticipant(user, communityId)) {
            throw new BizException(ErrorCode.ALREADY_EXISTS_PARTICIPANT);
        }
        jpaUserCommunityRepository.save(new CommunityParticipant(user, community));
    }

    /**
     * 회원의 커뮤니티 퇴장 처리
     * 1. 커뮤니티가 존재하는지
     * 2. 회원이 커뮤니티의 참가자인지
     * 3. 커뮤니티 개설자인 경우, 참가자가 본인 한명인지
     *
     * @param communityId 퇴장 처리할 커뮤니티 id
     * @param user 퇴장할 회원
     */
    public void exitCommunity(int communityId, User user) {
        Community community = getCommunity(communityId);
        if(!isCommunityParticipant(user, communityId)) {
            throw new BizException(ErrorCode.NOT_COMMUNITY_PARTICIPANT);
        }else if(verifyCommunityCreator(community, user) && !isTheOnlyParticipant(community.getId(), user)) {
            throw new BizException(ErrorCode.EXIST_TWO_OR_MORE_PARTICIPANTS);
        }
        jpaUserCommunityRepository.deleteByUserIdAndCommunityId(user.getId(), communityId);
    }

    private boolean verifyCommunityCreator(Community community, User user) {
        try {
            return !Objects.equals(community.getUser().getId(), user.getId());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private boolean isTheOnlyParticipant(int communityId, User user) {
        if(user == null || user.getId() == null) {
            return false;
        }
        List<CommunityParticipant> participantList = getCommunityParticipantList(communityId);
        return participantList.size() == 1 && participantList.get(0).getUser().getId().equals(user.getId());
    }

}
