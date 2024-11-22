package com.tms.sportlight.controller;

import com.tms.sportlight.domain.Course;
import com.tms.sportlight.dto.CouponDTO;
import com.tms.sportlight.dto.HostRequestDTO;
import com.tms.sportlight.dto.MyReviewDTO;
import com.tms.sportlight.dto.UserDTO;
import com.tms.sportlight.dto.UserUpdateDTO;
import com.tms.sportlight.dto.common.DataResponse;
import com.tms.sportlight.security.CustomUserDetails;
import com.tms.sportlight.service.InterestService;
import com.tms.sportlight.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/my")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final InterestService interestService;

    @GetMapping("")
    public ResponseEntity<String> getMyPage() {

        return null;
    }


    @GetMapping("/profile")
    public DataResponse<UserDTO> getMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserDTO userDTO = userService.getProfile(userDetails.getUser());
        return DataResponse.of(userDTO);
    } // 파일서비스 의존성 추가해서 거기서 유저의 프로필 이미지를 조회해서

    @PostMapping("/profile")
    public DataResponse<Void> updateMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @Valid UserUpdateDTO userUpdateDTO) {
        userService.updateProfile(userDetails.getUser(), userUpdateDTO);

        return DataResponse.empty();
    }

    /*@GetMapping("/courses")
    public DataResponse<List<Course>> getMyCourses(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Course> myCourses = userService.getMyCourses(userDetails.getUser());
        return DataResponse.of(myCourses);
    }

    @PatchMapping("/courses/{courseId}")
    public DataResponse<Void> cancelPaidCourse(@AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Integer courseId) {
        userService.cancelCourse(userDetails.getUser(), courseId);
        return DataResponse.empty();
    }*/

    @PostMapping("/courses/{id}/write-review")
    public DataResponse<Void> writeReview( @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Integer id,
                                           @Valid MyReviewDTO myReviewDTO) {
        userService.writeReview(id, userDetails.getUser(), myReviewDTO);
        return DataResponse.empty();
    }

    @GetMapping("/reviews")
    public DataResponse<List<MyReviewDTO>> getMyReviews(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<MyReviewDTO> myReviewDTOList = userService.getReviews(userDetails.getUser());
        return DataResponse.of(myReviewDTOList);
    }

    @PatchMapping("/reviews/{id}")
    public ResponseEntity<Void> modifyReview(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Integer id,
                                             @Valid MyReviewDTO myReviewDTO) {
        userService.modifyReview(id, userDetails.getUser(), myReviewDTO.getContent(),
            myReviewDTO.getRating());
        return null;
    }

    @DeleteMapping("/reviews/{id}")
    public DataResponse<Void> deleteReview(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Integer id) {
        userService.deleteReview(id, userDetails.getUser());
        return DataResponse.empty();
    }

    /*@GetMapping("/coupons")
    public DataResponse<List<CouponDTO>> getMyCoupons(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<CouponDTO> coupons = userService.getCoupons(userDetails.getUser());
        return DataResponse.of(coupons);
    }

    @GetMapping("/coupons/{id}")
    public DataResponse<CouponDTO> getMyCoupon(@AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Integer id) {
        CouponDTO coupon = userService.getCoupon(userDetails.getUser(), id);
        return DataResponse.of(coupon);
    }*/

    @GetMapping("/interests")
    public DataResponse<List<Course>> getMyInterests(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Course> interests = interestService.getInterests(userDetails.getUser());
        return DataResponse.of(interests);
    }

    @PatchMapping("/interests/{courseId}")
    public ResponseEntity<Void> toggleInterest(@AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Integer courseId) {
        interestService.toggleInterest(userDetails.getUser(), courseId);
        return ResponseEntity.ok().build();
    }

    /*@GetMapping("/chats")
    public DataResponse<List<ChatDTO>> getChats(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ChatDTO> chats = userService.getChats(userDetails.getUser());
        return DataResponse.of(chats);
    }

    @PostMapping("/host-Request")
    public DataResponse<Void> registerHostRequest(@AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid HostRequestDTO hostRequestDTO) {
        userService.registerHostRequest(userDetails.getUser(), hostRequestDTO);
        return DataResponse.empty();
    }

    @PatchMapping("/host-Request/{id}/mod")
    public DataResponse<Void> modifyHostRequest(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @PathVariable Integer id, @Valid HostRequestDTO hostRequestDTO) {
        userService.modifyHostRequest(userDetails.getUser(), id, hostRequestDTO);
        return DataResponse.empty();
    }

    @DeleteMapping("/host-Request/{id}/del")
    public DataResponse<Void> deleteHostRequest(@AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Integer id) {
        userService.deleteHostRequest(userDetails.getUser(), id);
        return DataResponse.empty();
    }

    @GetMapping("/host-Request")
    public DataResponse<HostRequestDTO> getMyHostRequest(@AuthenticationPrincipal CustomUserDetails userDetails) {
        HostRequestDTO hostRequest = userService.getMyHostRequest(userDetails.getUser());
        return DataResponse.of(hostRequest);
    }*/

}
