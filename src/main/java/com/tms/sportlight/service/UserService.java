package com.tms.sportlight.service;

import com.tms.sportlight.domain.Course;
import com.tms.sportlight.domain.Review;
import com.tms.sportlight.domain.User;
import com.tms.sportlight.dto.MyReviewDTO;
import com.tms.sportlight.dto.UserDTO;
import com.tms.sportlight.dto.UserUpdateDTO;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.CourseRepository;
//import com.tms.sportlight.repository.MyCommunityRepository;
import com.tms.sportlight.repository.JpaReviewRepository;
import com.tms.sportlight.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FileService fileService;
    private final JpaReviewRepository jpaReviewRepository;
    private final JpaReviewRepository reviewRepository;

    private final CourseRepository courseRepository;
    //private final MyCommunityRepository myCommunityRepository;


    public UserDTO getProfile(User user) {
        String userImage = fileService.getUserIconFile(Math.toIntExact(user.getId()));

        return UserDTO.builder()
            .userImage(userImage)
            .loginId(user.getLoginId())
            .userNickname(user.getUserNickname())
            .userIntroduce(user.getUserIntroduce())
            .userName(user.getUserName())
            .userPhone(user.getUserPhone())
            .marketingAgreement(user.getMarketingAgreement())
            .personalAgreement(user.getPersonalAgreement())
            .build();
    }

    @Transactional
    public void updateProfile(User user, UserUpdateDTO userUpdateDTO) {

        if (!user.getUserNickname().equals(userUpdateDTO.getUserNickname())
            && userRepository.existsByUserNickname(userUpdateDTO.getUserNickname())) {
            throw new BizException(ErrorCode.DUPLICATE_NICKNAME);
        }

        user.update(
            userUpdateDTO.getUserNickname(),
            userUpdateDTO.getUserIntroduce(),
            userUpdateDTO.getMarketingAgreement(),
            userUpdateDTO.getPersonalAgreement()
        );

    }

    public List<MyReviewDTO> getReviews(User user) {
        List<Review> reviews = jpaReviewRepository.findByUser(user);
        return reviews.stream()
            .map(MyReviewDTO::fromEntity)
            .toList();
    } // return 수정?

    @Transactional
    public void writeReview(Integer id, User user, MyReviewDTO myReviewDTO) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_COURSE));
        Review review = myReviewDTO.toEntity(course, user);
        jpaReviewRepository.save(review);
    }

    @Transactional
    public void modifyReview(Integer id, User user, String content, int rating) {
        Review review = jpaReviewRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_REVIEW));
        review.updateReview(content, rating);
    }

    @Transactional
    public void deleteReview(Integer id, User user) {
        Review review = jpaReviewRepository.findByIdAndUser(id, user)
            .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_REVIEW));
        jpaReviewRepository.delete(review);
    }

    /*public List<CommunityListDTO> getCreatedCommunities(Long userId) {
        List<Community> communities = myCommunityRepository.findCreatedCommunities(userId);
        return null;
            *//*communities.stream()
            .map(CommunityListDTO::fromEntity)
            .collect(Collectors.toList());*//*
    }

    public List<CommunityListDTO> getJoinedCommunities(Long userId) {
        List<Community> communities = myCommunityRepository.findJoinedCommunities(userId);
        return null;
            *//*communities.stream()
            .map(CommunityListDTO::fromEntity)
            .collect(Collectors.toList());*//*
    }*/




}
