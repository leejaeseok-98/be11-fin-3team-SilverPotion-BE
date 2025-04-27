package silverpotion.postserver.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import silverpotion.postserver.comment.domain.Comment;
import silverpotion.postserver.post.domain.Post;
import silverpotion.postserver.post.domain.Vote;

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

    // parentId를 이용해 해당 댓글과 연관된 Post 찾기
    @Query("SELECT c.post FROM Comment c WHERE c.id = :parentId")
    Post findPostByParentId(@Param("parentId") Long parentId);

//  일반 게시물 부모 댓글 조회
    List<Comment> findByPostAndParentIsNull(Post post);

//  투표 게시물 부모 댓글 조회
    List<Comment> findByVoteAndParentIsNull(Vote vote);

//   투표게시물 댓글 조회
    @Query("select c.vote from Comment c where c.id = :parentId")
    Vote findVoteByParentId(@Param("parentId") Long parentId);

}