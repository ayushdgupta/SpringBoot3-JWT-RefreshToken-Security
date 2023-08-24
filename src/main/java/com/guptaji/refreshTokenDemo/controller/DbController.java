package com.guptaji.refreshTokenDemo.controller;

import com.guptaji.refreshTokenDemo.entity.UserSecurityInfo;
import com.guptaji.refreshTokenDemo.helper.JwtHelper;
import com.guptaji.refreshTokenDemo.model.jwt.JwtAuthRequest;
import com.guptaji.refreshTokenDemo.model.jwt.JwtAuthResponse;
import com.guptaji.refreshTokenDemo.model.refreshToken.RefreshTokenAuthRequest;
import com.guptaji.refreshTokenDemo.service.RefreshTokenService;
import com.guptaji.refreshTokenDemo.service.UserSecurityService;

import java.security.Principal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/DbUserHandling")
public class DbController {

  Logger LOG = LogManager.getLogger(DbController.class);

  @Autowired public AuthenticationManager authenticationManager;

  @Autowired public UserSecurityService userSecurityService;

  @Autowired public JwtHelper jwtHelper;

  @Autowired public RefreshTokenService refreshTokenService;

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

  @GetMapping("/login")
  public ResponseEntity<?> login(@RequestBody JwtAuthRequest jwtAuthRequest) {
    LOG.info("First authenticate the user {}", jwtAuthRequest.getUserName());
    Authentication authenticate =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                jwtAuthRequest.getUserName(), jwtAuthRequest.getPassword()));
    if (authenticate.isAuthenticated()) {
      LOG.info("Try to generate JWT for the user {}", jwtAuthRequest.getUserName());
      // Generate JWT
      String jwtToken = jwtHelper.generateToken(jwtAuthRequest.getUserName());

      // Generate Refresh Token
      String refreshToken = refreshTokenService.createRefreshToken(jwtAuthRequest.getUserName());

      JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
      jwtAuthResponse.setJwtToken(jwtToken);
      jwtAuthResponse.setUserName(jwtAuthRequest.getUserName());
      jwtAuthResponse.setRefreshToken(refreshToken);

      return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    } else {
      LOG.error("Galat banda hai re tu Bhaag yha se -- Bhaag bhaag yha se, Jldi yha se hato");
      throw new BadCredentialsException("Either userName or password is not matching");
    }
  }

  // API to refresh the JWT Token using refresh token
  @PostMapping("/refreshJwt")
  public ResponseEntity<?> refreshJwt(
      @RequestBody RefreshTokenAuthRequest refreshTokenAuthRequest) {
    boolean refreshTokenExist = refreshTokenService.isRefreshTokenExist(refreshTokenAuthRequest);
    if (refreshTokenExist) {
      LOG.info("Refreshing JWT");
      String jwtToken = jwtHelper.generateToken(refreshTokenAuthRequest.getUserName());
      JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
      jwtAuthResponse.setJwtToken(jwtToken);
      jwtAuthResponse.setUserName(refreshTokenAuthRequest.getUserName());
      jwtAuthResponse.setRefreshToken(refreshTokenAuthRequest.getRefreshToken());
      return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }
    return new ResponseEntity<>(
        "Either refreshToken is wrong or expired please check", HttpStatus.NOT_FOUND);
  }
}
