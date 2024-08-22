package com.umc.bwither.user.service;

import com.umc.bwither.user.dto.WithdrawDTO;
import com.umc.bwither.user.entity.BreederWithdrawReason;
import com.umc.bwither.user.entity.MemberWithdrawReason;
import com.umc.bwither.user.entity.User;
import com.umc.bwither.user.entity.enums.BreederReasonType;
import com.umc.bwither.user.entity.enums.MemberReasonType;
import com.umc.bwither.user.entity.enums.Role;
import com.umc.bwither.user.entity.enums.Status;
import com.umc.bwither.user.repository.BreederWithdrawReasonRepository;
import com.umc.bwither.user.repository.MemberWithdrawReasonRepository;
import com.umc.bwither.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MemberWithdrawReasonRepository memberWithdrawReasonRepository;
    private final BreederWithdrawReasonRepository breederWithdrawReasonRepository;

    public User create(User user) {
        if (user == null || user.getUsername() == null) {
            throw new RuntimeException("유효하지 않은 사용자 정보입니다.");
        }
        final String username = user.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }
        return userRepository.save(user);
    }

    public User getByCredentials(final String username, final String password,
                                 final PasswordEncoder encoder){
        final User originalUser = userRepository.findByUsername(username);

        if (originalUser == null) {
            log.error("No user found with username: {}", username);
            return null;
        }

        if(encoder.matches(password, originalUser.getPassword())) {
            return originalUser;
        } else {
            log.error("Password mismatch for user: {}", username);
        }

        return null;
    }

    public void withdrawUser(WithdrawDTO.MemberWithdrawDTO withdrawDTO) {
        User user = userRepository.findById(withdrawDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));

        // 탈퇴 사유 저장
        for (String reason : withdrawDTO.getReasons()) {
            MemberWithdrawReason memberWithdrawReason = MemberWithdrawReason.builder()
                    .user(user)
                    .reasonType(MemberReasonType.valueOf(reason))
                    .additionalComment(withdrawDTO.getAdditionalComment())
                    .build();

            memberWithdrawReasonRepository.save(memberWithdrawReason);
        }

        // 유저 정보 업데이트
        user.setStatus(Status.INACTIVE);
        user.setName("탈퇴한 회원");
        user.setWithdrawalDate(LocalDateTime.now());
        userRepository.save(user);
    }


    public void withdrawBreeder(WithdrawDTO.BreederWithdrawDTO withdrawDTO) {
        User user = userRepository.findById(withdrawDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));

        // 탈퇴 사유 저장
        for (String reason : withdrawDTO.getReasons()) {
            BreederWithdrawReason breederWithdrawReason = BreederWithdrawReason.builder()
                    .user(user)
                    .reasonType(BreederReasonType.valueOf(reason))
                    .additionalComment(withdrawDTO.getAdditionalComment())
                    .build();

            breederWithdrawReasonRepository.save(breederWithdrawReason);
        }

        // 유저 정보 업데이트
        user.setStatus(Status.INACTIVE);
        user.setName("탈퇴한 회원");
        user.setWithdrawalDate(LocalDateTime.now());
        userRepository.save(user);
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void deleteInactiveUsers() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<User> usersToDelete = userRepository.findByStatusAndWithdrawalDateBefore(Status.INACTIVE, sevenDaysAgo);

        userRepository.deleteAll(usersToDelete);
    }

    public boolean checkUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
