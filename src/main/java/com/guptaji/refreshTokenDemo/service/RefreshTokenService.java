package com.guptaji.refreshTokenDemo.service;

import com.guptaji.refreshTokenDemo.entity.RefreshTokenSecurity;
import com.guptaji.refreshTokenDemo.entity.UserSecurityInfo;
import com.guptaji.refreshTokenDemo.model.refreshToken.RefreshTokenAuthRequest;
import com.guptaji.refreshTokenDemo.repository.RefreshTokenRepo;
import com.guptaji.refreshTokenDemo.repository.UserInfoRepo;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {

  Logger LOG = LogManager.getLogger(RefreshTokenService.class);

  @Autowired public RefreshTokenRepo refreshTokenRepo;

  @Autowired public UserInfoRepo userInfoRepo;

  public String createRefreshToken(String userName) {

    // First I need to delete the older refresh token if there is any
    RefreshTokenSecurity data = refreshTokenRepo.findByuserSecurityInfoUserName(userName);
    if (data != null) {
      LOG.info("Deleting the older one");
      refreshTokenRepo.delete(data);
    }

    RefreshTokenSecurity refreshTokenSecurity = new RefreshTokenSecurity();
    UserSecurityInfo userSecurityInfo = userInfoRepo.findByUserName(userName).get();
    refreshTokenSecurity.setUserSecurityInfo(userSecurityInfo);
    refreshTokenSecurity.setExpiryTime(Instant.now().plus(15, ChronoUnit.MINUTES));
    refreshTokenSecurity.setRefreshToken(UUID.randomUUID().toString());

    refreshTokenRepo.save(refreshTokenSecurity);
    LOG.info("Save the refresh token into DB");
    return refreshTokenSecurity.getRefreshToken();
  }

  public boolean isRefreshTokenExist(RefreshTokenAuthRequest authRequest) {
    LOG.info("Verifying the refreshToken");
    Optional<RefreshTokenSecurity> tokenData =
        refreshTokenRepo.findByRefreshTokenAndUserSecurityInfoUserName(
            authRequest.getRefreshToken(), authRequest.getUserName());
    if (tokenData.isPresent()) {
      // Also verifying that refresh token valid or not i.e. it should not be expired
      if (isRefreshTokenValid(tokenData.get())) {
        return true;
      } else {
        LOG.info("Deleting the token from DB because it expired");
        refreshTokenRepo.delete(tokenData.get());
      }
    }
    return false;
  }

  public boolean isRefreshTokenValid(RefreshTokenSecurity refreshTokenSecurity) {
    if (refreshTokenSecurity.getExpiryTime().compareTo(Instant.now()) > 0) {
      return true;
    }
    return false;
  }
}
