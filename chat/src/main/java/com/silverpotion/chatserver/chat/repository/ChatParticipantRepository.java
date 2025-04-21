package com.silverpotion.chatserver.chat.repository;

import com.silverpotion.chatserver.chat.domain.ChatParticipant;
import com.silverpotion.chatserver.chat.domain.ChatRoom;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
    List<ChatParticipant> findByUserId(Long userId);
    Optional<ChatParticipant> findByChatRoomIdAndUserId(Long roomId, Long userId);
    List<ChatParticipant> findByChatRoomIdAndIsConnectedFalse(Long chatRoomId);
    @Query("SELECT c.loginId FROM ChatParticipant c WHERE c.chatRoom.id = :roomId")
    List<String> findLoginIdsByRoomId(@Param("roomId") Long roomId);
}
