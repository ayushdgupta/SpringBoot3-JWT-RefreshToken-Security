package com.guptaji.refreshTokenDemo.model.jwt;

public class JwtAuthResponse {

  private String userName;
  private String jwtToken;
  private String refreshToken;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getJwtToken() {
    return jwtToken;
  }

  public void setJwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  @Override
  public String toString() {
    return "JwtAuthResponse{"
        + "userName='"
        + userName
        + '\''
        + ", jwtToken='"
        + jwtToken
        + '\''
        + ", refreshToken='"
        + refreshToken
        + '\''
        + '}';
  }
}
