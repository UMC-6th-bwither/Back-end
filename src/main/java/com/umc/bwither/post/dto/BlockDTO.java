package com.umc.bwither.post.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.Map;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockDTO {
    private String id;
    private String type;
    private Map<String, Object> data;
}
