package com.silverpotion.chatserver.chat.controller;


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

    // ✅ 1.2 채팅방 생성 - 그룹
    @PostMapping("/room/group")
    public ResponseEntity<ChatRoomDto> createGroupRoom(
            @RequestBody CreateChatRoomRequest request,
            HttpServletRequest httpRequest
    ) {
        ChatRoomDto room = chatRoomService.createGroupRoom(request, httpRequest);
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
}
