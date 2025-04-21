package silverpotion.postserver.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import silverpotion.postserver.common.domain.DelYN;
import silverpotion.postserver.post.domain.Post;
import silverpotion.postserver.post.domain.PostCategory;
import silverpotion.postserver.post.domain.PostStatus;
import silverpotion.postserver.post.domain.Vote;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {
    Optional<Vote> findVoteByVoteId(Long voteId);

    //게시물 댓글 수
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.vote.voteId = :voteId")
    Long countVoteComments(@Param("voteId") Long voteId);

    //    게시물 페이징 조회
    Page<Vote> findByWriterIdInAndPostCategoryAndDelYnAndPostStatus(List<Long> writerIds, PostCategory postCategory, DelYN delYN, PostStatus postStatus, Pageable pageable);


}
