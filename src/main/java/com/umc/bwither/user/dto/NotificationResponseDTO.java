package com.umc.bwither.user.dto;


import com.umc.bwither.user.entity.enums.NotificationType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NotificationResponseDTO {

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class MemberNotificationDTO {
    Long notificationId;
    NotificationType notificationType;
    Boolean isChecked;
    String title;
    String body;
    String description;
    LocalDateTime createdAt;
  }

}
