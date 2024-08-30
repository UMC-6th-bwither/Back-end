package com.umc.bwither.breeder.service;

import com.umc.bwither.breeder.dto.BreederResponseDTO;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.entity.Inquiry;
import com.umc.bwither.breeder.repository.BreederRepository;
import com.umc.bwither.breeder.repository.InquiryRepository;
import com.umc.bwither.user.entity.User;
import com.umc.bwither.user.entity.enums.NotificationType;
import com.umc.bwither.user.repository.UserRepository;
import com.umc.bwither.user.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {
    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;
    private final BreederRepository breederRepository;
    private final NotificationService notificationService;

    @Override
    public void createInquiry(Long userId, Long breederId) {
        Breeder breeder = breederRepository.findById(breederId)
                .orElseThrow(() -> new IllegalArgumentException("해당 브리더가 존재하지 않습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));

        // 기존에 동일한 문의가 있는지 확인
        Optional<Inquiry> existingInquiry = inquiryRepository.findByUserUserIdAndBreederBreederId(userId, breeder.getBreederId());

        if (existingInquiry.isPresent()) {
            throw new RuntimeException("이미 존재하는 문의입니다.");
        }

        // 문의 생성
        Inquiry inquiry = Inquiry.builder()
                .user(user)
                .breeder(breeder)
                .build();

        inquiryRepository.save(inquiry);

        // 알림
        notificationService.createNotification(breeder.getUser().getUserId(), NotificationType.INQUIRY, "새로운 문의 요청",user.getName()+"님이 회원님께 문의를 요청했어요!");
    }

    @Override
    public List<BreederResponseDTO.BreederInquiryDTO> getBreedersByUserId(Long userId) {
        List<Inquiry> inquiries = inquiryRepository.findByUserUserId(userId);
        log.info("요청:"+inquiries);

        // 브리더 ID 리스트 추출
        List<Long> breederIds = inquiries.stream()
                .map(inquiry -> inquiry.getBreeder().getBreederId())
                .distinct()
                .collect(Collectors.toList());

        // 브리더 ID로 브리더 정보 조회
        List<Breeder> breeders = breederRepository.findAllById(breederIds);

        // Breeder 엔티티를 BreederPreviewDTO로 변환
        List<BreederResponseDTO.BreederInquiryDTO> BreederInquiryDTO = breeders.stream()
                .map(breeder -> BreederResponseDTO.BreederInquiryDTO.builder()
                        .breederId(breeder.getBreederId())
                        .profileUrl(breeder.getBreederFiles().isEmpty() ? null : breeder.getBreederFiles().get(0).getBreederFilePath()) // profileUrl 필드가 존재한다고 가정
                        .address(breeder.getUser().getAddress())
                        .breederName(breeder.getTradeName())
                        .tradePhone(breeder.getTradePhone())
                        .createdAt(breeder.getUser().getCreatedAt())
                        .updatedAt(breeder.getUser().getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        return BreederInquiryDTO;
    }
}
