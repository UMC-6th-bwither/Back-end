package com.umc.bwither.post.entity;

import jakarta.persistence.*;
import lombok.*;

@Data @Entity @Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "block")
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String block;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
