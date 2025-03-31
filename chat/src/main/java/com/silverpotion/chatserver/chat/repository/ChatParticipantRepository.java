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
    List<ChatParticipant> findByChatRoom(ChatRoom chatRoom);
    Optional<ChatParticipant> findByChatRoomAndUserId(ChatRoom chatRoom, Long userId);
    List<ChatParticipant> findAllByUserId(Long userId);

    @Query("""
    SELECT cp1.chatRoom
    FROM ChatParticipant cp1
    JOIN ChatParticipant cp2 ON cp1.chatRoom.id = cp2.chatRoom.id
    WHERE cp1.userId = :myId
      AND cp2.userId = :otherUserId
      AND cp1.chatRoom.isGroupChat = 'N'
""")
    Optional<ChatRoom> findExistingPrivateRoom(@Param("myId") Long myId, @Param("otherUserId") Long otherUserId);

}
