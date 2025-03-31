package com.silverpotion.chatserver.chat.repository;

import com.silverpotion.chatserver.chat.domain.ChatRoom;
import com.silverpotion.chatserver.chat.domain.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadStatusRepository extends JpaRepository<ReadStatus, Long> {
    List<ReadStatus> findByChatRoomAndUserId(ChatRoom chatRoom, Long userId);
    Long countByChatRoomAndUserIdAndIsReadFalse(ChatRoom chatRoom, Long userId);
}
