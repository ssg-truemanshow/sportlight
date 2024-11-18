package com.tms.sportlight.controller;

import com.tms.sportlight.dto.ReviewDTO;
import com.tms.sportlight.dto.UserDTO;
import com.tms.sportlight.dto.UserUpdateDTO;
import com.tms.sportlight.dto.common.DataResponse;
import com.tms.sportlight.security.CustomUserDetails;
import com.tms.sportlight.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/my")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

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
    public DataResponse<Void> updateUserProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @Valid UserUpdateDTO userUpdateDTO) {

        userService.updateProfile(userDetails.getUser(), userUpdateDTO);

        return DataResponse.empty();
    }

    @GetMapping("/courses")
    public ResponseEntity<String> getMyCourses() {

        return null;
    }

    @PostMapping("/courses")
    public ResponseEntity<String> cancelPaidCourse() {

        return null;
    }

    @PostMapping("/courses/{id}/write-review")
    public ResponseEntity<String> writeReview(@PathVariable String id) {

        return null;
    }

    @GetMapping("/reviews")
    public DataResponse<List<ReviewDTO>> getMyReviews(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ReviewDTO> reviewDTOList = userService.getReviews(userDetails.getUser());
        return DataResponse.of(reviewDTOList);
    }

    @PatchMapping("/reviews/{id}")
    public ResponseEntity<Void> modifyReview(@PathVariable Integer id, @AuthenticationPrincipal CustomUserDetails userDetails, ReviewDTO reviewDTO) {
        userService.modifyReview(id, userDetails.getUser(), reviewDTO.getContent(),
            reviewDTO.getRating());
        return null;
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Integer id) {

        return null;
    }

    @GetMapping("/coupons")
    public ResponseEntity<String> getMyCoupons() {

        return null;
    }

    @GetMapping("/coupons/{id}")
    public ResponseEntity<String> getMyCoupon() {

        return null;
    }

    @GetMapping("/interests")
    public ResponseEntity<String> getInterests() {

        return null;
    }

    @PostMapping("/interests")
    public ResponseEntity<String> cancelInterest() {

        return null;
    }

    @GetMapping("/chats")
    public ResponseEntity<String> getChats() {

        return null;
    }

    @PostMapping("/host-Request")
    public ResponseEntity<String> registerHostRequest() {

        return null;
    }

    @PostMapping("/host-Request/{id}/mod")
    public ResponseEntity<String> modifyHostRequest(@PathVariable String id) {

        return null;
    }

    @PostMapping("/host-Request/{id}/del")
    public ResponseEntity<String> deleteHostRequest(@PathVariable String id) {

        return null;
    }

    @GetMapping("/host-Request")
    public ResponseEntity<String> getMyHostRequest() {

        return null;
    }
}
