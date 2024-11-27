package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Community;
import com.tms.sportlight.dto.CommunityListDTO;
import com.tms.sportlight.dto.CommunitySearchDTO;
import com.tms.sportlight.dto.common.PageRequestDTO;
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

    public List<CommunityListDTO> getCommunityListByCapacity(PageRequestDTO<CommunitySearchDTO> pageRequestDTO) {
        return communityMapper.getListDTOListByCapacity(pageRequestDTO);
    }

    public List<CommunityListDTO> getListDTOListByDistance(PageRequestDTO<CommunitySearchDTO> pageRequestDTO) {
        return communityMapper.getListDTOListByDistance(pageRequestDTO);
    }

    public int getCommunityCount(PageRequestDTO<CommunitySearchDTO> pageRequestDTO) {
        return communityMapper.getCommunityCount(pageRequestDTO);
    }

    /*public Optional<CommunityDetailDTO> getCommunityDetail(int id) {
        return communityMapper.getCommunityDetail(id);
    }*/
}
