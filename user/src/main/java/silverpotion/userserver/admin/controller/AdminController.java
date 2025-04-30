package silverpotion.userserver.admin.controller;

import com.google.api.Http;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import silverpotion.userserver.admin.dtos.AdminUserListDto;
import silverpotion.userserver.admin.dtos.UserDetailDto;
import silverpotion.userserver.admin.dtos.UserSearchDto;
import silverpotion.userserver.admin.service.AdminService;
import silverpotion.userserver.common.dto.CommonDto;
import silverpotion.userserver.user.service.UserService;

@RestController
@RequestMapping("/silverpotion/admins")
public class AdminController {
    private final AdminService adminService;
    private final UserService userService;

    public AdminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@PageableDefault(size = 10, sort = "id", direction =Sort.Direction.DESC)Pageable pageable, UserSearchDto dto){
        Page<AdminUserListDto> userListDto = adminService.userList(pageable,dto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "관리자 유저 조회 완료",userListDto),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/detail/{userId}")
    public ResponseEntity<?> detailList(@PathVariable Long userId){
        UserDetailDto userDetailDto = adminService.userDetailList(userId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "관리자 유저 상세 조회 완료",userDetailDto),HttpStatus.OK);
    }

}
