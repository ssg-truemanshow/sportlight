package com.tms.sportlight.service;

import com.tms.sportlight.domain.*;
import com.tms.sportlight.dto.*;
import com.tms.sportlight.dto.common.PageRequestDTO;
import com.tms.sportlight.dto.common.PageResponse;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.repository.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FileService fileService;
    private final InterestRepository interestRepository;
    private final MyReviewRepository myReviewRepository;
    private final MyCommunityRepository myCommunityRepository;
    private final MyCouponRepository myCouponRepository;
    private final HostRequestRepository hostRequestRepository;
    private final CourseRepository courseRepository;
    private final MyCourseRepository myCourseRepository;
    private final UserInterestsRepository userInterestsRepository;
    private final MyCategoryRepository myCategoryRepository;
    private final HostInfoRepository hostInfoRepository;

    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_USER));
    }


    @Transactional(readOnly = true)
    public MyPageDTO getMyPageInfo(User user) {
        String userImage = null;
        String hostRequestStatus = "신청가능";
        int couponCount = 0;
        int communityCount = 0;
        int interestCount = 0;
        int reviewCount = 0;

        try {
            userImage = fileService.getUserIconFile(Math.toIntExact(user.getId()));
        } catch (Exception e) {
            log.error("이미지 로드 실패", e);
        }

        try {
            interestCount = interestRepository.countByUserId(user.getId()); // 관심 목록 카운트
        } catch (Exception e) {
            log.error("관심목록 카운트 실패", e);
        }

        try {
            reviewCount = myReviewRepository.countByUserId(user.getId());
        } catch (Exception e) {
            log.error("리뷰 카운트 실패", e);
        }

        try {
            hostRequestStatus = hostRequestRepository.findByUser(user)
                .map(request -> request.getReqStatus().getMessage())
                .orElse("신청가능");
        } catch (Exception e) {
            log.error("호스트 요청 상태 로드 실패", e);
        }

        try {
            couponCount = myCouponRepository.countAvailableByUserId(user.getId());
        } catch (Exception e) {
            log.error("쿠폰 카운트 실패", e);
        }

        try {
            communityCount = myCommunityRepository.countByUserId(user.getId());
        } catch (Exception e) {
            log.error("커뮤니티 카운트 실패", e);
        }

        UserDTO userInfo = UserDTO.builder()
            .userId(user.getId())
            .loginId(user.getLoginId())
            .userNickname(user.getUserNickname())
            .userIntroduce(user.getUserIntroduce())
            .userName(user.getUserName())
            .userPhone(user.getUserPhone())
            .marketingAgreement(user.getMarketingAgreement())
            .personalAgreement(user.getPersonalAgreement())
            .userImage(userImage)
            .build();

        return MyPageDTO.builder()
            .userInfo(userInfo)
            .interestCount(interestCount)
            .reviewCount(reviewCount)
            .couponCount(couponCount)
            .communityCount(communityCount)
            .hostRequestStatus(hostRequestStatus)
            .build();
    }

    public boolean isLoginIdAvailable(String loginId) {
        if (userRepository.existsByLoginId(loginId)) {
            throw new BizException(ErrorCode.DUPLICATE_USERNAME);
        }
        return true;
    }

    public boolean isNicknameCheck(String nickname) {
        if (userRepository.existsByUserNickname(nickname)) {
            throw new BizException(ErrorCode.DUPLICATE_NICKNAME);
        }
        return true;
    }

    @Transactional(readOnly = true)
    public UserDTO getProfile(User user) {
        try {
            String userImage = fileService.getUserIconFile(Math.toIntExact(user.getId()));

            Integer couponCount = myCouponRepository.countAvailableByUserId(user.getId());
            return UserDTO.builder()
                .userId(user.getId())
                .userImage(userImage)
                .loginId(user.getLoginId())
                .userNickname(user.getUserNickname())
                .userIntroduce(user.getUserIntroduce())
                .userName(user.getUserName())
                .userPhone(user.getUserPhone())
                .marketingAgreement(user.getMarketingAgreement())
                .personalAgreement(user.getPersonalAgreement())
                .couponCount(couponCount)
                .build();
        } catch (Exception e) {
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void updateProfile(User user, UserUpdateDTO userUpdateDTO) {
        if (userUpdateDTO.getUserNickname() != null &&
            !user.getUserNickname().equals(userUpdateDTO.getUserNickname()) &&
            userRepository.existsByUserNickname(userUpdateDTO.getUserNickname())) {
            throw new BizException(ErrorCode.DUPLICATE_NICKNAME);
        }
        User findUser = userRepository.findById(user.getId()).get();
        findUser.update(
            userUpdateDTO.getUserNickname(),
            userUpdateDTO.getUserIntroduce(),
            userUpdateDTO.getMarketingAgreement(),
            userUpdateDTO.getPersonalAgreement()
        );

        if (userUpdateDTO.getUserImage() != null && !userUpdateDTO.getUserImage().isEmpty()) {
            try {
                fileService.saveUserIconFile(Math.toIntExact(user.getId()), userUpdateDTO.getUserImage());
            } catch (Exception e) {
                log.error("프로필 이미지 업로드 실패", e);
                throw new BizException(ErrorCode.FILE_UPLOAD_ERROR);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<CourseCardDTO> getInterests(User user) {
        List<Course> courses = interestRepository.findByUserId(user.getId()).stream()
            .map(Interest::getCourse)
            .toList();

        return courses.stream()
            .map(course -> {
                double rating = myReviewRepository.getAverageRatingByCourseId(course.getId());
                long reviewCount = myReviewRepository.countByCourseId(course.getId());
                String imgUrl = null;
                try {
                    imgUrl = fileService.getCourseMainImage(course.getId());
                } catch (Exception e) {
                    log.error("코스 이미지 로드 실패", e);
                }

                return CourseCardDTO.builder()
                    .id(course.getId())
                    .nickname(course.getUser().getUserNickname())
                    .title(course.getTitle())
                    .address(course.getAddress())
                    .tuition(course.getTuition())
                    .discountRate(course.getDiscountRate())
                    .time(course.getTime())
                    .level(course.getLevel())
                    .category(course.getCategory().getName())
                    .rating(rating)
                    .reviewCount(reviewCount)
                    .imgUrl(imgUrl)
                    .build();
            })
            .collect(Collectors.toList());
    }

    @Transactional
    public boolean toggleInterest(User user, Integer courseId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_COURSE));

        return interestRepository.findByUserIdAndCourseId(user.getId(), courseId)
            .map(interest -> {
                interestRepository.delete(interest);
                return false;
            })
            .orElseGet(() -> {
                Interest newInterest = Interest.builder()
                    .user(user)
                    .course(course)
                    .build();
                interestRepository.save(newInterest);
                return true;
            });
    }

    @Transactional(readOnly = true)
    public List<MyReviewDTO> getReviews(User user) {
        List<Review> reviews = myReviewRepository.findAllByUserId(user.getId());
        return reviews.stream()
            .map(review -> MyReviewDTO.builder()
                .courseTitle(review.getCourse().getTitle())
                .content(review.getContent())
                .rating(review.getRating())
                .regDate(review.getRegDate())
                .build())
            .collect(Collectors.toList());
    }

    @Transactional
    public void writeReview(Integer id, Integer courseId, User user, MyReviewDTO myReviewDTO) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_COURSE));

        Review review = Review.builder()
            .id(id)
            .course(course)
            .user(user)
            .content(myReviewDTO.getContent())
            .rating(myReviewDTO.getRating())
            .regDate(LocalDateTime.now())
            .build();

        myReviewRepository.save(review);
    }

    @Transactional
    public void modifyReview(Integer reviewId, User user, String content, int rating) {
        Review review = myReviewRepository.findByIdAndUserId(reviewId, user.getId())
            .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_REVIEW));
        review.updateReview(content, rating);
    }

    @Transactional
    public void deleteReview(Integer reviewId, User user) {
        Review review = myReviewRepository.findByIdAndUserId(reviewId, user.getId())
            .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_REVIEW));
        myReviewRepository.delete(review);
    }

    @Transactional(readOnly = true)
    public HostRequestDTO getHostRequestDetail(User user) {
        HostRequest hostRequest = hostRequestRepository.findByUser(user)
            .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_REQUEST));

        return HostRequestDTO.builder()
            .hostBio(hostRequest.getHostBio())
            .portfolio(hostRequest.getPortfolio())
            .reqStatus(hostRequest.getReqStatus())
            .build();
    }

    @Transactional(readOnly = true)
    public HostRequestCheckDTO getHostRequestStatus(User user) {
        return hostRequestRepository.findByUser(user)
            .map(HostRequestCheckDTO::fromEntity)
            .orElse(null);
    }

    @Transactional
    public void registerHostRequest(User user, HostRequestDTO hostRequestDTO) {
        if (hostRequestRepository.findByUser(user).isPresent()) {
            throw new BizException(ErrorCode.DUPLICATE_HOST_REQUEST);
        }

        HostRequest hostRequest = hostRequestDTO.toEntity(user, null);
        hostRequestRepository.save(hostRequest);
    }

    @Transactional
    public void updateHostRequest(User user, HostRequestDTO requestDTO, List<MultipartFile> files) {
        HostRequest hostRequest = hostRequestRepository.findByUser(user)
            .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_REQUEST));

        hostRequest.updateHostRequest(requestDTO.getHostBio(), requestDTO.getPortfolio(), files);

        if (files != null && !files.isEmpty()) {
            fileService.saveHostCertificationFiles(hostRequest.getId(), files);
        }
    }

    @Transactional
    public void deleteHostRequest(User user) {
        HostRequest hostRequest = hostRequestRepository.findByUser(user)
            .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_REQUEST));
        hostRequestRepository.delete(hostRequest);
    }

    @Transactional(readOnly = true)
    public List<CouponDTO> getCoupons(User user) {
        return myCouponRepository.findAvailableByUserId(user.getId())
            .stream()
            .map(userCoupon -> CouponDTO.fromEntity(userCoupon.getCoupon()))
            .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public CouponDTO getCoupon(User user, Integer couponId) {
        UserCoupon userCoupon = myCouponRepository.findByIdAndUserId(couponId, user.getId())
            .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_COUPON));

        return CouponDTO.fromEntity(userCoupon.getCoupon());
    }

    @Transactional(readOnly = true)
    public PageResponse<MyCouponDTO> getUserCoupons(User user, PageRequestDTO<MyCouponDTO> pageRequestDTO, MyCouponStatus status) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("expDate").ascending());

        Page<UserCoupon> userCoupons = myCouponRepository.findAllByUserIdAndStatus(
            user.getId(),
            status.name(),
            pageable
        );

        List<MyCouponDTO> couponDTOs = userCoupons.stream()
            .map(uc -> MyCouponDTO.fromEntity(uc, status))
            .collect(Collectors.toList());

        return new PageResponse<>(pageRequestDTO, couponDTOs, (int) userCoupons.getTotalElements());
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

    @Transactional(readOnly = true)
    public List<CategoryDTO> getUserInterests(User user) {
        return userInterestsRepository.findByUserId(user.getId()).stream()
            .map(UserInterests::getCategory)
            .map(category -> CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build())
            .collect(Collectors.toList());
    }

    @Transactional
    public void updateUserInterests(User user, List<Integer> categoryIds) {
        if (categoryIds.size() > 5) {
            throw new BizException(ErrorCode.INVALID_INPUT_VALUE);
        }

        User findUser = getUser(user.getId());
        userInterestsRepository.deleteByUserId(findUser.getId());

        List<Category> categories = myCategoryRepository.findAllById(categoryIds);
        if (categories.size() != categoryIds.size()) {
            throw new BizException(ErrorCode.NOT_FOUND_CATEGORY);
        }

        categories.forEach(category -> {
            UserInterests userInterest = UserInterests.builder()
                .user(findUser)
                .category(category)
                .build();
            userInterestsRepository.save(userInterest);
        });
    }

    @Transactional(readOnly = true)
    public List<MyCourseDTO> getMyCourses(User user, AttendCourseStatus status) {
        List<AttendCourse> attendCourses;
        if (status != null) {
            attendCourses = myCourseRepository.findByUserIdAndStatus(user.getId(), status);
        } else {
            attendCourses = myCourseRepository.findByUserId(user.getId());
        }

        List<MyCourseDTO> dtos = attendCourses.stream()
            .map(course -> {
                MyCourseDTO myCourseDTO = MyCourseDTO.fromEntity(course);
                boolean hasReview = myReviewRepository.existsByCourseIdAndUserId(
                    course.getCourseSchedule().getCourse().getId(),
                    user.getId()
                );
                return myCourseDTO.toBuilder().hasReview(hasReview).build();
            })
            .collect(Collectors.toList());

        return dtos;
    }

    @Transactional
    public void cancelCourse(User user, Integer courseId) {
        AttendCourse attendCourse = myCourseRepository.findByIdAndUserId(courseId, user.getId())
            .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_COURSE));

        if (!"RESERVED".equals(attendCourse.getStatus())) {
            throw new BizException(ErrorCode.INVALID_CANCEL_REQUEST);
        }

        LocalDateTime startTime = attendCourse.getCourseSchedule().getStartTime();
        LocalDateTime now = LocalDateTime.now();
        long daysUntilStart = ChronoUnit.DAYS.between(now, startTime);

        double refundRate;
        if (daysUntilStart >= 7) refundRate = 1.0;
        else if (daysUntilStart >= 5) refundRate = 0.75;
        else if (daysUntilStart >= 3) refundRate = 0.5;
        else if (daysUntilStart >= 1) refundRate = 0.25;
        else throw new BizException(ErrorCode.INVALID_CANCEL_REQUEST);

        double refundAmount = attendCourse.getFinalAmount() * refundRate;

        /*RefundLog refundLog = RefundLog.builder()
            .attendCourse(attendCourse)
            .refundRate(refundRate)
            .refundAmount(refundAmount)
            .requestDate(now)
            .build();
        refundLogRepository.save(refundLog);

        paymentService.requestRefund(attendCourse.getPaymentKey(), refundAmount);

        attendCourse.setStatus("REFUNDED");
        attendCourse.setRefundLog(now + "," + refundAmount);
*/
        // 수강 인원 업데이트
        /*CourseSchedule schedule = attendCourse.getCourseSchedule();
        schedule.updateRemainedNum(schedule.getRemainedNum() + attendCourse.getParticipantNum());*/
    }

    public Long getUsersCount() {
        return userRepository.countUsersWithRole(UserRole.USER);
    }

    @Transactional(readOnly = true)
    public HostInfoDTO getHostInfoDTO(User user) {
        if(!user.getRoles().contains(UserRole.HOST)) {
            throw new BizException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        Optional<HostInfo> hostInfoOptional = hostInfoRepository.findByUserId(user.getId());
        HostInfo hostInfo;
        if(hostInfoOptional.isEmpty()) {
            hostInfo = HostInfo.builder()
                    .user(user)
                    .bio("")
                    .build();
            saveHostInfo(hostInfo);
        } else {
            hostInfo = hostInfoOptional.get();
        }
        return HostInfoDTO.from(hostInfo);
    }

    @Transactional
    public void saveHostInfo(HostInfo hostInfo) {
        hostInfoRepository.save(hostInfo);
    }

    @Transactional
    public void updateHostInfo(User user, HostInfoDTO dto) {
        HostInfo hostInfo = hostInfoRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BizException(ErrorCode.NOT_FOUND_HOST_INFO));
        hostInfo.update(dto.getHostBio(), dto.getHostInsta(), dto.getHostFacebook(), dto.getHostTwitter(), dto.getHostYoutube());
    }

}
