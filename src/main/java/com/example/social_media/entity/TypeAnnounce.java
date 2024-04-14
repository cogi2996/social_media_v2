package com.example.social_media.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TypeAnnounce {
    POST("post"),
    LIKE("like"),
    FOLLOW("follow");
    @Getter
    private final String type;
}
