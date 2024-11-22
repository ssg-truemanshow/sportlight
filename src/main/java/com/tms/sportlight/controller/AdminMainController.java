package com.tms.sportlight.controller;

import com.tms.sportlight.domain.AdjustmentStatus;
import com.tms.sportlight.domain.CourseStatus;
import com.tms.sportlight.domain.HostRequestStatus;
import com.tms.sportlight.dto.*;
import com.tms.sportlight.dto.AdminCourseLocationDTO;
import com.tms.sportlight.dto.common.DataResponse;
import com.tms.sportlight.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminMainController {
    private final AdminService adminService;
    private final DailySaleService dailySaleService;
    private final MonthlySaleService monthlySaleService;
    private final YearlySaleService yearlySaleService;
    private final CourseService courseService;


    @GetMapping("/main")
    public DataResponse<Map<String, Object>> getMainData() {
        long userCount = adminService.getUserCount();
        long courseCount = adminService.getCourseCount();
        BigDecimal totalDailySaleAmount = dailySaleService.getTotalDailySaleAmount();
        BigDecimal averageDailySaleAmount = dailySaleService.getAverageDailySaleAmount();
        BigDecimal averageMonthlySaleAmount = monthlySaleService.getAverageMonthlySaleAmount();
        BigDecimal averageYearlySaleAmount = yearlySaleService.getAverageYearlySaleAmount();
        List<DailySaleDTO> allDailySales = dailySaleService.getAllDailySales();
        List<Object[]> openCourseCounts = adminService.getOpenCourseCountsGroupedByStartTime();
        List<CourseCardDTO> courseTop = courseService.getPopularCourses();
        List<UserDTO> hostTop = adminService.getTop3UsersByRevenue();


        Map<String, Object> mainData = new HashMap<>();
        mainData.put("userCount", userCount);
        mainData.put("courseCount", courseCount);
        mainData.put("totalDailySale", totalDailySaleAmount);
        mainData.put("averageDailySale", averageDailySaleAmount);
        mainData.put("averageMonthlySale", averageMonthlySaleAmount);
        mainData.put("averageYearlySale", averageYearlySaleAmount);
        mainData.put("allDailySales", allDailySales);
        mainData.put("openCourseCounts", openCourseCounts);
        mainData.put("courseTop", courseTop);
        mainData.put("hostTop", hostTop);

        return DataResponse.of(mainData);
    }

    @GetMapping("/users")
    public DataResponse<List<AdminUserDTO>> getUsers() {
        List<AdminUserDTO> users = adminService.getAllUsers();
        return DataResponse.of(users);
    }

    @GetMapping("/courses")
    public DataResponse<List<AdminCourseDTO>> getAllCourses() {
        List<AdminCourseDTO> courses = adminService.getAllCourses();
        return DataResponse.of(courses);
    }

    @GetMapping("/sales")
    public DataResponse<Map<String, List<?>>> getAllSales() {
        List<DailySaleDTO> dailySales = dailySaleService.getAllDailySales();
        List<MonthlySaleDTO> monthlySales = monthlySaleService.getAllMonthlySales();
        List<YearlySaleDTO> yearlySales = yearlySaleService.getAllYearlySales();

        Map<String, List<?>> salesData = new HashMap<>();
        salesData.put("dailySales", dailySales);
        salesData.put("monthlySales", monthlySales);
        salesData.put("yearlySales", yearlySales);

        return DataResponse.of(salesData);
    }

    @GetMapping("/payments")
    public DataResponse<List<AdminPaymentDTO>> getPaymentsWithoutRefund() {
        List<AdminPaymentDTO> payments = adminService.getAllPaymentsWithoutRefund();
        return DataResponse.of(payments);
    }

    @GetMapping("/refunds")
    public DataResponse<List<AdminRefundDTO>> getAllRefunds() {
        List<AdminRefundDTO> refunds = adminService.getAllRefunds();
        return DataResponse.of(refunds);
    }

    @GetMapping("/dashboard")
    public DataResponse<Map<String, Object>> getDashboardData() {
        AdminUserRoleCountDTO adminUserRoleCountDTO = adminService.getUserRoleCounts();
        AdminUserAgeGroupCountDTO adminUserAgeGroupCountDTO = adminService.getUserAgeGroupCounts();
        List<AdminGenderAgeGroupCourseCountDTO> adminGenderAgeGroupCourseCountDTO = adminService.getCourseCountsGroupedByGenderAndAge();
        List<AdminCourseLocationDTO> adminCourseLocationDTO = adminService.getAllCourseTitlesAndLocations();
        List<AdminCategoryCourseCountDTO> adminCategoryCourseCountDTO = adminService.getCategoryCourseCounts();

        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("userRoleCount", adminUserRoleCountDTO);
        dashboardData.put("userAgeGroupCount", adminUserAgeGroupCountDTO);
        dashboardData.put("genderAgeGroupCourseCountDTO", adminGenderAgeGroupCourseCountDTO);
        dashboardData.put("courseLocationDTO", adminCourseLocationDTO);
        dashboardData.put("categoryCourseCountDTO", adminCategoryCourseCountDTO);

        return DataResponse.of(dashboardData);
    }

    @GetMapping("/events")
    public DataResponse<List<AdminEventDTO>> getAllEventsWithCoupon() {
        List<AdminEventDTO> events = adminService.getAllEventsWithCoupon();
        return DataResponse.of(events);
    }

    @PostMapping("/event-create")
    public ResponseEntity<DataResponse> createEvent(@RequestBody AdminEventRequestDTO adminEventRequestDTO) {
        adminService.createEvent(adminEventRequestDTO);
        return ResponseEntity.ok(DataResponse.empty());
    }

    @GetMapping("/coupons")
    public DataResponse<List<AdminCouponDTO>> getAllCouponsWithEvent() {
        List<AdminCouponDTO> coupons = adminService.getAllCouponsWithEvent();
        return DataResponse.of(coupons);
    }

    @GetMapping("/adjustments")
    public DataResponse<List<AdminAdjustmentDetailDTO>> getAllAdjustments() {
        List<AdminAdjustmentDetailDTO> adjustments = adminService.getAllAdjustments();
        return DataResponse.of(adjustments);
    }

    @PatchMapping("/adjustments/{id}/status")
    public DataResponse<Void> modifyStatus(@PathVariable Id id, @Valid @NotNull AdjustmentStatus status) {
        adminService.updateAdjustmentStatus(id.getId(), status);
        return DataResponse.empty();
    }

    @GetMapping("/host-requests")
    public DataResponse<List<AdminHostRequestDTO>> getAllHostRequests() {
        List<AdminHostRequestDTO> hostRequests = adminService.getAllHostRequests();
        return DataResponse.of(hostRequests);
    }

    @PatchMapping("/host-requests/{id}/status")
    public DataResponse<Void> modifyStatus(@PathVariable Id id, @Valid @NotNull HostRequestStatus status) {
        adminService.updateHostRequestStatus(id.getId(), status);
        return DataResponse.empty();
    }

    @GetMapping("/course-requests")
    public DataResponse<List<AdminCourseRequestDTO>> getAllCourseRequests() {
        List<AdminCourseRequestDTO> courseRequests = adminService.getAllCourseRequests();
        return DataResponse.of(courseRequests);
    }

    @PatchMapping("/course-requests/{id}/status")
    public DataResponse<Void> modifyStatus(@PathVariable Id id, @Valid @NotNull CourseStatus status) {
        adminService.updateCourseRequestStatus(id.getId(), status);
        return DataResponse.empty();
    }

}
