package com.guptaji.refreshTokenDemo.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setHeader("Error Message", "Sahi creds dal bhootni ke");
    PrintWriter printWriter = response.getWriter();
    printWriter.println("Janab Access Denied !! " + authException.getMessage());
  }
}
