package com.umc.bwither.breeder.service;

import com.umc.bwither._base.apiPayLoad.code.status.ErrorStatus;
import com.umc.bwither._base.apiPayLoad.exception.handler.TestHandler;
import com.umc.bwither.animal.entity.Animal;
import com.umc.bwither.animal.repository.AnimalRepository;
import com.umc.bwither.breeder.dto.BreederFileDTO;
import com.umc.bwither.breeder.dto.BreederResponseDTO;
import com.umc.bwither.breeder.dto.BreederResponseDTO.BreedingAnimalDTO;
import com.umc.bwither.breeder.dto.BreederResponseDTO.TrustLevelResponseDTO;
import com.umc.bwither.breeder.dto.BreederResponseDTO.BreederDetailDTO;
import com.umc.bwither.breeder.dto.BreedingCareerDTO;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.breeder.entity.BreederFile;
import com.umc.bwither.breeder.entity.BreederMember;
import com.umc.bwither.breeder.entity.Breeding;
import com.umc.bwither.breeder.entity.enums.AnimalType;
import com.umc.bwither.breeder.repository.BreederFileRepository;
import com.umc.bwither.breeder.repository.BreederMemberRepository;
import com.umc.bwither.breeder.repository.BreederRepository;
import com.umc.bwither.breeder.repository.BreedingRepository;
import com.umc.bwither.member.entity.Member;
import com.umc.bwither.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BreederServiceImpl implements BreederService {

    private final BreederRepository breederRepository;
    private final BreedingRepository breedingRepository;
    private final BreederFileRepository breederFileRepository;
    private final AnimalRepository animalRepository;
    private final MemberRepository memberRepository;
    private final BreederMemberRepository breederMemberRepository;


    @Override
    public void saveBreeder(final Breeder breeder) {
        breederRepository.save(breeder);
    }

    @Override
    public void saveBreeding(final Breeding breeding) { breedingRepository.save(breeding); }

    @Override
    public void saveBreederFile(final BreederFile breederFile) { breederFileRepository.save(breederFile); }

    @Override
    public BreederDetailDTO getBreederDetail(Long breederId) {
        Breeder breeder = breederRepository.findById(breederId).
                orElseThrow(() -> new TestHandler(ErrorStatus.BREEDER_NOT_FOUND));
        Integer totalAnimals = animalRepository.countByBreeder(breeder);
        Integer careerYear = breedingRepository.findTotalCareerYearsByBreederId(breederId);

        List<BreederFileDTO> files = breeder.getBreederFiles().stream()
                .map(file -> new BreederFileDTO(file.getBreederFileId(), file.getType(), file.getBreederFilePath()))
                .collect(Collectors.toList());

        List<BreedingCareerDTO> breedingCareers = breeder.getBreedingCareer().stream()
                .map(career -> new BreedingCareerDTO(
                        career.getTradeName(),
                        career.getJoinDate().toString(),
                        career.getLeaveDate() != null ? career.getLeaveDate().toString() : null,
                        career.getCurrentlyEmployed(),
                        career.getDescription()
                ))
                .collect(Collectors.toList());

        List<Animal> animals = animalRepository.findByBreeder_BreederId(breederId);

        List<BreedingAnimalDTO> breedingAnimals = animals.stream()
                .map(animal -> new BreedingAnimalDTO(
                        animal.getAnimalId(),
                        animal.getAge(),
                        animal.getGender().name()
                ))
                .collect(Collectors.toList());

        // Todo ReviewDTO

        BreederDetailDTO breederDetailDTO = BreederDetailDTO.builder()
                .breederId(breeder.getBreederId())
                .profileUrl(breeder.getUser().getProfileImage())
                .tradeName(breeder.getTradeName())
                .species(breeder.getSpecies())
                .address(breeder.getUser().getAddress())
                .description(breeder.getDescription())
                .totalAnimals(totalAnimals)
                //.breederRating(breederRating)
                //.reviewCount(reviewCount)
                .careerYear(careerYear)
                .trustLevel(breeder.getTrustLevel())
                .tradePhone(breeder.getTradePhone())
                .snsAddress(breeder.getSnsAddress())
                .detailDescription(breeder.getDescriptionDetail())
                .schoolName(breeder.getSchoolName())
                .departmentName(breeder.getDepartmentName())
                .enrollmentDate(breeder.getEnrollmentDate())
                .graduationDate(breeder.getGraduationDate())
                .questionGuarantee(breeder.getQuestionGuarantee())
                .questionPedigree(breeder.getQuestionPedigree())
                .questionBaby(breeder.getQuestionBaby())
                .questionPeriod(breeder.getQuestionPeriod())
                .questionSupport(breeder.getQuestionSupport())
                .files(files)
                .breedingCareers(breedingCareers)
                .breedingAnimals(breedingAnimals)
                // .reviews(reviews) // ReviewDTO 리스트
                .build();

        return breederDetailDTO;
    }

    @Override
    public void bookmarkBreeder(long memberId, Long breederId) {
        Breeder breeder = breederRepository.findById(breederId)
                .orElseThrow(() -> new TestHandler(ErrorStatus.BREEDER_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new TestHandler(ErrorStatus.MEMBER_NOT_FOUND));
        breederMemberRepository.findByBreederAndMember(breeder, member)
                .ifPresent(mb -> { throw new TestHandler(ErrorStatus.ANIMAL_ALREADY_BOOKMARK); });

        BreederMember breederMember = BreederMember.builder()
                .breeder(breeder)
                .member(member)
                .build();
        breederMemberRepository.save(breederMember);
    }

    @Override
    public void unbookmarkBreeder(long memberId, Long breederId) {
        Breeder breeder = breederRepository.findById(breederId)
                .orElseThrow(() -> new TestHandler(ErrorStatus.BREEDER_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new TestHandler(ErrorStatus.MEMBER_NOT_FOUND));
        BreederMember breederMember = breederMemberRepository.findByBreederAndMember(breeder, member)
                .orElseThrow(() -> new TestHandler(ErrorStatus.BREEDER_NOT_BOOKMARK));

        breederMemberRepository.delete(breederMember);
    }

    @Override
    public BreederResponseDTO.BookmarkBreederPreViewListDTO getBookmarkedBreeders(long memberId, AnimalType type, String species, Integer page) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new TestHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "id"));
        Page<BreederMember> breederMembers = breederMemberRepository.findByMember(member, pageable);
        List<Long> breederIds = breederMembers.stream()
                .map(breederMember -> breederMember.getBreeder().getBreederId())
                .collect(Collectors.toList());

        List<Breeder> breeders = breederRepository.findAllById(breederIds);

        Map<Long, Breeder> breederMap = breeders.stream()
                .collect(Collectors.toMap(Breeder::getBreederId, Function.identity()));

        List<Breeder> sortedBreeders = breederMembers.stream()
                .map(breederMember -> breederMap.get(breederMember.getBreeder().getBreederId()))
                .collect(Collectors.toList());

        if (type != null) {
            sortedBreeders = sortedBreeders.stream()
                    .filter(b -> b.getAnimal().equals(type))
                    .collect(Collectors.toList());
        }
        if (species != null && !species.isEmpty()) {
            sortedBreeders = sortedBreeders.stream()
                    .filter(b -> b.getSpecies() != null && b.getSpecies().stream().anyMatch(s -> s.equalsIgnoreCase(species)))
                    .collect(Collectors.toList());
        }

        // DTO로 변환
        List<BreederResponseDTO.BookmarkBreederDTO> breederDTOs = sortedBreeders.stream()
                .map(b -> BreederResponseDTO.BookmarkBreederDTO.builder()
                        .breederId(b.getBreederId())
                        .profileUrl(b.getBreederFiles().isEmpty() ? null : b.getBreederFiles().get(0).getBreederFilePath())
                        .tradeName(b.getTradeName())
                        .address(b.getUser().getAddress())
                        .build())
                .collect(Collectors.toList());

        // 최종 결과 반환
        return BreederResponseDTO.BookmarkBreederPreViewListDTO.builder()
                .breederList(breederDTOs)
                .listSize(breederDTOs.size())
                .totalPage(breederMembers.getTotalPages())
                .totalElements(breederMembers.getTotalElements())
                .isFirst(breederMembers.isFirst())
                .isLast(breederMembers.isLast())
                .build();
    }

    @Override
    public TrustLevelResponseDTO getTrustLevel(Long breederId) {
        Integer trustLevel = breederRepository.findTrustLevelByBreederId(breederId);

        return TrustLevelResponseDTO.builder()
                .trustLevel(trustLevel)
                .build();
    }
}