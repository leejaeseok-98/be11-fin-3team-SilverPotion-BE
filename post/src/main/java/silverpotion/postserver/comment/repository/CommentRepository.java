package silverpotion.postserver.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import silverpotion.postserver.comment.domain.Comment;
import silverpotion.postserver.post.domain.Post;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
//    해당 게시물의 댓글 수 조회
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
    Long countPostComments(@Param("postId") Long postId);
//  게시물의 댓글 조회
    List<Comment> findByPost(Post post);

//    댓글좋아요수 조회
    @Query("SELECT COUNT(cl) FROM CommentLike cl WHERE cl.comment.id = :commentId")
    Long countCommentLikes(@Param("commentId") Long commentId);
}