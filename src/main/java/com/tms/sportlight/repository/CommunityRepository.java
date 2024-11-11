package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Community;
import com.tms.sportlight.dto.CommunityListDTO;
import com.tms.sportlight.mapper.CommunityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommunityRepository {

    private final JpaCommunityRepository jpaCommunityRepository;
    private final CommunityMapper communityMapper;

    public Optional<Community> findById(int id) {
        return jpaCommunityRepository.findById(id);
    }

    public int save(Community community) {
        return jpaCommunityRepository.save(community).getId();
    }

    public List<CommunityListDTO> findListDTOList() {
        // 현재 내 위치로부터 가까운 순 정렬 + 페이징 + 필터링
        return communityMapper.getListDTOList();
    }
}
