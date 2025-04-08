package silverpotion.userserver.plan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import silverpotion.userserver.plan.domain.Plan;

import java.util.List;
@Repository
public interface PlanRepository extends JpaRepository<Plan , Long> {
    List<Plan> findByUserLoginId(String userLoginId);
}
