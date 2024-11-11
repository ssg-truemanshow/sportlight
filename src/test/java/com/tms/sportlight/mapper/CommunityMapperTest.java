package com.tms.sportlight.mapper;

import com.tms.sportlight.dto.CommunityListDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class CommunityMapperTest {

    @Autowired
    CommunityMapper communityMapper;

    @Test
    void getListDTO() {
        List<CommunityListDTO> listDTOList = communityMapper.getListDTOList();
        listDTOList.forEach(dto -> log.info("{}", dto));
    }

}
