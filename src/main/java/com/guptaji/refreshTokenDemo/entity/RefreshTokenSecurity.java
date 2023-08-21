package com.guptaji.refreshTokenDemo.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class RefreshTokenSecurity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String refreshToken;

  private Instant expiryTime;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "userName")
  private UserSecurityInfo userSecurityInfo;

  public RefreshTokenSecurity() {
    // default constructor
  }

  public RefreshTokenSecurity(
      long id, String refreshToken, Instant expiryTime, UserSecurityInfo userSecurityInfo) {
    this.id = id;
    this.refreshToken = refreshToken;
    this.expiryTime = expiryTime;
    this.userSecurityInfo = userSecurityInfo;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public Instant getExpiryTime() {
    return expiryTime;
  }

  public void setExpiryTime(Instant expiryTime) {
    this.expiryTime = expiryTime;
  }

  public UserSecurityInfo getUserSecurityInfo() {
    return userSecurityInfo;
  }

  public void setUserSecurityInfo(UserSecurityInfo userSecurityInfo) {
    this.userSecurityInfo = userSecurityInfo;
  }

  @Override
  public String toString() {
    return "RefreshTokenSecurity{"
        + "id="
        + id
        + ", refreshToken='"
        + refreshToken
        + '\''
        + ", expiryTime="
        + expiryTime
        + ", userSecurityInfo="
        + userSecurityInfo
        + '}';
  }
}
