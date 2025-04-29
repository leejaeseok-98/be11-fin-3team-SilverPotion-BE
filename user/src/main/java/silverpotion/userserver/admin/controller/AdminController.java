package silverpotion.userserver.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import silverpotion.userserver.admin.service.AdminService;
import silverpotion.userserver.common.dto.CommonDto;

@RestController
@RequestMapping("/silverpotion/admins")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/{userId}")
    public ResponseEntity<?> register(@PathVariable Long userId) {
        adminService.registerAdmin(userId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "관리자 등록 완료",userId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long userId,@RequestHeader("X-User-LoginId")String loginId){
        adminService.deleteAdmin(userId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "관리자 삭제 완료",userId), HttpStatus.OK);
    }


}
