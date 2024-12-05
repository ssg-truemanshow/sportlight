package com.tms.sportlight.controller;

import com.tms.sportlight.domain.AttendCourseStatus;
import com.tms.sportlight.domain.MyCouponStatus;
import com.tms.sportlight.dto.CategoryDTO;
import com.tms.sportlight.dto.CouponDTO;
import com.tms.sportlight.dto.CourseCardDTO;
import com.tms.sportlight.dto.HostRequestCheckDTO;
import com.tms.sportlight.dto.HostRequestDTO;
import com.tms.sportlight.dto.MyCouponDTO;
import com.tms.sportlight.dto.MyCourseDTO;
import com.tms.sportlight.dto.MyPageDTO;
import com.tms.sportlight.dto.MyReviewDTO;
import com.tms.sportlight.dto.UpdateUserInterestsRequestDTO;
import com.tms.sportlight.dto.UserDTO;
import com.tms.sportlight.dto.UserUpdateDTO;
import com.tms.sportlight.dto.common.DataResponse;
import com.tms.sportlight.dto.common.PageRequestDTO;
import com.tms.sportlight.dto.common.PageResponse;
import com.tms.sportlight.exception.BizException;
import com.tms.sportlight.exception.ErrorCode;
import com.tms.sportlight.security.CustomUserDetails;
import com.tms.sportlight.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController()
@RequestMapping("/my")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public DataResponse<MyPageDTO> getMyPage(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        MyPageDTO myPageInfo = userService.getMyPageInfo(userDetails.getUser());
        return DataResponse.of(myPageInfo);
    }

    @GetMapping("/check-loginId")
    public DataResponse<Boolean> checkLoginId(@RequestParam String loginId) {
        boolean isAvailable = userService.isLoginIdAvailable(loginId);
        return DataResponse.of(isAvailable);
    }


    @GetMapping("/check-nickname")
    public DataResponse<Boolean> checkNickname(@RequestParam String userNickname) {
        boolean isAvailable = userService.isNicknameCheck(userNickname);
        return DataResponse.of(isAvailable);
    }


    @GetMapping("/profile")
    public DataResponse<UserDTO> getMyProfile(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            UserDTO userDTO = userService.getProfile(userDetails.getUser());
            return DataResponse.of(userDTO);
        } catch (Exception e) {
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DataResponse<Void> updateMyProfile(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @ModelAttribute UserUpdateDTO userUpdateDTO) {
        try {
            userService.updateProfile(userDetails.getUser(), userUpdateDTO);
            return DataResponse.empty();
        } catch (Exception e) {
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/courses/{id}/write-review")
    public DataResponse<Void> writeReview(@AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Integer id,
        @Valid MyReviewDTO myReviewDTO) {
        userService.writeReview(id, userDetails.getUser(), myReviewDTO);
        return DataResponse.empty();
    }

    @GetMapping("/reviews")
    public DataResponse<List<MyReviewDTO>> getMyReviews(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<MyReviewDTO> myReviewDTOList = userService.getReviews(userDetails.getUser());
        return DataResponse.of(myReviewDTOList);
    }

    @PatchMapping("/reviews/{id}")
    public ResponseEntity<Void> modifyReview(@AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Integer id,
        @Valid MyReviewDTO myReviewDTO) {
        userService.modifyReview(id, userDetails.getUser(), myReviewDTO.getContent(),
            myReviewDTO.getRating());
        return null;
    }

    @DeleteMapping("/reviews/{id}")
    public DataResponse<Void> deleteReview(@AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Integer id) {
        userService.deleteReview(id, userDetails.getUser());
        return DataResponse.empty();
    }

    @GetMapping("/coupons")
    public DataResponse<List<CouponDTO>> getMyCoupons(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<CouponDTO> coupons = userService.getCoupons(userDetails.getUser());
        return DataResponse.of(coupons);
    }

    @GetMapping("/coupons/{couponId}")
    public DataResponse<CouponDTO> getMyCoupon(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Integer couponId
    ) {
        CouponDTO coupon = userService.getCoupon(userDetails.getUser(), couponId);
        return DataResponse.of(coupon);
    }

    @GetMapping("/coupons/paged")
    public PageResponse<MyCouponDTO> getMyPagedCoupon(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam(defaultValue = "AVAILABLE") String status,  // ENUM 대신 String으로 받음
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        PageRequestDTO<MyCouponDTO> pageRequestDTO = new PageRequestDTO<>();
        pageRequestDTO.setPage(page);
        pageRequestDTO.setSize(size);

        MyCouponStatus couponStatus = MyCouponStatus.valueOf(status);

        return userService.getUserCoupons(userDetails.getUser(), pageRequestDTO, couponStatus);
    }


    @GetMapping("/interests")
    public DataResponse<List<CourseCardDTO>> getMyInterests(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        return DataResponse.of(userService.getInterests(userDetails.getUser()));
    }

    @PatchMapping("/interests/{courseId}")
    public DataResponse<Boolean> toggleInterest(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Integer courseId
    ) {
        return DataResponse.of(userService.toggleInterest(userDetails.getUser(), courseId));
    }

    /*@GetMapping("/chats")
    public DataResponse<List<ChatDTO>> getChats(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ChatDTO> chats = userService.getChats(userDetails.getUser());
        return DataResponse.of(chats);
    }*/

    @PostMapping(value = "/host-request", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DataResponse<Void> registerHostRequest(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @ModelAttribute @Valid HostRequestDTO hostRequestDTO) {
        userService.registerHostRequest(userDetails.getUser(), hostRequestDTO);
        return DataResponse.empty();
    }

    @PatchMapping("/host-request")
    public DataResponse<Void> updateHostRequest(@AuthenticationPrincipal CustomUserDetails userDetails, @ModelAttribute HostRequestDTO hostRequestDTO) {
        log.info("patch: {}", hostRequestDTO);
        userService.updateHostRequest(userDetails.getUser(), hostRequestDTO,
            hostRequestDTO.getCertification());
        return DataResponse.of(null);
    }

    @DeleteMapping("/host-request")
    public DataResponse<Void> deleteHostRequest(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deleteHostRequest(userDetails.getUser());
        return DataResponse.empty();
    }

    @GetMapping("/host-request")
    public DataResponse<HostRequestDTO> getHostRequestDetail(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        HostRequestDTO hostRequestDetail = userService.getHostRequestDetail(userDetails.getUser());
        return DataResponse.of(hostRequestDetail);
    }

    @GetMapping("/host-request-status")
    public DataResponse<HostRequestCheckDTO> getHostRequestStatus(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        HostRequestCheckDTO hostRequestStatus = userService.getHostRequestStatus(userDetails.getUser());
        return DataResponse.of(hostRequestStatus);
    }

    @GetMapping("/user-interests")
    public DataResponse<List<CategoryDTO>> getUserInterests(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<CategoryDTO> interests = userService.getUserInterests(userDetails.getUser());
        return DataResponse.of(interests);
    }

    @PatchMapping("/user-interests")
    public DataResponse<Void> updateUserInterests(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody UpdateUserInterestsRequestDTO request
    ) {
        userService.updateUserInterests(userDetails.getUser(), request.getCategoryIds());
        return DataResponse.empty();
    }

    @GetMapping("/courses")
    public DataResponse<List<MyCourseDTO>> getMyCourses(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestParam(required = false) AttendCourseStatus status
    ) {
        List<MyCourseDTO> myCourses = userService.getMyCourses(userDetails.getUser(), status);
        return DataResponse.of(myCourses);
    }

    @PostMapping("/courses/{courseId}/cancel")
    public DataResponse<Void> cancelCourse(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Integer courseId
    ) {
        userService.cancelCourse(userDetails.getUser(), courseId);
        return DataResponse.empty();
    }

}
