package silverpotion.postserver.gathering.chatDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatRoomCreateRequest {
    private List<Long> userIds;
    private String title;
    private String type; // "GROUP" or "SINGLE" 문자열로 전달
}
