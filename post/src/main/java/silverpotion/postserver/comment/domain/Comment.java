package silverpotion.postserver.comment.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import silverpotion.postserver.common.domain.BaseTimeEntity;
import silverpotion.postserver.common.domain.DelYN;
import silverpotion.postserver.post.domain.Post;
import silverpotion.postserver.post.domain.Vote;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id")
    private Vote vote;

    private Long userId;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Comment> child = new ArrayList<>();

    @OneToMany(mappedBy = "comment" , cascade = CascadeType.ALL)
    private List<CommentLike> commentLike = new ArrayList<>();

    @Builder.Default
    private DelYN delYn = DelYN.N;

    public void updateContent(String content){
        this.content = content;
    }
    public void delete() {
        this.content = "삭제된 댓글입니다.";
        this.delYn= DelYN.Y;
    }
    public void addChild(Comment comment) {
        this.child.add(comment);
    }

}


