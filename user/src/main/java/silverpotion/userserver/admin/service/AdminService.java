package silverpotion.userserver.admin.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import silverpotion.userserver.admin.domain.Admin;
import silverpotion.userserver.admin.domain.AdminRole;
import silverpotion.userserver.admin.dtos.AdminUserListDto;
import silverpotion.userserver.admin.dtos.UserDetailDto;
import silverpotion.userserver.admin.dtos.UserSearchDto;
import silverpotion.userserver.admin.repository.AdminRepository;
import silverpotion.userserver.careRelation.repository.CareRelationRepository;
import silverpotion.userserver.user.domain.Role;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AdminService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final CareRelationRepository careRelationRepository;

    public AdminService(AdminRepository adminRepository, UserRepository userRepository, CareRelationRepository careRelationRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.careRelationRepository = careRelationRepository;
    }

//    관리자 등록
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

//    관리자 삭제
    public void deleteAdmin(Long userId){
        Admin admin = adminRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("관리자로 등록되어 있지 않습니다."));

        adminRepository.delete(admin);

        User user = admin.getUser();
        user.changeRole(Role.USER);
    }

//    유저 목록 조회
    public Page<AdminUserListDto> userList(Pageable pageable, UserSearchDto dto){
        Specification<User> spec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (dto.getName() != null) {
                    predicates.add(criteriaBuilder.like(root.get("name"), "%" + dto.getName() + "%"));

                } if (dto.getEmail() != null) {
                    predicates.add(criteriaBuilder.like(root.get("email"), "%" + dto.getEmail() + "%"));

                } if (dto.getNickname() != null) {
                    predicates.add(criteriaBuilder.like(root.get("nickname"), "%" + dto.getNickname() + "%"));

                }
                return predicates.isEmpty() ? criteriaBuilder.conjunction() : criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
        return userRepository.findAll(spec,pageable).map(user-> user.fromEntity());
    }

//    유저상세조회
    public UserDetailDto userDetailList(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new EntityNotFoundException("없는 유저입니다."));
        int dependentCount = careRelationRepository.countProtectorsByDependentId(userId);
        int protectorCount = careRelationRepository.countDependentsByProtectorId(userId);

        return UserDetailDto.detailList(user,dependentCount,protectorCount);
    }
}
