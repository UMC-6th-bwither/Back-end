package com.umc.bwither.animal.service;

import com.umc.bwither.animal.dto.AnimalRequestDTO.AnimalCreateDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO.AnimalDetailDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO.AnimalPreViewListDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO.BookmarkAnimalPreViewListDTO;
import com.umc.bwither.animal.dto.AnimalResponseDTO.BreederAnimalPreViewListDTO;
import com.umc.bwither.animal.entity.enums.AnimalType;
import com.umc.bwither.animal.entity.enums.FileType;
import com.umc.bwither.animal.entity.enums.Gender;
import com.umc.bwither.animal.entity.enums.ParentType;
import com.umc.bwither.animal.entity.enums.Status;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface AnimalService {

  AnimalDetailDTO getAnimalDetail(Long animalId);

  Long animalCreate(long breederId, AnimalCreateDTO animalCreateDTO, Map<FileType, List<MultipartFile>> animalFiles, Map<ParentType, MultipartFile> parentImages, Map<ParentType, List<MultipartFile>> parentHealthCheckImages);

  void animalUpdate(Long animalId, long breederId, AnimalCreateDTO animalCreateDTO, Map<FileType, List<MultipartFile>> animalFiles, Map<ParentType, MultipartFile> parentImages, Map<ParentType, List<MultipartFile>> parentHealthCheckImages);

  boolean isAnimalAuthor(Long animalId, long breederId);

  void bookmarkAnimal(long memberId, Long animalId);

  void unbookmarkAnimal(long memberId, Long animalId);

  BookmarkAnimalPreViewListDTO getBookmarkedAnimals(long memberId, AnimalType animalType, Gender gender, String breed, Status status, Integer page);

  BreederAnimalPreViewListDTO getBreederAnimals(long breederId, Gender gender, String breed, Integer page);

  List<AnimalResponseDTO.MissingAnimalFilesDTO> getAnimalsWithMissingFiles(Long breederId);

  public List<AnimalResponseDTO.AnimalFileStatusDTO> getAnimalFileStatus(Long breederId);

  AnimalPreViewListDTO getAnimalList(List<String> regions, AnimalType animalType, Gender gender, String breed, Status status, String sortField, Integer page);

  void waitAnimal(long memberId, Long animalId);

  void unwaitAnimal(long memberId, Long animalId);

  Boolean checkBookmarkStatus(Long animalId, long memberId);
}
