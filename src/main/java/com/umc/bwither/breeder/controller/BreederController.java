package com.umc.bwither.breeder.controller;

import com.umc.bwither._base.apiPayLoad.ApiResponse;
import com.umc.bwither.breeder.dto.BreederResponseDTO;
import com.umc.bwither.breeder.service.BreederService;
import com.umc.bwither.user.dto.BreederJoinDTO;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/breeder")
public class BreederController {
    private final BreederService breederService;

    @GetMapping("/{breederId}/trust-level")
    public ApiResponse<BreederResponseDTO.TrustLevelResponseDTO> getTrustLevel(
            @PathVariable Long breederId) {
        BreederResponseDTO.TrustLevelResponseDTO result = breederService.getTrustLevel(breederId);
        return ApiResponse.onSuccess(result);
    }
}
