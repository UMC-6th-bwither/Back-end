package com.umc.bwither.user.controller;

import com.umc.bwither._base.apiPayLoad.ApiResponse;
import com.umc.bwither._base.apiPayLoad.code.status.SuccessStatus;
import com.umc.bwither._base.common.UserAuthorizationUtil;
import com.umc.bwither.user.dto.NotificationResponseDTO.MemberNotificationDTO;
import com.umc.bwither.user.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

  private final NotificationService notificationService;
  private final UserAuthorizationUtil userAuthorizationUtil;

  @GetMapping("/unread")
  @Operation(summary = "새로운 알림(읽지 않은 알림) 목록 조회 API", description = "새로운 알림(읽지 않은 알림) 목록을 조회하는 API")
  public ApiResponse<List<MemberNotificationDTO>> getUnreadNotifications() {
    Long userId = userAuthorizationUtil.getCurrentUserId();
    List<MemberNotificationDTO> notifications = notificationService.getUnreadNotifications(userId);
    return ApiResponse.of(SuccessStatus.SUCCESS_GET_NOTIFICATIONS, notifications);
  }

  @PatchMapping("/{notificationId}/read")
  @Operation(summary = "알림 읽음 상태로 변경 API", description = "특정 알림을 읽음 상태로 변경하는 API")
  public ApiResponse markNotificationAsRead(
      @PathVariable Long notificationId) {
    Long userId = userAuthorizationUtil.getCurrentUserId();
    notificationService.markNotificationAsRead(userId, notificationId);
    return ApiResponse.onSuccess(SuccessStatus.SUCCESS_UPDATE_NOTIFICATION);
  }

}
