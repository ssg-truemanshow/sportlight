package com.tms.sportlight.service;

import com.tms.sportlight.domain.Course;
import com.tms.sportlight.domain.Review;
import com.tms.sportlight.domain.User;
import com.tms.sportlight.dto.ReviewDTO;
import com.tms.sportlight.dto.UserDTO;
import com.tms.sportlight.dto.UserUpdateDTO;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.CourseRepository;
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
    private final JpaReviewRepository reviewRepository;
    private final CourseRepository courseRepository;

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

        boolean isExist = userRepository.existsByUserNickname(userUpdateDTO.getUserNickname());

        if (isExist) {
            throw new BizException(ErrorCode.DUPLICATE_NICKNAME);
        }

        user.update(
            userUpdateDTO.getUserNickname(),
            userUpdateDTO.getUserIntroduce(),
            userUpdateDTO.getMarketingAgreement(),
            userUpdateDTO.getPersonalAgreement()
        );

    }

    public List<ReviewDTO> getReviews(User user) {
        List<Review> reviews = reviewRepository.findByUser(user);
        return reviews.stream()
            .map(ReviewDTO::fromEntity)
            .toList();
    } // return 수정?

    @Transactional
    public void writeReview(Integer id, User user, ReviewDTO reviewDTO) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_COURSE));

        Review review = reviewDTO.toEntity(course, user);
        reviewRepository.save(review);
    }

    @Transactional
    public void modifyReview(Integer id, User user, String content, int rating) {
        Review review = reviewRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_REVIEW));

        review.updateReview(content, rating);
    }

    @Transactional
    public void deleteReview(Integer id, User user) {
        Review review = reviewRepository.findByIdAndUser(id, user)
            .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_REVIEW));

        reviewRepository.delete(review);
    }


}
