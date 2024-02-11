package com.example.social_media.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Follow")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "source_id")
    private String sourceId;
    @Column(name = "target_id")
    private String targetId;
    @Column(name = "follow_create_time")
    private Date followCreateTime;
    @Column(name = "follow_status")
    private Boolean followStatus;

}
