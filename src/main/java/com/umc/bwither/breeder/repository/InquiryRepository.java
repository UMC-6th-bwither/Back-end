package com.umc.bwither.breeder.repository;

import com.umc.bwither.breeder.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByUserUserId(Long userId);
    Optional<Inquiry> findByUserUserIdAndBreederBreederId(Long userId, Long breederId);

}
