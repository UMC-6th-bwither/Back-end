package com.umc.bwither.post.entity;

import com.umc.bwither._base.common.BaseEntity;
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

    @Column(nullable = false, length = 65)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetType petType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Column(nullable = true)
    private Integer scrapCount;

    @Column(nullable = true)
    private Integer viewCount;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
    private List<Block> blocks;

    public static Post create(User user, PetType petType, String title, Category category, List<Block> blocks) {
        Post post = new Post();
        post.user = user;
        post.petType = petType;
        post.title = title;
        post.category = category;
        post.blocks = blocks;
        return post;
    }

}
