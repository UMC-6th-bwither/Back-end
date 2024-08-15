package com.umc.bwither.user.service;

import com.umc.bwither.user.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface MyPageService {
    UserInfoDTO getUserInfo(Long userId);
    UserInfoDTO updateBreeder(Long id, BreederUpdateDTO breederUpdateDTO);
    UserInfoDTO updateMember(Long id, MemberUpdateDTO userMemberDTO);
    Object getUserReservation(Long userId);
    void saveAnimalView(Long userId, Long animalId, HttpServletRequest request, HttpServletResponse response);
    List<Long> getRecentViews(Long userId, HttpServletRequest request);
}
