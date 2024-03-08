package com.example.social_media.service;

import com.example.social_media.dao.UserRepository;
import com.example.social_media.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IUserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findUserById(int userId) {
        User user = userRepository.findUserByUserId(userId);
        return user;
    }

    @Override
    public int findAll(Integer pageNum, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNum, pageSize, Sort.by(sortBy));
        Page<User> pagedResult = userRepository.findAll(paging);
        if(pagedResult.hasContent()){
            return pagedResult.getSize();
        }
        else{
//            return new ArrayList<User>();
            return -1;
        }
    }


}
