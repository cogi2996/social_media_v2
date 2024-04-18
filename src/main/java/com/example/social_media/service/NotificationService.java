package com.example.social_media.service;

import com.example.social_media.entity.Notification;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface NotificationService {
    void saveAnnounce(Notification notification);

    List<Notification> findByUserId(int userId, int pageNum, int pageSize, Sort sortBy);
}
