package silverpotion.postserver.gathering.domain;

import jakarta.persistence.*;
import lombok.*;
import silverpotion.postserver.common.domain.BaseTimeEntity;
import silverpotion.postserver.common.domain.DelYN;
import silverpotion.postserver.gatheringCategory.domain.GatheringCategory;
import silverpotion.postserver.gatheringCategory.domain.GatheringDetail;
import silverpotion.postserver.gatheringVector.domain.GatheringVector;
import silverpotion.postserver.meeting.domain.Meeting;
import silverpotion.postserver.post.domain.Post;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Gathering extends BaseTimeEntity {
    //아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //모임 카테고리
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private GatheringCategory gatheringCategory;

    //모임장id
    private Long leaderId;

    //모임명
    @Column(nullable = false)
    private String gatheringName;

    //활동지역(이 부분 더 공부필요 api를 가져오거나 공공데이터포탈에서 전국 지역데 이터 다운받아서 수동으로 db에 넣거나)
    private String region;

    //모임소개
    @Column(nullable = false)
    private String introduce;

    //모임 프로필 사진
    private String imageUrl;

    //정원
    @Column(nullable = false)
    private Long maxPeople;

    @Builder.Default
    @Enumerated(EnumType.STRING )
    private DelYN delYN=DelYN.N;

    @OneToMany(mappedBy = "gathering" )
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "gathering" )
    @Builder.Default
    private List<GatheringDetail> gatheringDetails = new ArrayList<>();

    @OneToMany(mappedBy = "gathering" )
    @Builder.Default
    private List<Meeting> meetings = new ArrayList<>();

    @OneToMany(mappedBy = "gathering", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<GatheringPeople> gatheringPeople = new ArrayList<>();

    @OneToOne(mappedBy = "gathering",cascade = CascadeType.ALL, orphanRemoval = true)
    private GatheringVector gatheringVector;

    private Long chatRoomId; // 💡 채팅방 ID만 저장 (연결용)

    public void addLeaderToGatheringPeople(Long userId) {
        GatheringPeople leader = GatheringPeople.builder()
                .gathering(this)
                .userId(userId)
                .greetingMessage("처음 이 모임을 개설한 사람")
                .status(Status.ACTIVATE)
                .build();
        this.gatheringPeople.add(leader);
    }

    public void changeLeader(Long newLeaderId) {
        this.leaderId = newLeaderId;
    }
}
