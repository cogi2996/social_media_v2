package com.example.social_media.dao;

import com.example.social_media.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer>{
    Optional<Account> findByEmail(String email);
}
