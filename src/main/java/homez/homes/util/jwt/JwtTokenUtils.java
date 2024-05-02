package homez.homes.util.jwt;

import homez.homes.util.DateTimeProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenUtils {
    private final int ACCESS_TOKEN_DURATION = 1;

    private final DateTimeProvider dateTimeProvider;
    private final Key key;

    public JwtTokenUtils(DateTimeProvider dateTimeProvider, @Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = secretKey.getBytes();
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.dateTimeProvider = dateTimeProvider;
    }

    public TokenInfo generateToken() {
        return new TokenInfo("Bearer", generateAccessToken());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty", e);
        } catch (JwtException e) {
            log.info("JWT Token error: " + e.getMessage());
        }
        return false;
    }

    public Authentication getAuthentication(String accessToken) {
        String username = getUsernameFromToken(accessToken);
        User principal = new User(username, "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        return new UsernamePasswordAuthenticationToken(principal, accessToken, principal.getAuthorities());
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    private String generateAccessToken() {
        String username = UUID.randomUUID().toString();
        Claims claims = Jwts.claims().setSubject(username);
        Date accessTokenExpiresIn = dateTimeProvider.getDateAfterDays(ACCESS_TOKEN_DURATION);
        Date date = dateTimeProvider.nowDate();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

}