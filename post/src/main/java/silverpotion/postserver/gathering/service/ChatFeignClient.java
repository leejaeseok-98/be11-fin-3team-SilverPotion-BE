package silverpotion.postserver.gathering.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import silverpotion.postserver.gathering.chatDto.AddChatParticipantRequest;
import silverpotion.postserver.gathering.chatDto.ChatRoomCreateRequest;
import silverpotion.postserver.gathering.chatDto.ChatRoomResponse;

@FeignClient(name = "chat-service")
public interface ChatFeignClient {

    @PostMapping("/chat/room/group")
    ChatRoomResponse createGroupRoom(@RequestBody ChatRoomCreateRequest request);
    // 참여자 추가
    @PostMapping("/chat/room/participant")
    void addParticipant(@RequestBody AddChatParticipantRequest request);
    // 참여자 삭제
    @DeleteMapping("/chat/del/room/{roomId}/participant")
    void removeParticipant(
            @PathVariable("roomId") Long chatRoomId,
            @RequestParam("userId") Long userId
    );
    // 그룹채팅방 소프트 삭제
    @PostMapping("/chat/del/room/{roomId}")
    void deleteChatRoom(@PathVariable("roomId") Long chatRoomId);

    @GetMapping("/chat/room/group/existing")
    ChatRoomResponse findExistingGroupRoom(
            @RequestParam("title") String title,
            @RequestParam("userId") Long userId
    );
}
