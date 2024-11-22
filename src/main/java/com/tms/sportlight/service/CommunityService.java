package com.tms.sportlight.service;

import com.tms.sportlight.domain.*;
import com.tms.sportlight.dto.*;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.CommunityRepository;
import com.tms.sportlight.repository.UserCommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CategoryService categoryService;
    private final FileService fileService;
    private final UserCommunityRepository userCommunityRepository;


    /**
     * 커뮤니티 단건 조회
     *
     * @param id 커뮤니티 id
     * @return 커뮤니티 엔티티
     */
    @Transactional(readOnly = true)
    public Community getCommunity(int id) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_COMMUNITY));
        if(community.isDeleted()) {
            throw new BizException(ErrorCode.NOT_FOUND_COMMUNITY);
        }
        return community;
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
            userCommunityRepository.deleteByCommunityId(community.getId());
            community.delete();
        } else {
            throw new BizException(ErrorCode.EXIST_TWO_OR_MORE_PARTICIPANTS);
        }
    }

   /* public CommunityDetailDTO getCommunityDetail(int id) {
        return communityRepository.getCommunityDetail(id)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_COMMUNITY));
    }*/

    /**
     * 커뮤니티 리스트 조회
     * 지도 반경 내
     * @param pageRequestDTO
     * @return
     */
    public List<CommunityListDTO> getCommunityList(PageRequestDTO<CommunitySearchDTO> pageRequestDTO) {
        CommunitySearchDTO communitySearchDTO = pageRequestDTO.getSearchCond();
        if(communitySearchDTO == null) {
            throw new BizException(ErrorCode.INVALID_INPUT_VALUE);
        }
        if(communitySearchDTO.getSortType() == null || SortType.POPULARITY.equals(communitySearchDTO.getSortType())) {
            return communityRepository.getCommunityListByCapacity(pageRequestDTO);
        } else if (SortType.DISTANCE.equals(communitySearchDTO.getSortType())){
            return communityRepository.getListDTOListByDistance(pageRequestDTO);
        }
        return new ArrayList<>();
    }

    public int getCommunityCount(PageRequestDTO<CommunitySearchDTO> pageRequestDTO) {
        CommunitySearchDTO communitySearchDTO = pageRequestDTO.getSearchCond();
        if(communitySearchDTO == null) {
            throw new BizException(ErrorCode.INVALID_INPUT_VALUE);
        }
        return communityRepository.getCommunityCount(pageRequestDTO);
    }

    /**
     * 커뮤니티 참가자 수 조회
     *
     * @param communityId 커뮤니티 id
     * @return 커뮤니티 참가자 수
     */
    public int getParticipantCount(int communityId) {
        return userCommunityRepository.countByCommunityId(communityId);
    }

    /**
     * 회원이 커뮤니티 참가자인지 확인
     *
     * @param user 확인할 회원
     * @param communityId 커뮤니티 id
     * @return true: 참가자, false: 참가자가 아님
     */
    public boolean isCommunityParticipant(User user, int communityId) {
        return userCommunityRepository.existsByUserIdAndCommunityId(user.getId(), communityId);
    }

    /**
     * 회원이 커뮤니티 참가자인지 확인
     *
     * @param username 확인할 회원
     * @param communityId 커뮤니티 id
     * @return true: 참가자, false: 참가자가 아님
     */
    public boolean isCommunityParticipant(String username, int communityId) {
        return userCommunityRepository.existsByUsernameAndCommunityId(username, communityId);
    }

    /**
     * 커뮤니티 참가자 리스트 조회
     *
     * @param communityId 커뮤니티 id
     * @return 커뮤니티 참가자 리스트
     */
    public List<CommunityParticipant> getCommunityParticipantList(int communityId) {
        return userCommunityRepository.findByCommunityId(communityId);
    }

    /**
     * 회원의 커뮤니티 입장 처리
     * 1. 커뮤니티가 존재하는지
     * 2. 회원이 이미 커뮤니티의 참가자인지
     * 3. 커뮤니티 정원이 초과하지 않는지
     *
     * @param communityId 입장 처리할 커뮤니티 id
     * @param user 입장할 회원
     */
    public void enterCommunity(int communityId, User user) {
        Community community = getCommunity(communityId);
        if(isCommunityParticipant(user, communityId)) {
            throw new BizException(ErrorCode.ALREADY_EXISTS_PARTICIPANT);
        } else if(community.getMaxCapacity() <= getParticipantCount(communityId)) {
            throw new BizException(ErrorCode.EXCEED_COMMUNITY_MAX_CAPACITY);
        }
        userCommunityRepository.save(new CommunityParticipant(user, community, LocalDateTime.now()));
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
        userCommunityRepository.deleteByUserIdAndCommunityId(user.getId(), communityId);
    }

    public void kickParticipant(int communityId, int participantUserId, User user) {
        Community community = getCommunity(communityId);
        if(!verifyCommunityCreator(community, user)) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        } else if(!userCommunityRepository.existsByUserIdAndCommunityId(participantUserId, communityId)) {
            throw new BizException(ErrorCode.NOT_COMMUNITY_PARTICIPANT);
        }
        userCommunityRepository.deleteByUserIdAndCommunityId(participantUserId, communityId);
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
