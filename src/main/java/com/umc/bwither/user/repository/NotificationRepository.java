package com.umc.bwither.user.repository;

import com.umc.bwither.user.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> findByUserUserIdAndIsCheckedFalse(Long userId);

  Long countByUserUserIdAndIsCheckedFalse(Long userId);
}
