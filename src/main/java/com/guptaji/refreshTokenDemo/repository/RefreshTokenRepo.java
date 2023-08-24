package com.guptaji.refreshTokenDemo.repository;

import com.guptaji.refreshTokenDemo.entity.RefreshTokenSecurity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshTokenSecurity, Integer> {
  RefreshTokenSecurity findByuserSecurityInfoUserName(String userName);

  Optional<RefreshTokenSecurity> findByRefreshTokenAndUserSecurityInfoUserName(
      String refreshToken, String userName);
  //  RefreshTokenSecurity findByUserSecurityInfo(String userName);   NOT_WORKING

  //  RefreshTokenSecurity findByUserId(String userName);    NOT_WORKING

  //  RefreshTokenSecurity findByUserName(String userName);   NOT_WORKING
}
