package org.example.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.auth.JwtAutorizeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtService {

    @Value("${spring.security.key}")
    private String jwtSecret;

    public JwtAutorizeToken generateAuthToken(String login) {
        JwtAutorizeToken jwtDto = new JwtAutorizeToken();
        jwtDto.setToken(generateJwtToken(login));
        jwtDto.setRefreshToken(generateRefreshToken(login));
        return jwtDto;
    }

    public JwtAutorizeToken generateToken(String login) {
        JwtAutorizeToken jwtAutorizeToken = new JwtAutorizeToken();
        jwtAutorizeToken.setToken(generateJwtToken(login));
        jwtAutorizeToken.setRefreshToken(generateRefreshToken(login));
        return jwtAutorizeToken;
    }

    public JwtAutorizeToken refreshToken(String refreshToken) {
        if (!validateJwtToken(refreshToken)) {
            return null;
        }
        JwtAutorizeToken jwtAutorizeToken = new JwtAutorizeToken();
        String login = getLoginFromToken(refreshToken);
        jwtAutorizeToken.setToken(generateJwtToken(login));
        jwtAutorizeToken.setRefreshToken(generateRefreshToken(login));
        return jwtAutorizeToken;
    }

    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSingInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public String generateJwtToken(String login) {
        Date date = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(login)
                .expiration(date)
                .signWith(getSingInKey())
                .compact();
    }

    public String generateRefreshToken(String login) {
        Date date = Date.from(LocalDateTime.now().plusWeeks(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(login)
                .expiration(date)
                .signWith(getSingInKey())
                .compact();
    }

    private SecretKey getSingInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSingInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        }catch (ExpiredJwtException expEx){
            log.error("Expired JwtException", expEx);
        }catch (UnsupportedJwtException expEx){
            log.error("Unsupported JwtException", expEx);
        }catch (MalformedJwtException expEx){
            log.error("Malformed JwtException", expEx);
        }catch (SecurityException expEx){
            log.error("Security Exception", expEx);
        }catch (Exception expEx){
            log.error("invalid token", expEx);
        }
        return false;
    }

    private String getTokenFromRequest(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String getJwtToken(String authHeader) {
        String jwtToken = getTokenFromRequest(authHeader);

        if (!validateJwtToken(jwtToken)) {
            throw new IllegalArgumentException();
        }
        return jwtToken;
    }


}
