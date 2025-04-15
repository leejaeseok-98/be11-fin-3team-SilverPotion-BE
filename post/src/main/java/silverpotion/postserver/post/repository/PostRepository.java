package silverpotion.postserver.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import silverpotion.postserver.common.domain.DelYN;
import silverpotion.postserver.post.domain.Post;
import silverpotion.postserver.post.domain.PostCategory;
import silverpotion.postserver.post.domain.PostStatus;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    //소모임에 소속된 유저가 작성한 최신순 게시물 페이징해서 조회
    Page<Post> findByWriterIdIn(List<Long> writerIds, Pageable pageable);

    //게시물 좋아요 수
    @Query("SELECT COUNT(pl) FROM PostLike pl WHERE pl.post.id = :postId")
    Long countPostLikes(@Param("postId") Long postId);
    //게시물 댓글 수
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
    Long countPostComments(@Param("postId") Long postId);

//    게시물 페이징 조회
    Page<Post> findByWriterIdInAndPostCategoryAndDelYnAndPostStatus(List<Long> writerIds,PostCategory postCategory, DelYN delYN, PostStatus postStatus, Pageable pageable);

}
