package com.umc.bwither.user.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BreedingRequestDTO {
    List<BreedingDTO> breedingDTOs;
}
