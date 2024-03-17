package com.example.social_media.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="token_id")
    private int id;
    @Column(name="token")
    private String token;
    @Column(name="token_type")
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    @Column(name="expired")
    private boolean expired;
    @Column(name="revoked")
    private boolean revoked;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
