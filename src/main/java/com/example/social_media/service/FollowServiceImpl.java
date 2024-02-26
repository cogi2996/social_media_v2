package com.example.social_media.service;

import com.example.social_media.dao.FollowRepository;
import com.example.social_media.entity.Follow;
import com.example.social_media.entity.EntityId.FollowId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class FollowServiceImpl implements  FollowService{
    @Autowired
    private FollowRepository followRepository;


    @Override
    public Follow save(Follow follow) {
        return followRepository.save(follow);
    }

    @Override
    public void deleteFollow(FollowId follow) {
        followRepository.deleteById(follow);
    }

    @Override
    public void acceptFollow(FollowId followId) {
        System.out.println("followid: "+followId);
        Optional<Follow> result = followRepository.findById(followId);
        Follow follow = null;
        if(result.isPresent()){
            follow = result.get();
            follow.setFollowStatus(true);
            followRepository.save(follow);
        }
        else{
            throw new RuntimeException("Did not found followId");
        }
    }


}
