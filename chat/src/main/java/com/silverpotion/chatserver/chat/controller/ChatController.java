package com.silverpotion.chatserver.chat.controller;


import com.silverpotion.chatserver.chat.dto.AddChatParticipantRequest;
import com.silverpotion.chatserver.chat.dto.ChatRoomDto;
import com.silverpotion.chatserver.chat.dto.CreateChatRoomRequest;
import com.silverpotion.chatserver.chat.service.ChatRoomService;
import com.silverpotion.chatserver.chat.service.UserFeign;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "http://localhost:3000") // ✅ 꼭 추가
public class ChatController {
    private final ChatRoomService chatRoomService;
    private final UserFeign userFeign;


    public ChatController(ChatRoomService chatRoomService, UserFeign userFeign) {
        this.chatRoomService = chatRoomService;
        this.userFeign = userFeign;
    }

    // ✅ 1.1 채팅방 생성 - 1:1
    @PostMapping("/room")
    public ResponseEntity<ChatRoomDto> createRoom(
            @RequestBody CreateChatRoomRequest request,
            HttpServletRequest httpRequest
    ) {
        ChatRoomDto room = chatRoomService.createRoom(request, httpRequest);
        return ResponseEntity.ok(room);
    }


    // ✅ 2. 메시지 읽음 처리
    @PatchMapping("/room/{roomId}/read")
    public ResponseEntity<Void> updateLastReadMessage(
            @PathVariable Long roomId,
            @RequestParam Long userId,
            @RequestParam Long messageId) {

        chatRoomService.updateLastReadMessage(roomId, userId, messageId);
        return ResponseEntity.ok().build();
    }
    // ✅ 3. 읽지 않은 메시지 수 조회
    @GetMapping("/room/{roomId}/unread-count")
    public ResponseEntity<Long> getUnreadMessageCount(
            @PathVariable Long roomId,
            @RequestParam Long userId
    ) {
        long count = chatRoomService.getUnreadMessageCount(roomId, userId);
        return ResponseEntity.ok(count);
    }
    // ✅ 4. 전체 채팅방 리스트 조회
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomDto>> getAllRooms(@RequestParam Long userId) {
        List<ChatRoomDto> rooms = chatRoomService.getAllRooms(userId);
        return ResponseEntity.ok(rooms);
    }
    // ✅ 5. 그룹 채팅방 생성 및 조회
    @PostMapping("/room/group")
    public ResponseEntity<ChatRoomDto> createGroupRoom(@RequestBody CreateChatRoomRequest request) {
        ChatRoomDto room = chatRoomService.createGroupRoom(request);
        return ResponseEntity.ok(room);
    }

    // ✅ 6. 그룹 채팅방 회원 추가
    @PostMapping("/room/participant")
    public ResponseEntity<Void> addParticipant(@RequestBody AddChatParticipantRequest request) {
        chatRoomService.addParticipantToRoom(request);
        return ResponseEntity.ok().build();
    }

    // ✅ 7. 그룹채팅방 생성 및 조회시 중복 확인.
    @GetMapping("/room/group/existing")
    public ResponseEntity<ChatRoomDto> findExistingGroupRoom(
            @RequestParam String title,
            @RequestParam Long userId
    ) {
        ChatRoomDto room = chatRoomService.findExistingGroupRoom(title, userId);
        return ResponseEntity.ok(room);
    }

    // ✅ 8. 그룹 채팅방 참여자 제거
    @DeleteMapping("/del/room/{roomId}/participant")
    public ResponseEntity<Void> removeParticipant(
            @PathVariable Long roomId,
            @RequestParam Long userId
    ) {
        chatRoomService.removeParticipantFromRoom(roomId, userId);
        return ResponseEntity.ok().build();
    }

    // ✅ 9. 그룹 채팅방 삭제
    @PostMapping("/del/room/{roomId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable Long roomId) {
        chatRoomService.deleteChatRoom(roomId);
        return ResponseEntity.ok().build();
    }

}
