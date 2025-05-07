package silverpotion.userserver.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import silverpotion.userserver.user.domain.DelYN;
import silverpotion.userserver.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    // 정지 해제 되는 계정 조회
    List<User> findAllByBanYNIsTrueAndBanUntilBefore(LocalDateTime now);

    Optional<User> findByIdAndDelYN(Long id,DelYN delYN);

    Optional<User> findByLoginIdAndDelYN(String loginId, DelYN delYN);

    Optional<User> findByNickNameAndDelYN(String NickName, DelYN delYN);

    Optional<User> findByEmailAndDelYN(String email, DelYN delYN);

    Optional<User> findByPhoneNumberAndDelYN(String phoneNumber, DelYN delYN);

    Optional<User> findByLoginId(String loginId);

    Page<User> findAll(Pageable pageable);

    List<User> findAllByIdInAndDelYN(List<Long> ids, DelYN delYN);

    @Query("SELECT u FROM User u WHERE u.banUntil <= :now AND u.banYN = 'N'")
    List<User> findUsersToBan(@Param("now") LocalDateTime now);

    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
    boolean existsByNickName(String nickname);
}
