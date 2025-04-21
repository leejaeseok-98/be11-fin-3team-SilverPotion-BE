package silverpotion.postserver.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import silverpotion.postserver.post.domain.VoteAnswer;

@Repository
public interface VoteAnswerRepository extends JpaRepository<VoteAnswer, Long> {
//    참여여부
    @Query("SELECT COUNT(va) > 0 FROM VoteAnswer va " +
            "WHERE va.userId = :userId AND va.voteOption.vote.voteId = :voteId")
    boolean existsByUserIdAndVoteId(@Param("userId") Long userId, @Param("voteId") Long voteId);

//    참여자수
    @Query("SELECT COUNT(DISTINCT va.userId) FROM VoteAnswer va " +
            "WHERE va.voteOption.vote.voteId = :voteId")
    Long countDistinctUserByVoteId(@Param("voteId") Long voteId);
}
