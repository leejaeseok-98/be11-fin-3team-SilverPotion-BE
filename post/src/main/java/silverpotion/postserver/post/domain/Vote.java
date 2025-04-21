package silverpotion.postserver.post.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import silverpotion.postserver.common.domain.BaseTimeEntity;
import silverpotion.postserver.common.domain.DelYN;
import silverpotion.postserver.gathering.domain.Gathering;
import silverpotion.postserver.post.dtos.VotePostUpdateDto;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Vote extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId; // 투표id

    private Long writerId; //회원서버에서 요청

    private String title;

    //게시판
    @Enumerated(EnumType.STRING)
    private PostCategory postCategory;

    //삭제여부
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DelYN delYn=DelYN.N;

    private String description;

    private List<String> voteOptions;//투표항목

    private boolean multipleChoice; //복수선택 여부

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private PostStatus postStatus = PostStatus.draft;

    //소모임 아이디(이 게시글이 속하는 소모임의 아이디)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;

    public void update(Long userId,VotePostUpdateDto dto) {
        this.writerId = userId;
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.voteOptions = dto.getVoteOptions();
        this.multipleChoice = dto.isMultipleChoice();
    }

    //  작성자를 한번만 지정할 수 있도록 제약
    public void assignWriter(Long userId) {
        if (this.writerId != null) {
            throw new IllegalStateException("작성자는 이미 설정되어 있습니다.");
        }
        this.writerId = userId;
    }

    public void changeStatus(PostStatus status) {
        this.postStatus = status;
    }
}
