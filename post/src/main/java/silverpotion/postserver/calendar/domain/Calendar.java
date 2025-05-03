package silverpotion.postserver.calendar.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import silverpotion.postserver.calendar.dtos.CalendarCreateResDto;
import silverpotion.postserver.common.domain.BaseTimeEntity;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class Calendar extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId; //일정 소유자(사용자 입력 or 정모자동등록)

    @Column(nullable = false)
    private String title;//제목
    
    private String description;//설명

    @Column(nullable = false)
    private LocalDateTime start; //시작 날짜

    private LocalDateTime end;

    private boolean allDay; //종일 여부
    
    @Column(nullable = false)
    private String place;//장소
    
    @Column(nullable = false)
    private String source; // 예: 정모, 사용자 입력

}
