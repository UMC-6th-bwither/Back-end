package com.umc.bwither.member.service;

import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.repository.BreederRepository;
import com.umc.bwither.member.dto.BreederViewedDTO;
import com.umc.bwither.member.entity.BreederViewed;
import com.umc.bwither.member.entity.Member;
import com.umc.bwither.member.repository.BreederViewedRepository;
import com.umc.bwither.member.repository.MemberRepository;
import com.umc.bwither.user.entity.User;
import com.umc.bwither.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final BreederViewedRepository breederViewedRepository;

    @Override
    public void saveMember(final Member member) {
        memberRepository.save(member);
    }

    public User getByCredentials(final String username, final String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public User getByCredentials(final String username, final String password,
                                 final PasswordEncoder encoder){
        final User originalUser = userRepository.findByUsername(username);

        if(originalUser != null && encoder.matches(password, originalUser.getPassword())) {
            return originalUser;
        }

        return null;
    }

    @Override
    public void addView(Long memberId, Long breederId) throws Exception {
        try {
            BreederViewed view = new BreederViewed();
            view.setMember(new Member(memberId));
            view.setBreeder(new Breeder(breederId));
            view.setViewedAt(LocalDateTime.now());
            breederViewedRepository.save(view);
        } catch (Exception e) {
            throw new Exception("Failed to record breeder view.", e);
        }
    }
    @Override
    public List<BreederViewedDTO> getRecentViews(Long memberId) throws Exception {

        if (!memberRepository.existsByMemberId(memberId)) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        return breederViewedRepository.findByMember_MemberIdOrderByViewedAtDesc(memberId)
                .stream()
                .filter(distinctByKey(breederViewed -> breederViewed.getBreeder().getBreederId()))  // breederId를 기준으로 중복 제거
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private <T> java.util.function.Predicate<T> distinctByKey(java.util.function.Function<? super T, Object> keyExtractor) {
        java.util.Set<Object> seen = java.util.concurrent.ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private BreederViewedDTO convertToDTO(BreederViewed breederViewed) {
        return BreederViewedDTO.builder()
                .breederId(breederViewed.getBreeder().getBreederId())
                .breederName(breederViewed.getBreeder().getTradeName())
                .viewedAt(breederViewed.getViewedAt())
                .build();
    }
}
