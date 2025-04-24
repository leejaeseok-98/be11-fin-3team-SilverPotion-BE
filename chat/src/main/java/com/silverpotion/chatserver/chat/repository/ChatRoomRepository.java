package com.silverpotion.chatserver.chat.repository;

import com.silverpotion.chatserver.chat.domain.ChatRoom;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query(value = """
    SELECT cr.* FROM chat_room cr
    JOIN chat_participant cp ON cp.chat_room_id = cr.id
    WHERE cr.type = 'SINGLE'
      AND cp.user_id IN (:userId1, :userId2)
    GROUP BY cr.id
    HAVING COUNT(DISTINCT cp.user_id) = 2
    """, nativeQuery = true)
    Optional<ChatRoom> findSingleChatRoomByUsers(
            @Param("userId1") Long userId1,
            @Param("userId2") Long userId2);

    @Query("SELECT cr FROM ChatRoom cr JOIN cr.chatParticipants p WHERE p.userId = :userId")
    List<ChatRoom> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM ChatRoom r JOIN ChatParticipant p ON p.chatRoom = r " +
            "WHERE r.title = :title AND r.type = com.silverpotion.chatserver.chat.domain.ChatRoomType.GROUP " +
            "AND p.userId = :userId")
    Optional<ChatRoom> findGroupRoomByTitleAndUser(@Param("title") String title, @Param("userId") Long userId);

}
