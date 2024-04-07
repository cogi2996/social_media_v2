package com.example.social_media.service;

import com.example.social_media.dao.UserRepository;
import com.example.social_media.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findUserById(int userId) {
        return userRepository.findUserByUserId(userId);
    }

    @Override
    public Page<User> findAll(int pageNum, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNum, pageSize, Sort.by(sortBy));
        return userRepository.findAll(paging);
    }

    @Override
    public List<User> findFollowersByUserId(int userid) {
        return userRepository.findFollowersByUserId(userid);
    }

    @Override
    public List<User> findFollowingUserByUserId(int userId) {
        return userRepository.findFollowingUserByUserId(userId);
    }


    @Override
    public List<User> findPeopleNotFollowedByUserId(int userId,int pageNum, int pageSize) {
        Pageable paging = PageRequest.of(pageNum,pageSize);
        return userRepository.findPeopleNotFollowedByUserId(userId,paging);
    }

    @Override
    public User update(User user) {
        User dbuser = userRepository.findUserByUserId(user.getUserId());
        if (dbuser == null) {
            return null;
        }

        dbuser.setFirstName(user.getFirstName());
        dbuser.setLastName(user.getLastName());
        dbuser.setMidName(user.getMidName());
        if(user.getAvatar() != null)
        {
            dbuser.setAvatar(user.getAvatar());
        }
        dbuser.setDob(user.getDob());
        dbuser.setGender(user.getGender());
        return userRepository.save(dbuser);
    }


}
