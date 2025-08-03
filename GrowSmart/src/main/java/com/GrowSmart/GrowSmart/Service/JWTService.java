package com.GrowSmart.GrowSmart.Service;

import com.GrowSmart.GrowSmart.Enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Data
@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access.expiration}") //15 minutes
    private long accessTokenExpiration;

    @Value("${jwt.refresh.expiration}") //7 days
    private long refreshTokenExpiration;
    //    public void printSecretKey() {
//        System.out.println("Base64 Secret Key Used: " + secretKey);
//        System.out.println("Decoded Key (Hex): " + bytesToHex(Decoders.BASE64.decode(secretKey)));
//    }
//
//    private static String bytesToHex(byte[] bytes) {
//        StringBuilder hexString = new StringBuilder();
//        for (byte b : bytes) {
//            hexString.append(String.format("%02x", b));
//        }
//        return hexString.toString();
//    }
    public String generateAccessToken(String username, Set<Role> roles) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("roles", roles.stream().map(Enum::name).toList());

//        printSecretKey();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .and()
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();

    }
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getUrlDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = Jwts.parser() // Correct way in JJWT 0.12.x
                    .verifyWith(getSigningKey()) // Use `verifyWith()` instead of `setSigningKey()`
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // Ensure token is not expired
            return !claims.getExpiration().before(new Date());
        } catch (SignatureException e) {
            return false; // Invalid signature
        } catch (Exception e) {
            return false; // Expired or malformed token
        }
    }
    public long getTokenExpiry(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .getTime() - System.currentTimeMillis();
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generatePasswordResetToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 20 * 60 * 1000)) // 🔹 20 min expiry
                .signWith(getSigningKey())
                .compact();
    }
    public String extractEmailFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())  // Verify signature
                .build()
                .parseSignedClaims(token)  // Parse token
                .getPayload()
                .getSubject();
    }
}
