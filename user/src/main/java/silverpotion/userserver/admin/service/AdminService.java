package silverpotion.userserver.admin.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import silverpotion.userserver.admin.domain.Admin;
import silverpotion.userserver.admin.domain.AdminRole;
import silverpotion.userserver.admin.repository.AdminRepository;
import silverpotion.userserver.user.domain.Role;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.user.repository.UserRepository;

@Service
@Transactional
public class AdminService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    public AdminService(AdminRepository adminRepository, UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    public void registerAdmin(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new EntityNotFoundException("해당 유저가 존재하지 않습니다."));

        // 이미 관리자인지 여부 확인
        if (adminRepository.findByUserId(userId).isPresent()){
            throw new IllegalArgumentException("이미 관리자로 등록된 유저입니다.");
        }

        // ADMIN으로 변경
        user.changeRole(Role.ADMIN);

//        ADMIN 테이블에 등록
        Admin admin = Admin.builder()
                .user(user)
                .role(AdminRole.NORMAL_ADMIN) //기본 권한을 일반권한으로 부여
                .name(user.getName())
                .build();
        adminRepository.save(admin);
    }

    public void deleteAdmin(Long userId){
        Admin admin = adminRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("관리자로 등록되어 있지 않습니다."));

        adminRepository.delete(admin);

        User user = admin.getUser();
        user.changeRole(Role.USER);
    }


}
