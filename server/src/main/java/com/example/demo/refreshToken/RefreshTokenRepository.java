package com.example.demo.refreshToken;

import com.example.demo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokenRepository  extends JpaRepository<RefreshToken, Long> {
    public List<RefreshToken> findByToken(String token);
    public Long deleteByToken(String token);
}
