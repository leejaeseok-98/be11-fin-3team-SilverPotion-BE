package silverpotion.postserver.post.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VoteAnswerResDto {
    private List<Long> selectedOptionsIds; // 유저가 선택한 항목들(복수 선택 대응)
    private boolean multipleChoice; //복수선택 여부
    private Long totalParticipants; // 전체 투표 인원 수
    private List<VoteOptionsResDto> options;// 항목 리스트
}
