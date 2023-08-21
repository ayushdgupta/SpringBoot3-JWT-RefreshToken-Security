package com.guptaji.refreshTokenDemo.model;

import com.guptaji.refreshTokenDemo.entity.UserSecurityInfo;
import com.guptaji.refreshTokenDemo.repository.UserInfoRepo;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

  Logger LOG = LogManager.getLogger(UserDetailsServiceImpl.class);

  @Autowired private UserInfoRepo userInfoRepo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // M1->
    //        LOG.info("Validating the user {}", username);
    //        UserSecurityInfo userInfo = userInfoRepo.findByUserName(username).orElseThrow(() ->
    // new UsernameNotFoundException("User Not found in DB"));
    //        LOG.info("User Authenticated");
    //        UserDetailsImpl userDetails = new UserDetailsImpl(userInfo);
    //        return userDetails;

    // M2-> Using JAVA 8
    LOG.info("Validating the user {}", username);
    Optional<UserSecurityInfo> userByUserName = userInfoRepo.findByUserName(username);

    UserDetailsImpl userDetails =
        userByUserName
            .map(UserDetailsImpl::new)
            .orElseThrow(() -> new UsernameNotFoundException("User Not found in DB"));

    LOG.info("User Authenticated {}", userDetails);
    return userDetails;
  }
}
