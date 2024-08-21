package com.umc.bwither.user.service;

import com.umc.bwither.user.dto.NotificationResponseDTO.MemberNotificationDTO;
import com.umc.bwither.user.entity.Notification;
import com.umc.bwither.user.entity.enums.NotificationType;
import java.util.List;

public interface NotificationService {
  Notification createNotification(Long userId, NotificationType notificationType, String title, String body);

  List<MemberNotificationDTO> getUnreadNotifications(Long userId);
}
