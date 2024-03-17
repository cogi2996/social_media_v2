package com.example.social_media.dao;

import com.example.social_media.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query("""
            select t from Token t inner join Account a on t.account.id = a.id
            where a.id = :accountId and (t.expired = false and t.revoked = false )
            """)
    List<Token> findAllValidTokensByAccount(Integer accountId);

    Optional<Token> findByToken(String token);
}
