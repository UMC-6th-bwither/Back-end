package com.umc.bwither.chat.entity;

import com.umc.bwither._base.common.BaseEntity;
import com.umc.bwither.chat.entity.enums.RoomState;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Column(length = 50)
    private String roomName;

    @Enumerated(EnumType.STRING)
    private RoomState roomState;
}

