package com.tms.sportlight.repository;

import com.tms.sportlight.domain.Category;
import com.tms.sportlight.domain.Community;
import com.tms.sportlight.domain.User;
import com.tms.sportlight.domain.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@SpringBootTest
class JpaCommunityRepositoryTest {

    @Autowired
    JpaCommunityRepository jpaCommunityRepository;
    @Autowired
    UserRepository userRepository;
    User creator;

    @BeforeEach
    void creatorInit() {
        creator = User.builder()
                .roles(List.of(UserRole.USER, UserRole.COMMUNITY_CREATOR))
                .termsAgreement(true)
                .regDate(LocalDateTime.now())
                .isDeleted(false)
                .build();
        userRepository.save(creator);
    }

    @Test
    void save() {
        Community community = Community.builder()
                .category(new Category(1))
                .user(creator)
                .title("title")
                .description("description")
                .maxCapacity(50)
                .latitude(127.0495556)
                .longitude(37.514575)
                .address("address")
                .detailAddress("detailAddress")
                .regDate(LocalDateTime.now())
                .delDate(null)
                .deleted(false)
                .build();
        jpaCommunityRepository.save(community);
        Community findCommunity = jpaCommunityRepository.findById(community.getId()).get();
        Assertions.assertEquals(community.getUser().getId(), findCommunity.getUser().getId());
        Assertions.assertEquals(community.getCategory().getId(), findCommunity.getCategory().getId());
        Assertions.assertEquals(community.getTitle(), findCommunity.getTitle());
        Assertions.assertEquals(community.getDescription(), findCommunity.getDescription());
        Assertions.assertEquals(community.getMaxCapacity(), findCommunity.getMaxCapacity());
        Assertions.assertEquals(community.getLatitude(), findCommunity.getLatitude());
        Assertions.assertEquals(community.getLongitude(), findCommunity.getLongitude());
        Assertions.assertEquals(community.getAddress(), findCommunity.getAddress());
        Assertions.assertEquals(community.getDetailAddress(), findCommunity.getDetailAddress());
        Assertions.assertEquals(community.isDeleted(), findCommunity.isDeleted());
    }
}
