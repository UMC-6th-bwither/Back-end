package com.umc.bwither.user.dto;

import java.time.LocalDate;

import com.umc.bwither.breeder.entity.enums.EmploymentStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BreedingDTO {
    private Long breedingId;
    private String tradeName;
    private LocalDate joinDate;
    private LocalDate leaveDate;
    private Boolean currentlyEmployed;
    private String breedingDescription;
}
