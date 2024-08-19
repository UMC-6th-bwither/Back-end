package com.umc.bwither.breeder.service;

import com.umc.bwither.breeder.dto.BreederResponseDTO;

import java.util.List;

public interface InquiryService {
    void createInquiry(Long userId, Long breederUserId);
    List<BreederResponseDTO.BreederPreviewDTO> getBreedersByUserId(Long userId);
}
