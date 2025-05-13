package silverpotion.postserver.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import silverpotion.postserver.post.domain.Post;
import silverpotion.postserver.post.domain.PostLike;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike,Long> {
//    게시물의 좋아요 수
    Long countByPost(Post post);

    //해당 게시물의 좋아요수
    @Query("SELECT COUNT(pl) FROM PostLike pl WHERE pl.post.id = :postId")
    Long countPostLikes(@Param("postId") Long postId);

//    로그인한 유저가 해당 게시글에 좋아요 눌렀는지 여부
    boolean existsByPostIdAndUserId(Long postId, Long userId);

//    특정유저가 특정게시물에 좋아요 눌렀는지 여부
    Optional<PostLike> findByPostAndUserId(Post post, Long userId);

//    특정 게시물 좋아요 유저 목록
    @Query("SELECT pl.userId FROM PostLike pl WHERE pl.post.id = :postId")
    Page<Long> findUserIdsByPostId(@Param("postId") Long postId, Pageable pageable);

}
