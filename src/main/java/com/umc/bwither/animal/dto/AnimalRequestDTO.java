package com.umc.bwither.animal.dto;

import com.umc.bwither.animal.entity.enums.AnimalType;
import com.umc.bwither.animal.entity.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

public class AnimalRequestDTO {

  @Getter
  @Builder
  public static class AnimalCreateDTO {
    @NotBlank
    String name;
    @NotNull
    AnimalType type;
    @NotBlank
    String breed;
    @NotNull
    Gender gender;
    @NotNull
    LocalDate birthDate;
    @NotBlank
    String character;
    @NotBlank
    String feature;
    @NotBlank
    String feeding;
    @NotBlank
    String vaccination;
    @NotBlank
    String virusCheck;
    @NotBlank
    String parasitic;
    @NotBlank
    String healthCheck;
    @NotBlank
    String motherName;
    @NotBlank
    String motherBreed;
    @NotNull
    LocalDate motherBirthDate;
    @NotBlank
    String motherHereditary;
    @NotBlank
    String motherCharacter;
    @NotBlank
    String motherHealthCheck;
    @NotBlank
    String fatherName;
    @NotBlank
    String fatherBreed;
    @NotNull
    LocalDate fatherBirthDate;
    @NotBlank
    String fatherHereditary;
    @NotBlank
    String fatherCharacter;
    @NotBlank
    String fatherHealthCheck;
  }
}
