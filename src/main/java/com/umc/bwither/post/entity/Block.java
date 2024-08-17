package com.umc.bwither.post.entity;

import jakarta.persistence.*;
import lombok.*;

@Data @Entity @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "json")
    private String block;

    /*@Column
    private DataType dataType;
    @Column
    private String text;
    @Column
    private String imageUrl;*/

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
