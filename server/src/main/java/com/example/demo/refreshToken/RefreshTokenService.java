package com.example.demo.refreshToken;

import com.example.demo.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public List<RefreshToken> getTokens() {
        return refreshTokenRepository.findAll();
    }

    public List<RefreshToken> getToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void saveRefreshToken(RefreshToken token) {
        refreshTokenRepository.save(token);
    }

    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

}
