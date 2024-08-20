package com.umc.bwither.post.entity;

import com.umc.bwither._base.common.BaseEntity;
import com.umc.bwither.breeder.entity.Breeder;
import com.umc.bwither.post.entity.enums.Category;
import com.umc.bwither.post.entity.enums.PetType;
import com.umc.bwither.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = true, length = 65)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetType petType;

    @Column(nullable = true)
    private Integer rating;

    @ManyToOne
    @JoinColumn(name = "breeder_id", nullable = true)
    private Breeder breeder;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Builder.Default
    @Column(nullable = true)
    private Integer bookmarkCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer viewCount = 0;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
    private List<Block> blocks;

}
