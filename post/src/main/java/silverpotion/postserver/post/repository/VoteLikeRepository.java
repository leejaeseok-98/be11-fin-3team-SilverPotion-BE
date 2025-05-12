package silverpotion.postserver.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import silverpotion.postserver.post.domain.Post;
import silverpotion.postserver.post.domain.PostLike;
import silverpotion.postserver.post.domain.Vote;
import silverpotion.postserver.post.domain.VoteLike;

import java.util.Optional;

@Repository
public interface VoteLikeRepository extends JpaRepository<VoteLike, Long> {
    //    게시물의 좋아요 수
    Long countByVote(Vote vote);

    // 해당 투표(voteId)의 좋아요 수 조회
    @Query("SELECT COUNT(vl) FROM VoteLike vl WHERE vl.vote.voteId = :voteId")
    Long countByVoteId(@Param("voteId") Long voteId);

    //    로그인한 유저가 해당 게시글에 좋아요 눌렀는지 여부
    boolean existsByVoteAndUserId(Vote vote, Long userId);

    //    특정유저가 특정게시물에 좋아요 눌렀는지 여부
    Optional<VoteLike> findByVoteAndUserId(Vote vote, Long userId);

    //    특정 게시물 좋아요 유저 목록
    @Query("SELECT vl.userId FROM VoteLike vl WHERE vl.vote.voteId = :voteId")
    Page<Long> findUserIdsByPostId(@Param("voteId") Long voteId, Pageable pageable);
}
