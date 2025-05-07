package silverpotion.userserver.userDetailHealthInfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import silverpotion.userserver.userDetailHealthInfo.domain.UserDetailHealthInfo;
import silverpotion.userserver.userDetailHealthInfo.service.UserDetailHealthInfoService;

@Repository
public interface UserDetailHealthInfoRepository extends JpaRepository<UserDetailHealthInfo,Long> {

}
