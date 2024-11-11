package com.tms.sportlight.mapper;

import com.tms.sportlight.dto.CommunityListDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunityMapper {

    List<CommunityListDTO> getListDTOList();
}
