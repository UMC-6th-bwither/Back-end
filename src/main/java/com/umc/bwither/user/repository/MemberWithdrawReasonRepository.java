package com.umc.bwither.user.repository;

import com.umc.bwither.user.entity.MemberWithdrawReason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberWithdrawReasonRepository extends JpaRepository<MemberWithdrawReason, Long> {
}
