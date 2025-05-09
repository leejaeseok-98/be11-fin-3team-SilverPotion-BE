package com.silverpotion.chatserver.notification.repository;

import com.silverpotion.chatserver.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByLoginIdAndIsReadFalseOrderByCreatedAtDesc(String loginId);
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.id = :notificationId")
    void markAsReadById(@Param("notificationId") Long notificationId);
}
