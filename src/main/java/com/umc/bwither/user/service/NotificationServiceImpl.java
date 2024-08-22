package com.umc.bwither.user.service;

import com.umc.bwither.member.repository.MemberRepository;
import com.umc.bwither.user.dto.NotificationResponseDTO.MemberNotificationDTO;
import com.umc.bwither.user.entity.Notification;
import com.umc.bwither.user.entity.User;
import com.umc.bwither.user.entity.enums.NotificationType;
import com.umc.bwither.user.repository.NotificationRepository;
import com.umc.bwither.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;
  private final MemberRepository memberRepository;

  @Override
  public Notification createNotification(Long userId, NotificationType notificationType, String title, String body) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    Notification notification = Notification.builder()
        .user(user)
        .notificationType(notificationType)
        .isChecked(false)
        .title(title)
        .body(body)
        .build();

    return notificationRepository.save(notification);
  }

  @Override
  public List<MemberNotificationDTO> getUnreadNotifications(Long userId) {
    List<Notification> notifications = notificationRepository.findByUserUserIdAndIsCheckedFalse(userId);

    return notifications.stream()
        .map(notification -> MemberNotificationDTO.builder()
            .notificationId(notification.getNotificationId())
            .notificationType(notification.getNotificationType())
            .isChecked(notification.getIsChecked())
            .title(notification.getTitle())
            .body(notification.getBody())
            .description(notification.getDescription())
            .createdAt(notification.getCreatedAt())
            .build())
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void markNotificationAsRead(Long userId, Long notificationId) {
    Notification notification = notificationRepository.findById(notificationId)
        .orElseThrow(() -> new RuntimeException("알림을 찾을 수 없습니다."));

    if (!notification.getUser().getUserId().equals(userId)) {
      throw new RuntimeException("권한이 없습니다.");
    }

    notification.setIsChecked(true);
    notificationRepository.save(notification);
  }

}
