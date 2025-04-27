package silverpotion.postserver.gathering.chatDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddChatParticipantRequest {
    private Long chatRoomId;
    private Long userId;
}
