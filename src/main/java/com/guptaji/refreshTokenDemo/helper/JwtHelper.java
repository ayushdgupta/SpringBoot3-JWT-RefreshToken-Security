package com.guptaji.refreshTokenDemo.helper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtHelper {

  Logger LOG = LogManager.getLogger(JwtHelper.class);

  @Value("${mySecret}")
  private String secretKey;

  public String generateToken(String userName) {
    LOG.info("Generating the JWT Token for {}", userName);
    Map<String, Object> claims = new HashMap<>();
    claims.put("Issuer", "Guptaji");
    String token = createJwtToken(claims, userName);
    return token;
  }

  private String createJwtToken(Map<String, Object> claims, String userName) {
    LOG.info("Creating the token for the user {}", userName);
    String jwtToken =
        Jwts.builder()
            .setClaims(claims)
            .setSubject(userName)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(
                new Date(System.currentTimeMillis() + 1000 * 60 * 3)) // token will expire in 3 min.
            .signWith(generateSignedKey(), SignatureAlgorithm.HS256)
            .compact();
    return jwtToken;
  }

  private Key generateSignedKey() {
    // Here the secretKey is fetched from env. variables and I generated that key using one
    // code from the link --
    // https://cloud.google.com/storage/docs/samples/storage-generate-encryption-key#storage_generate_encryption_key-java
    // which is in base64 format.
    byte[] byteKey = secretKey.getBytes();
    return Keys.hmacShaKeyFor(byteKey);
  }

  public String extractUserName(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpirationTime(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public boolean isTokenExpired(String token) {
    return extractExpirationTime(token).before(new Date());
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(generateSignedKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String userName = extractUserName(token);
    return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
}
