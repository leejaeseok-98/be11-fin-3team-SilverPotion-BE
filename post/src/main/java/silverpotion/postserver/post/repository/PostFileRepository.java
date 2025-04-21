package silverpotion.postserver.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import silverpotion.postserver.common.domain.DelYN;
import silverpotion.postserver.post.domain.PostFile;

import java.util.List;

@Repository
public interface PostFileRepository extends JpaRepository<PostFile,Long> {
    List<PostFile> findByPostId(Long postId);

}

