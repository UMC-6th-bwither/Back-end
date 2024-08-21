package com.umc.bwither.user.service;

import com.umc.bwither.user.entity.Notification;
import com.umc.bwither.user.entity.enums.Category;

public interface NotificationService {
  Notification createNotification(Long userId, Category category, String title, String body);
}
