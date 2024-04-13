package com.example.social_media.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.Instant;

@Entity(name = "announce")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Announce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String type;

    @CurrentTimestamp
    private Instant create_time;

    @Column
    private boolean status;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne(mappedBy = "announce")
    private AnnounceLikePost announceLikePost;

}