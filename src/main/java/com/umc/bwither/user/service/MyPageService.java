package com.umc.bwither.user.service;

import com.umc.bwither.user.dto.*;

public interface MyPageService {
    UserInfoDTO getUserInfo(Long userId);
    UserInfoDTO updateBreeder(Long id, BreederUpdateDTO breederUpdateDTO);
    UserInfoDTO updateMember(Long id, MemberUpdateDTO userMemberDTO);
    Object getUserReservation(Long userId);
}
