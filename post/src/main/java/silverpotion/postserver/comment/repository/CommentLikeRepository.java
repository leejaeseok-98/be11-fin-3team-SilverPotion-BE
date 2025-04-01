package silverpotion.postserver.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import silverpotion.postserver.comment.domain.Comment;
import silverpotion.postserver.comment.domain.CommentLike;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

// 해당 댓글에 사용자가 좋아요 눌렀는지 조회
    Optional<CommentLike> findByCommentAndUserId(Comment comment, Long userId);
//  댓글 수 조회
    Long countByComment(Comment comment);
//  댓글에 유자가 좋아요가 있는지 확인
    boolean existsByCommentIdAndUserId(Long commentId, Long userId);
//  댓글에 좋아요 누른 유저 페이징네이션 조회
    @Query("SELECT cl.userId FROM CommentLike cl WHERE cl.comment.id = :commentId")
    Page<Long> findUsersWhoLikedComment(@Param("commentId") Long commentId, Pageable pageable);


}

