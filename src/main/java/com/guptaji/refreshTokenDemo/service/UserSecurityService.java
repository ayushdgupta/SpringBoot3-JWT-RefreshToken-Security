package com.guptaji.refreshTokenDemo.service;

import com.guptaji.refreshTokenDemo.entity.UserSecurityInfo;
import com.guptaji.refreshTokenDemo.repository.UserInfoRepo;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityService {

  Logger LOG = LogManager.getLogger(UserSecurityService.class);

  @Autowired public UserInfoRepo userInfoRepo;

  @Autowired public PasswordEncoder passwordEncoder;

  public void createUserInDB(UserSecurityInfo userSecurityInfo) {
    userSecurityInfo.setActualPassword(userSecurityInfo.getPassword());
    userSecurityInfo.setPassword(passwordEncoder.encode(userSecurityInfo.getPassword()));
    LOG.info("password encoded {}", userSecurityInfo.getPassword());
    userInfoRepo.save(userSecurityInfo);
  }

  public List<UserSecurityInfo> fetchAllUsers() {
    LOG.info("Fetching all users from DB");
    return userInfoRepo.findAll();
  }

  public UserSecurityInfo getUserByUserName(String userName) {
    LOG.info("Fetching corresponding user info from DB");
    return userInfoRepo
        .findByUserName(userName)
        .orElseThrow(() -> new UsernameNotFoundException("No user Found with this name"));
  }
}
