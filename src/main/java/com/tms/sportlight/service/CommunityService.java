package com.tms.sportlight.service;

import com.tms.sportlight.domain.Community;
import com.tms.sportlight.dto.CommunityListDTO;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;

    @Transactional(readOnly = true)
    public Community getCommunity(int id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_COMMUNITY));
    }

    public int save(Community community) {
        return communityRepository.save(community);
    }

    public void updateCommunity(Community community) {

    }

    public List<CommunityListDTO> getCommunityListDTOList() {
        return communityRepository.findListDTOList();
    }

}
