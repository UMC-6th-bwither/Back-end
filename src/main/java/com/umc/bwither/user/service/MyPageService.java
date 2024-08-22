package com.umc.bwither.user.service;

import com.umc.bwither.breeder.entity.enums.FileType;
import com.umc.bwither.user.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface MyPageService {
    UserInfoDTO getUserInfo(Long userId);
    UserInfoDTO updateBreederProfile(Long id, BreederProfileUpdateDTO breederInfoUpdateDTO);
    void updateBreederInfo(Long id, BreederInfoUpdateDTO breederInfoUpdateDTO, Map<FileType, List<MultipartFile>> breederFiles);
    UserInfoDTO updateMember(Long id, MemberUpdateDTO userMemberDTO);
    void updateMemberProfileImage(Long userId, String profileImage);
    Object getUserReservation(Long userId);
    void saveAnimalView(Long userId, Long animalId, HttpServletRequest request, HttpServletResponse response);
    List<Long> getRecentViews(Long userId, HttpServletRequest request);
    void updateBreederBackgroundImage(Long userId, String backgroundImageUrl);
    void updateBreederBreeding(Long userId, List<BreedingDTO> breeding);
}
