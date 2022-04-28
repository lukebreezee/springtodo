package com.example.demo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    private final String ACCESS_TOKEN_SECRET = "8a338d92dc33d6662082dd8132c8ad2f179a0e19ebb6c3883e4cf037f1f3ea51a02e7e4b01a6fb8737a60acd28bf749d7c00155c19c85563e5a4fa133528b7d1";
    private final String REFRESH_TOKEN_SECRET = "f60625590e46485bd94cddfdbf506fd5b52ac8c84ade2c73d35ff12dd7e9e27e2e610a9a6e981100eb17e4aab8866a1666dd81f69b544350eac407b78f70b775";

    public String extractId(String token) {
        return extractClaim(token, Claims::getId);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(ACCESS_TOKEN_SECRET).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String id) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, id);
    }

    public String generateRefreshToken(String id) {
        Map<String, Object> claims = new HashMap<>();
        return createRefreshToken(claims, id);
    }

    private String createToken(Map<String, Object> claims, String id) {
        return Jwts.builder().setClaims(claims).setId(id).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(SignatureAlgorithm.HS256, ACCESS_TOKEN_SECRET).compact();
    }

    private String createRefreshToken(Map<String, Object> claims, String id) {
        return Jwts.builder().setClaims(claims).setId(id).setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, REFRESH_TOKEN_SECRET).compact();
    }

    public String extractRefreshId(String token) {
        return extractRefreshClaim(token, Claims::getId);
    }

    public <T> T extractRefreshClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllRefreshClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllRefreshClaims(String token) {
        return Jwts.parser().setSigningKey(REFRESH_TOKEN_SECRET).parseClaimsJws(token).getBody();
    }

    public Boolean validateToken(String token, String id) {
        final String tokenId = extractId(token);
        return (tokenId.equals(id) && !isTokenExpired(token));
    }

    public Boolean validateRefreshToken(String token, String id) {
        final String tokenId = extractRefreshId(token);
        return (tokenId.equals(id));
    }

}
