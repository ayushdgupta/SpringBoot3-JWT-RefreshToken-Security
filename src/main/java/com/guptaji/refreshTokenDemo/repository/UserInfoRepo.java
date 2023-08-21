package com.guptaji.refreshTokenDemo.repository;

import com.guptaji.refreshTokenDemo.entity.UserSecurityInfo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepo extends JpaRepository<UserSecurityInfo, String> {
  Optional<UserSecurityInfo> findByUserName(String username);
}
