package silverpotion.postserver.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import silverpotion.postserver.post.domain.PostLike;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike,Long> {
    //해당 게시물의 좋아요수
    @Query("SELECT COUNT(pl) FROM PostLike pl WHERE pl.post.id = :postId")
    Long countPostLikes(@Param("postId") Long postId);

//    로그인한 유저가 해당 게시글에 좋아요 눌렀는지 여부
    boolean existsByPostIdAndUserId(Long postId, Long userId);

}
