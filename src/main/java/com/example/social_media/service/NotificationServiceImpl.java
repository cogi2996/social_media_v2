package com.example.social_media.service;

import com.example.social_media.dao.NotificationRepository;
import com.example.social_media.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Override
    public void saveAnnounce(Notification notification) {
        notificationRepository.save(notification);
    }


    @Override
    public List<Notification> findByUserId(int userId, int pageNum, int pageSize, Sort sortBy) {
        return notificationRepository.findByUserId(userId, PageRequest.of(pageNum, pageSize, sortBy));
    }
}
