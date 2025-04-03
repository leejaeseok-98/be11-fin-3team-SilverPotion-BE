package silverpotion.postserver.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import silverpotion.postserver.common.domain.DelYN;
import silverpotion.postserver.meeting.domain.MeetingParticipant;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingParticipantRepository extends JpaRepository<MeetingParticipant,Long> {
    List<MeetingParticipant> findByMeetingId(Long meetingId);
}
