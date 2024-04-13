package com.example.social_media.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "announce_like_post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnounceLikePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    @JoinColumn(name = "announce_id")
    private Announce announce;

}
