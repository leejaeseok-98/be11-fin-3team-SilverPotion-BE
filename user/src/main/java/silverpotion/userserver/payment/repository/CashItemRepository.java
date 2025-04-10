package silverpotion.userserver.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import silverpotion.userserver.payment.domain.CashItem;

import java.util.Optional;

@Repository
public interface CashItemRepository extends JpaRepository<CashItem, Long> {

    Optional<CashItem> findByImpUid(String impUid);



}
