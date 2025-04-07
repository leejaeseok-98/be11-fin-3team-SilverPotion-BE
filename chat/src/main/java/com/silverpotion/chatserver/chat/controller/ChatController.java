package com.silverpotion.chatserver.chat.controller;


import com.silverpotion.chatserver.chat.dto.ChatMessageDto;
import com.silverpotion.chatserver.chat.dto.ChatRoomListResDto;
import com.silverpotion.chatserver.chat.dto.MyChatListResDto;
import com.silverpotion.chatserver.chat.service.ChatService;
import com.silverpotion.chatserver.chat.service.UserFeign;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "http://localhost:3000") // ✅ 꼭 추가
public class ChatController {
    private final ChatService chatService;
    private final UserFeign userFeign;

    public ChatController(ChatService chatService, UserFeign userFeign) {
        this.chatService = chatService;
        this.userFeign = userFeign;
    }

    // 🔹 그룹 채팅방 개설
    @PostMapping("/room/group/create")
    public ResponseEntity<?> createGroupRoom(@RequestParam String roomName, @RequestHeader("X-User-LoginId")String loginId) {
        if (loginId == null || loginId.isBlank()) {
            return ResponseEntity.badRequest().body("❌ X-User-LoginId 헤더가 없습니다.");
        }

        try {
            Long userId = userFeign.getUserIdByLoginId(loginId);
            chatService.createGroupRoom(roomName, userId);
            return ResponseEntity.ok().build();
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("❌ X-User-LoginId 값이 유효한 숫자가 아닙니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ " + e.getMessage());
        }
    }

    // 🔹 그룹 채팅방 목록 조회
    @GetMapping("/room/group/list")
    public ResponseEntity<?> getGroupChatRooms() {
        List<ChatRoomListResDto> chatRooms = chatService.getGroupchatRooms();
        return ResponseEntity.ok(chatRooms);
    }

    // 🔹 그룹 채팅방 참여
    @PostMapping("/room/group/{roomId}/join")
    public ResponseEntity<?> joinGroupChatRoom(@PathVariable Long roomId, @RequestHeader("X-User-LoginId")String loginId) {
        Long userId = userFeign.getUserIdByLoginId(loginId);

        System.out.println("userId : "+userId);

        try {
            chatService.addParticipantToGroupChat(roomId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ " + e.getMessage());
        }
    }

    // 🔹 이전 메시지 조회
    @GetMapping("/history/{roomId}")
    public ResponseEntity<?> getChatHistory(@PathVariable Long roomId, HttpServletRequest request) {
        String loginId = request.getHeader("X-User-loginId");
        if (loginId == null || loginId.isBlank()) {
            return ResponseEntity.badRequest().body("Missing X-User-LoginId header");
        }

        try {
            Long userId = userFeign.getUserIdByLoginId(loginId);
            List<ChatMessageDto> chatMessageDtos = chatService.getChatHistory(roomId, userId);
            return ResponseEntity.ok(chatMessageDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ " + e.getMessage());
        }
    }

    // 🔹 채팅 메시지 읽음 처리
    @PostMapping("/room/{roomId}/read")
    public ResponseEntity<?> messageRead(@PathVariable Long roomId, @RequestHeader("X-User-LoginId") String loginId) {
        if (loginId == null || loginId.isBlank()) {
            return ResponseEntity.badRequest().body("❌ X-User-LoginId 헤더가 없습니다.");
        }

        try {
            Long userId = userFeign.getUserIdByLoginId(loginId);
            chatService.messageRead(roomId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ " + e.getMessage());
        }
    }

    // 🔹 내 채팅방 목록 조회
    @GetMapping("/my/rooms")
    public ResponseEntity<?> getMyChatRooms(HttpServletRequest request) {
        String userIdHeader = request.getHeader("X-User-LoginId");
        if (userIdHeader == null || userIdHeader.isBlank()) {
            return ResponseEntity.badRequest().body("❌ X-User-LoginId 헤더가 없습니다.");
        }

        try {
            Long userId = Long.parseLong(userIdHeader);
            List<MyChatListResDto> myChatListResDtos = chatService.getMyChatRooms(userId);
            return ResponseEntity.ok(myChatListResDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ " + e.getMessage());
        }
    }

    // 🔹 그룹 채팅방 나가기
    @DeleteMapping("/room/group/{roomId}/leave")
    public ResponseEntity<?> leaveGroupChatRoom(@PathVariable Long roomId, HttpServletRequest request) {
        String userIdHeader = request.getHeader("X-User-LoginId");
        if (userIdHeader == null || userIdHeader.isBlank()) {
            return ResponseEntity.badRequest().body("❌ X-User-LoginId 헤더가 없습니다.");
        }

        try {
            Long userId = Long.parseLong(userIdHeader);
            chatService.leaveGroupChatRoom(roomId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ " + e.getMessage());
        }
    }

    // 🔹 개인 채팅방 개설 or 기존 조회
    @PostMapping("/room/private/create")
    public ResponseEntity<?> createRoom(@RequestParam Long otherUserId, @RequestHeader("X-User-LoginId")String loginId) {
        System.out.println("Controller / otherUserId : "+otherUserId);
        System.out.println("Controller / loginId : "+loginId);

        if (loginId == null || loginId.isBlank()) {
            return ResponseEntity.badRequest().body("Missing X-User-LoginId header");
        }

        try {
            Long currentUserId = userFeign.getUserIdByLoginId(loginId);
            System.out.println("Controller / currentUserId : "+currentUserId);
            Long roomId = chatService.getOrCreatePrivateRoom(currentUserId, otherUserId);
            System.out.println("Controller / : roomId : " +roomId);
            return ResponseEntity.ok(roomId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("User service error: " + e.getMessage());
        }
    }
}
