package silverpotion.userserver.admin.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import silverpotion.userserver.admin.dtos.*;
import silverpotion.userserver.admin.service.AdminService;
import silverpotion.userserver.common.dto.CommonDto;
import silverpotion.userserver.report.dtos.ReportDetailListDto;
import silverpotion.userserver.report.dtos.ReportProcessResDto;
import silverpotion.userserver.report.dtos.ReportRequestDto;
import silverpotion.userserver.report.dtos.ReportResponseDto;
import silverpotion.userserver.report.service.ReportService;
import silverpotion.userserver.user.service.UserService;

@RestController
@RequestMapping("/silverpotion/admins")
public class AdminController {
    private final AdminService adminService;
    private final ReportService reportService;
    private final UserService userService;

    public AdminController(AdminService adminService, ReportService reportService, UserService userService) {
        this.adminService = adminService;
        this.reportService = reportService;
        this.userService = userService;
    }
    // 관리자 등록
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/register/{userId}")
    public ResponseEntity<?> register(@PathVariable Long userId) {
        adminService.registerAdmin(userId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "관리자 등록 완료",userId), HttpStatus.OK);
    }
    //관리자 삭제
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long userId,@RequestHeader("X-User-LoginId")String loginId){
        adminService.deleteAdmin(userId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "관리자 삭제 완료",userId), HttpStatus.OK);
    }
    // 유저 목록 조회
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@PageableDefault(size = 10, sort = "id", direction =Sort.Direction.DESC)Pageable pageable, @ModelAttribute UserSearchDto dto){
        Page<AdminUserListDto> userListDto = adminService.userList(pageable,dto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "관리자 유저 조회 완료",userListDto),HttpStatus.OK);
    }
    // 유저 상세 조회
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @GetMapping("/detail/{userId}")
    public ResponseEntity<?> detailList(@PathVariable Long userId){
        UserDetailDto userDetailDto = adminService.userDetailList(userId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "관리자 유저 상세 조회 완료",userDetailDto),HttpStatus.OK);
    }

    //    사용자 정지(관리자 수동 처리)
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/ban")
    public ResponseEntity<?> banUser(@RequestBody UserBanRequestDto userBanRequestDto){
        adminService.banUserManually(userBanRequestDto.getUserId(),userBanRequestDto.getBanDays());
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"사용자가 정지되었습니다.",userBanRequestDto.getUserId()),HttpStatus.OK);
    }

    //    사용자 정지 해제(관리자 수동 처리)
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @PostMapping("/unban/{userId}")
    public ResponseEntity<?> unbanUser(@PathVariable Long userId){
        adminService.unbanUser(userId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"정지 해제되었습니다..",userId),HttpStatus.OK);
    }

    //    신고 목록 조회
    @GetMapping("/report/list")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<?> getReportList(@RequestHeader("X-User-LoginId") String loginId, Pageable pageable, @ModelAttribute ReportRequestDto reportRequestDto){
        Page<ReportResponseDto> reports = reportService.findAllReports(loginId, pageable,reportRequestDto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"신고 유저 조회 성공",reports),HttpStatus.OK);
    }

    //    신고 상세 조회
    @GetMapping("/report/detail/{reportId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<?> getReportDetails(@PathVariable Long reportId){
        ReportDetailListDto dto = reportService.getReportDetails(reportId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "detail is uploaded successfully",dto),HttpStatus.OK);
    }

    //    특정 신고 처리
    @PostMapping("/report/{reportId}/process")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<?> getReportProcess(@PathVariable Long reportId, @RequestBody @Valid ReportProcessResDto dto){
        ReportProcessResDto reportProcessResDto = reportService.processReport(reportId,dto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "process is uploaded successfully",reportProcessResDto),HttpStatus.OK);
    }


}
