package com.umc.bwither.post.entity;

import com.umc.bwither._base.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postFileId;

    @Column(nullable = false)
    private String postFileName;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
