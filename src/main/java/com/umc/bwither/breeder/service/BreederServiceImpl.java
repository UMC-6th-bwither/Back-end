package com.umc.bwither.breeder.service;

import com.umc.bwither._base.apiPayLoad.code.status.ErrorStatus;
import com.umc.bwither._base.apiPayLoad.exception.handler.TestHandler;
import com.umc.bwither.animal.entity.Animal;
import com.umc.bwither.animal.entity.enums.FileType;
import com.umc.bwither.animal.repository.AnimalRepository;
import com.umc.bwither.animal.repository.WaitListRepository;
import com.umc.bwither.breeder.dto.BreederFileDTO;
import com.umc.bwither.breeder.dto.BreederResponseDTO;
import com.umc.bwither.breeder.dto.BreederResponseDTO.BreederPreviewDTO;
import com.umc.bwither.breeder.dto.BreederResponseDTO.BreederPreViewListDTO;
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
import com.umc.bwither.post.dto.BlockDTO;
import com.umc.bwither.post.entity.enums.Category;
import com.umc.bwither.post.repository.PostRepository;
import com.umc.bwither.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final WaitListRepository waitListRepository;
    private final PostRepository postRepository;


    @Override
    public void saveBreeder(final Breeder breeder) {
        breederRepository.save(breeder);
    }

    @Override
    public void saveBreeding(final Breeding breeding) { breedingRepository.save(breeding); }

    @Override
    public void saveBreederFile(final BreederFile breederFile) { breederFileRepository.save(breederFile); }

    @Override
    public BreederDetailDTO getBreederDetail(Long breederId, String sortField) {
        Breeder breeder = breederRepository.findById(breederId).
                orElseThrow(() -> new TestHandler(ErrorStatus.BREEDER_NOT_FOUND));
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");  // 기본값: 최신순

        switch (sortField.toLowerCase()) {
            case "rating_asc":
                sort = Sort.by(Sort.Direction.ASC, "rating");  // 별점 낮은 순
                break;
            case "rating_desc":
                sort = Sort.by(Sort.Direction.DESC, "rating");  // 별점 높은 순
                break;
            case "createdAt":
            default:
                break;  // 기본값: 최신순
        }

        int reviewCount = postRepository.countReviewsByBreederId(breeder.getBreederId());

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
                .map(animal -> {
                    // 첫 번째 ANIMAL_IMAGE 타입의 파일 경로 추출
                    Optional<String> animalImage = animal.getAnimalFiles().stream()
                            .filter(file -> file.getType() == FileType.ANIMAL_IMAGE)
                            .map(file -> file.getAnimalFilePath())
                            .findFirst();

                    return new BreedingAnimalDTO(
                            animal.getAnimalId(),
                            animalImage.orElse(null),
                            animal.getName(),
                            animal.getAge(),
                            animal.getGender().name()
                    );
                })
                .collect(Collectors.toList());

        // 종별로 개체 수를 그룹화
        Map<String, Long> animalCountMap = animals.stream()
                .collect(Collectors.groupingBy(
                        Animal::getBreed,  // 종으로 그룹화
                        Collectors.counting() // 각 그룹의 개체 수를 셈
                ));

        // 개체 수 정보를 문자열 리스트로 변환
        List<String> animalCountList = animalCountMap.entrySet().stream()
                .map(entry -> entry.getKey() + " " + entry.getValue() + "마리")
                .collect(Collectors.toList());

        String formattedRating = breeder.getAverageRating() != null
                ? String.format("%.1f", breeder.getAverageRating())
                : "0.0";


        List<BreederResponseDTO.ReviewDTO> reviews = postRepository.findByBreederAndCategory(breeder, Category.BREEDER_REVIEWS, sort)
                .stream()
                .map(review -> {
                    // BlockDTO 리스트 생성
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<BlockDTO> content = review.getBlocks().stream()
                            .map(block -> {
                                try {
                                    // JSON 문자열을 Map으로 변환
                                    Map<String, Object> blockData = objectMapper.readValue(block.getBlock(), Map.class);
                                    return new BlockDTO(
                                            String.valueOf(block.getId()),
                                            (String) blockData.get("type"),
                                            (Map<String, Object>) blockData.get("data")
                                    );
                                } catch (Exception e) {
                                    return new BlockDTO(null, null, null);
                                }
                            })
                            .collect(Collectors.toList());

                    // ReviewDTO 생성
                    return new BreederResponseDTO.ReviewDTO(
                            review.getPostId(),
                            review.getUser().getName(),
                            review.getPetType().name(),
                            review.getRating(),
                            content,
                            review.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());

        ObjectMapper objectMapper = new ObjectMapper();
        User user = breeder.getUser();

        List<BreederResponseDTO.BreederTipsDTO> breederTips = postRepository.findByUserAndCategory(user, Category.TIPS)
                .stream()
                .map(tip -> {
                    // 첫 번째 이미지 URL 추출
                    Optional<String> thumbnailUrl = tip.getBlocks().stream()
                            .map(block -> {
                                try {
                                    // block 필드(JSON 형식의 문자열)를 Map으로 변환
                                    Map<String, Object> blockData = objectMapper.readValue(block.getBlock(), Map.class);
                                    return blockData;
                                } catch (Exception e) {
                                    // 파싱 실패 시 null 반환
                                    return null;
                                }
                            })
                            .filter(blockData -> blockData != null && "image".equals(blockData.get("type")))  // type이 "image"인 블록 필터링
                            .map(blockData -> {
                                Map<String, Object> data = (Map<String, Object>) blockData.get("data");
                                Map<String, Object> fileData = (Map<String, Object>) data.get("file");
                                return (String) fileData.get("url");  // 이미지 URL 추출
                            })
                            .findFirst();  // 첫 번째 이미지 URL만 추출

                    // BreederTipsDTO 생성
                    return new BreederResponseDTO.BreederTipsDTO(
                            tip.getPostId(),
                            tip.getUser().getName(),
                            tip.getTitle(),
                            tip.getUser().getProfileImage(),
                            thumbnailUrl.orElse(null)
                    );
                })
                .collect(Collectors.toList());

        BreederDetailDTO breederDetailDTO = BreederDetailDTO.builder()
                .breederId(breeder.getBreederId())
                .profileUrl(breeder.getUser().getProfileImage())
                .backgroundUrl(breeder.getBackgroundImage())
                .tradeName(breeder.getTradeName())
                .species(breeder.getSpecies())
                .address(breeder.getUser().getAddress())
                .description(breeder.getDescription())
                .totalAnimals(breeder.getTotalAnimals())
                .breederRating(Double.valueOf(formattedRating))
                .reviewCount(reviewCount)
                .careerYear(breeder.getExperienceYears())
                .trustLevel(breeder.getTrustLevel())
                .tradePhone(breeder.getTradePhone())
                .contactableTime(breeder.getContactableTime())
                .snsAddress(breeder.getSnsAddress())
                .detailDescription(breeder.getDescriptionDetail())
                .schoolName(breeder.getSchoolName())
                .departmentName(breeder.getDepartmentName())
                .enrollmentDate(breeder.getEnrollmentDate())
                .graduationDate(breeder.getGraduationDate())
                .kennelAddress(breeder.getUser().getAddress() + " " + breeder.getUser().getAddressDetail())
                .animalCount(animalCountList)
                .businessTime(breeder.getBusinessTime())
                .questionGuarantee(breeder.getQuestionGuarantee())
                .questionPedigree(breeder.getQuestionPedigree())
                .questionBaby(breeder.getQuestionBaby())
                .questionPeriod(breeder.getQuestionPeriod())
                .questionSupport(breeder.getQuestionSupport())
                .files(files)
                .breedingCareers(breedingCareers)
                .breedingAnimals(breedingAnimals)
                .reviews(reviews)
                .breederTips(breederTips)
                .build();

        return breederDetailDTO;
    }

    @Override
    public BreederPreViewListDTO getBreederList(List<String> regions, AnimalType animalType, String species, String sortField, Integer page) {
        Pageable pageable;

        if ("totalAnimals".equals(sortField)) {
            pageable = PageRequest.of(page, 5,
                    Sort.by(Sort.Order.desc("totalAnimals"), Sort.Order.desc("createdAt")));
        } else if ("breederMemberCount".equals(sortField)) {
            pageable = PageRequest.of(page, 5,
                    Sort.by(Sort.Order.desc("breederMemberCount"), Sort.Order.desc("createdAt")));
        } else {
            pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, sortField));
        }

        Page<Breeder> breeders = breederRepository.findAll(pageable);
        List<Breeder> breederList = new ArrayList<>(breeders.getContent());


        if (species != null && !species.isEmpty()) {
            breederList = breederList.stream()
                    .filter(b -> b.getSpecies() != null && b.getSpecies().stream().anyMatch(s -> s.equalsIgnoreCase(species)))
                    .collect(Collectors.toList());
        }

        if (animalType != null) {
            breederList = breederList.stream()
                    .filter(b -> b.getAnimal().equals(animalType))
                    .collect(Collectors.toList());
        }

        if (regions != null && !regions.isEmpty()) {
            breederList = breederList.stream()
                    .filter(b -> regions.stream()
                            .anyMatch(region -> b.getUser().getAddress().contains(region)))
                    .collect(Collectors.toList());
        }

        List<BreederPreviewDTO> breederDTOs = breederList.stream()
                .map(breeder -> {
                    int certificateCount = breederFileRepository.countCertificatesByBreederId(breeder.getBreederId());
                    int waitAnimalCount = waitListRepository.countAnimalsByBreederId(breeder.getBreederId());
                    int waitListCount = waitListRepository.countMembersByBreederId(breeder.getBreederId());
                    int reviewCount = postRepository.countReviewsByBreederId(breeder.getBreederId());
                    String formattedRating = breeder.getAverageRating() != null
                            ? String.format("%.1f", breeder.getAverageRating())
                            : "0.0";

                    return BreederPreviewDTO.builder()
                            .breederId(breeder.getBreederId())
                            .profileUrl(breeder.getUser().getProfileImage())
                            .address(breeder.getUser().getAddress())
                            .breederName(breeder.getTradeName())
                            .animalType(breeder.getAnimal())
                            .species(breeder.getSpecies())
                            .careerYear(breeder.getExperienceYears())
                            .certificateCount(certificateCount)
                            .waitAnimal(waitAnimalCount)
                            .waitList(waitListCount)
                            .breederRating(Double.valueOf(formattedRating))
                            .reviewCount(reviewCount)
                            .createdAt(breeder.getCreatedAt())
                            .updatedAt(breeder.getUpdatedAt())
                            .build();
                })
                .collect(Collectors.toList());

        return BreederPreViewListDTO.builder()
                .breederList(breederDTOs)
                .listSize(breederDTOs.size())
                .totalPage(breeders.getTotalPages())
                .totalElements(breeders.getTotalElements())
                .isFirst(breeders.isFirst())
                .isLast(breeders.isLast())
                .build();
    }

    @Override
    public void bookmarkBreeder(long memberId, Long breederId) {
        Breeder breeder = breederRepository.findById(breederId)
                .orElseThrow(() -> new TestHandler(ErrorStatus.BREEDER_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new TestHandler(ErrorStatus.MEMBER_NOT_FOUND));
        breederMemberRepository.findByBreederAndMember(breeder, member)
                .ifPresent(mb -> { throw new TestHandler(ErrorStatus.BREEDER_ALREADY_BOOKMARK); });

        BreederMember breederMember = BreederMember.builder()
                .breeder(breeder)
                .member(member)
                .build();
        breederMemberRepository.save(breederMember);

        Integer count = breederMemberRepository.countByBreeder(breeder);
        breeder.setBreederMemberCount(count);
        breederRepository.save(breeder);
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

        Integer count = breederMemberRepository.countByBreeder(breeder);
        breeder.setBreederMemberCount(count);
        breederRepository.save(breeder);
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
                .map(b -> {
                    int certificateCount = breederFileRepository.countCertificatesByBreederId(b.getBreederId());
                    int waitAnimalCount = waitListRepository.countAnimalsByBreederId(b.getBreederId());
                    int waitListCount = waitListRepository.countMembersByBreederId(b.getBreederId());
                    int reviewCount = postRepository.countReviewsByBreederId(b.getBreederId());
                    String formattedRating = b.getAverageRating() != null
                            ? String.format("%.1f", b.getAverageRating())
                            : "0.0";
                    return BreederResponseDTO.BookmarkBreederDTO.builder()
                            .breederId(b.getBreederId())
                            .profileUrl(b.getUser().getProfileImage())
                            .breederName(b.getTradeName())
                            .address(b.getUser().getAddress())
                            .animalType(b.getAnimal())
                            .species(b.getSpecies())
                            .careerYear(b.getExperienceYears())
                            .certificateCount(certificateCount)
                            .waitAnimal(waitAnimalCount)
                            .waitList(waitListCount)
                            .breederRating(Double.valueOf(formattedRating))
                            .reviewCount(reviewCount)
                            .build();
                })
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

  @Override
  public Boolean checkBookmarkStatus(Long breederId, Long memberId) {
    Breeder breeder = breederRepository.findById(breederId)
        .orElseThrow(() -> new TestHandler(ErrorStatus.BREEDER_NOT_FOUND));
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new TestHandler(ErrorStatus.MEMBER_NOT_FOUND));

    return breederMemberRepository.existsByMemberAndBreeder(member, breeder);
  }
}