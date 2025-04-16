package silverpotion.postserver.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import silverpotion.postserver.common.domain.DelYN;
import silverpotion.postserver.meeting.domain.Meeting;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting,Long> {
    Optional<Meeting> findByIdAndDelYN(Long id, DelYN delYN);
    List<Meeting> findByGatheringId(Long gatheringId);
}
