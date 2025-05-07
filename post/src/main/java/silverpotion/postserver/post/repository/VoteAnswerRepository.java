package silverpotion.postserver.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import silverpotion.postserver.post.domain.Vote;
import silverpotion.postserver.post.domain.VoteAnswer;
import silverpotion.postserver.post.domain.VoteOptions;

import java.util.List;

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

    // 내가 선택한 항목들만 찾는 쿼리
    @Query("SELECT va.voteOption.id FROM VoteAnswer va WHERE va.userId = :userId AND va.voteOption.vote.voteId = :voteId")
    List<Long> findOptionIdsByUserIdAndVoteId(@Param("userId") Long userId, @Param("voteId") Long voteId);

    // 옵션별 투표 수 (득표 수)
    Long countByVoteOptionId(Long optionId);

    // 다시 투표 시, 기존 투표 모두 삭제
    void deleteByUserIdAndVoteOption_Vote_VoteId(Long userId, Long voteId);

    // 옵션 ID로 답변 전체 조회
    List<VoteAnswer> findByVoteOption_Id(Long voteOptionId);

    //로그인 유저가 선택한 투표 항목 조회
    List<VoteAnswer> findAllByUserIdAndVoteOption_Vote_VoteId(Long userId, Long voteId);

    //투표 결과 항목 조회
    @Query("SELECT va.voteOption FROM VoteAnswer va WHERE va.voteOption.vote = :vote AND va.userId = :userId")
    List<VoteOptions> findSelectedOptionsByVoteAndUserId(@Param("vote") Vote vote, @Param("userId") Long userId);
}
