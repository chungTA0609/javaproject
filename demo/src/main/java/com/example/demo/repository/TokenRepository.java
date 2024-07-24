package com.example.demo.repository;

import com.example.demo.entity.Token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query(value = """ 
    select t from Token t inner join User u\s
    on t.id = u.id \s
    where u.id = :userId and (t.expired = false or t.revoked = false)
    """)
    List<Token> findValidTokenByUser(Integer userId);
    Optional<Token> findByToken(String token);
}
