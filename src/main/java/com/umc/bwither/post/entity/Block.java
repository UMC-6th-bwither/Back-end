package com.umc.bwither.post.entity;

import com.umc.bwither.post.entity.enums.DataType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private DataType dataType;
    @Column
    private String text;
    @Column
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
