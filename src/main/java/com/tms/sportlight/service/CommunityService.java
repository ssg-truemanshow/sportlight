package com.tms.sportlight.service;

import com.tms.sportlight.domain.Category;
import com.tms.sportlight.domain.Community;
import com.tms.sportlight.domain.User;
import com.tms.sportlight.domain.UserRole;
import com.tms.sportlight.dto.CommunityCreateDTO;
import com.tms.sportlight.dto.CommunityListDTO;
import com.tms.sportlight.dto.CommunityUpdateDTO;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.CommunityRepository;
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

    @Transactional(readOnly = true)
    public Community getCommunity(int id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_COMMUNITY));
    }

    public int save(User user, CommunityCreateDTO createDTO) {
        if(!user.getRoles().contains(UserRole.COMMUNITY_CREATOR)) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        Category category = categoryService.get(createDTO.getCategoryId());
        int id = communityRepository.save(createDTO.toEntity(user, category));
        fileService.saveCommunityIconFile(id, createDTO.getIconImage());
        return id;
    }

    public void updateCommunity(int id, User user, CommunityUpdateDTO updateDTO) {
        Community community = getCommunity(id);
        verifyCommunityCreator(community, user);
        // TODO: maxCapacity 검증 로직
        community.update(updateDTO.getTitle(), updateDTO.getDescription(), updateDTO.getMaxCapacity(),
                updateDTO.getLatitude(), updateDTO.getLongitude(), updateDTO.getAddress(), updateDTO.getDetailAddress());
    }

    public void deleteCommunity(int id, User user) {
        Community community = getCommunity(id);
        verifyCommunityCreator(community, user);
        // TODO: 현재 참가자 == 1 이고 그게 본인인 검증 로직
        community.delete();
    }

    public List<CommunityListDTO> getCommunityListDTOList() {
        return communityRepository.findListDTOList();
    }

    private void verifyCommunityCreator(Community community, User user) {
        try {
            if(!Objects.equals(community.getUser().getId(), user.getId())) {
                throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
            }
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
