package com.guptaji.refreshTokenDemo.model.refreshToken;

public class RefreshTokenAuthRequest {

  private String refreshToken;
  private String userName;

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  @Override
  public String toString() {
    return "RefrehTokenAuthRequest{"
        + "refreshToken='"
        + refreshToken
        + '\''
        + ", userName='"
        + userName
        + '\''
        + '}';
  }
}
