package com.umc.bwither._base.aws.s3;

import com.umc.bwither._base.aws.s3.Uuid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UuidRepository extends JpaRepository<Uuid, Long> {
}