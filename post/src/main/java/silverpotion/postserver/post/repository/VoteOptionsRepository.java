package silverpotion.postserver.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import silverpotion.postserver.post.domain.VoteOptions;

import java.util.List;

@Repository
public interface VoteOptionsRepository extends JpaRepository<VoteOptions, Long> {

    List<VoteOptions> findByOptionText(String optionText);
}
