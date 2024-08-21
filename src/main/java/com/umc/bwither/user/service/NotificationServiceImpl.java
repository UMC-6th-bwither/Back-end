package com.umc.bwither.user.service;

import com.umc.bwither.user.entity.Notification;
import com.umc.bwither.user.entity.User;
import com.umc.bwither.user.entity.enums.Category;
import com.umc.bwither.user.repository.NotificationRepository;
import com.umc.bwither.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;

  @Override
  public Notification createNotification(Long userId, Category category, String title, String body) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    Notification notification = Notification.builder()
        .user(user)
        .category(category)
        .isChecked(false)
        .title(title)
        .body(body)
        .build();

    return notificationRepository.save(notification);
  }

}
