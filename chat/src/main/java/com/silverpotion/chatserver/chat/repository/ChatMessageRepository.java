package com.silverpotion.chatserver.chat.repository;

import com.silverpotion.chatserver.chat.domain.ChatMessage;
import com.silverpotion.chatserver.chat.domain.ChatRoom;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Page<ChatMessage> findByChatRoomId(Long chatRoomId, Pageable pageable);
    @Query("SELECT COUNT(m) FROM ChatMessage m " +
            "WHERE m.chatRoom.id = :roomId " +
            "AND m.id > :lastReadMessageId")
    long countUnreadMessages(@Param("roomId") Long roomId, @Param("lastReadMessageId") Long lastReadMessageId);

    @Query("SELECT COUNT(m) FROM ChatMessage m " +
            "WHERE m.chatRoom.id = :roomId")
    long countAllMessages(@Param("roomId") Long roomId); // lastReadMessageId가 null일 때 전체 메시지 수
}
