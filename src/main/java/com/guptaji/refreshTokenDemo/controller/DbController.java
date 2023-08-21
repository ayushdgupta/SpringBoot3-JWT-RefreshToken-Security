package com.guptaji.refreshTokenDemo.controller;

import com.guptaji.refreshTokenDemo.entity.UserSecurityInfo;
import com.guptaji.refreshTokenDemo.service.UserSecurityService;

import java.security.Principal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/DbUserHandling")
public class DbController {

  Logger LOG = LogManager.getLogger(DbController.class);

  @Autowired public UserSecurityService userSecurityService;

  @PostMapping("/createNewUser")
  public ResponseEntity<?> createUser(@RequestBody UserSecurityInfo userSecurityInfo) {
    LOG.info("Creating a new user in DB {}", userSecurityInfo);
    userSecurityService.createUserInDB(userSecurityInfo);
    return new ResponseEntity<>("user created", HttpStatus.CREATED);
  }

  @GetMapping("/getAllUser")
  public ResponseEntity<?> getUser() {
    List<UserSecurityInfo> userSecurityInfo = userSecurityService.fetchAllUsers();
    return new ResponseEntity<>(userSecurityInfo, HttpStatus.OK);
  }

  @GetMapping("/getCurrentUser")
  public ResponseEntity<?> getCurrentActiveUser(Principal principal) {
    LOG.info("Fetching the current user");
    return new ResponseEntity<>(principal.getName(), HttpStatus.OK);
  }

  @GetMapping("/getUserById/{userId}")
  public ResponseEntity<?> getUserById(@PathVariable("userId") String userName) {
    LOG.info("Fetching the current user by userName {}", userName);
    UserSecurityInfo userByUserName = userSecurityService.getUserByUserName(userName);
    return new ResponseEntity<>(userByUserName, HttpStatus.OK);
  }
}
