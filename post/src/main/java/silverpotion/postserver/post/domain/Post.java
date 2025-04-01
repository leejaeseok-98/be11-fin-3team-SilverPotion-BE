package silverpotion.postserver.post.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import silverpotion.postserver.comment.domain.Comment;
import silverpotion.postserver.common.domain.BaseTimeEntity;
import silverpotion.postserver.common.domain.DelYN;
import silverpotion.postserver.gathering.domain.Gathering;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Post extends BaseTimeEntity {
    //아이디
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private Long writerId; //회원서버에서 요청

    private String nickName; //회원서버에서 요청
    //게시판
    @Enumerated(EnumType.STRING)
    private PostCategory postCategory;
    //글 제목
    private String title;
    //글 내용
    private String content;
    //삭제여부
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DelYN delYN=DelYN.N;
    //조회수
    private Integer viewCount = 0;
    //좋아요 수
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PostLike> postLike = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PostFile> postFile = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comment = new ArrayList<>();

    //소모임 아이디(이 게시글이 속하는 소모임의 아이디)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private PostStatus postStatus = PostStatus.draft;

    public void update(String title, String content){
        Post.builder()
                .title(title)
                .content(content)
                .build();
    }

    //  작성자를 한번만 지정할 수 있도록 제약
    public void assignWriter(Long userId) {
        if (this.writerId != null) {
            throw new IllegalStateException("작성자는 이미 설정되어 있습니다.");
        }
        this.writerId = userId;
    }

    public List<String> getFileUrls() {
        return postFile.stream()
                .map(PostFile::getFileUrl)
                .collect(Collectors.toList());
    }

}
