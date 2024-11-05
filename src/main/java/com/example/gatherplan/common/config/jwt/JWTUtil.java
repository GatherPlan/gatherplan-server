package com.example.gatherplan.common.config.jwt;

import com.example.gatherplan.appointment.enums.UserAuthType;
import com.example.gatherplan.common.config.jwt.exception.JwtTokenException;
import com.example.gatherplan.common.exception.ErrorCode;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JWTUtil {
    private final SecretKey secretKey;

    public JWTUtil(@Value("${jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public Long getId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("id", Long.class);
    }

    public String getEmail(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    public String getNickName(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("nickname", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public String getUserAuthType(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userAuthType", String.class);
    }

    public void validateTokenExpired(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration();
        } catch (MalformedJwtException e) {
            log.info("Malformed JWT token structure");
            throw new JwtTokenException(ErrorCode.JWT_TOKEN_FORMAT_INVALID);
        } catch (ExpiredJwtException e) {
            log.info("JWT token has expired");
            throw new JwtTokenException(ErrorCode.JWT_TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.info("JWT token is unsupported or has invalid format");
            throw new JwtTokenException(ErrorCode.JWT_TOKEN_UNSUPPORTED_TYPE);
        } catch (SecurityException e) {
            log.info("JWT signature validation failed");
            throw new JwtTokenException(ErrorCode.JWT_TOKEN_SIGNATURE_INVALID);
        } catch (IllegalArgumentException e) {
            log.info("JWT token is null or empty");
            throw new JwtTokenException(ErrorCode.JWT_TOKEN_EMPTY);
        }
    }

    public String createJwt(Long id, String nickname, String email, UserAuthType userAuthType, String role, Long expiredMs) {
        return Jwts.builder()
                .claim("id", id)
                .claim("nickname", nickname)
                .claim("email", email)
                .claim("userAuthType", userAuthType)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
