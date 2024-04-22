package com.example.social_media.service;

import com.example.social_media.dao.AccountRepository;
import com.example.social_media.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{
    private final AccountRepository accountRepository;
    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }


}
