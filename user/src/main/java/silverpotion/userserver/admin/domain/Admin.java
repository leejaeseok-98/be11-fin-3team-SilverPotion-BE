package silverpotion.userserver.admin.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import silverpotion.userserver.common.domain.BaseTimeEntity;
import silverpotion.userserver.user.domain.DelYN;
import silverpotion.userserver.user.domain.User;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Admin extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    private AdminRole role; //슈퍼관리자, 일반 관리자

    @Column(nullable = false)
    @Builder.Default
    private DelYN delYN = DelYN.N;


}
