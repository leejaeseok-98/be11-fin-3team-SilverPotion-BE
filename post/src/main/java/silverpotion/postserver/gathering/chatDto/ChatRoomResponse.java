package silverpotion.postserver.gathering.chatDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatRoomResponse {
    private Long id;
    private String title;
    private String type;
    private LocalDateTime createdAt;
    private String lastMessageContent;
    private LocalDateTime lastMessageTime;
}
