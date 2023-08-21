package com.guptaji.refreshTokenDemo.config;

import static com.guptaji.refreshTokenDemo.constants.SecurityConstants.*;

import com.guptaji.refreshTokenDemo.Security.JwtAuthenticationEntryPoint;
import com.guptaji.refreshTokenDemo.model.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired private JwtAuthenticationEntryPoint authenticationEntryPoint;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        //        .cors(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            (authorizeHttpRequests) ->
                authorizeHttpRequests
                    .requestMatchers("/DbUserHandling/createNewUser")
                    .hasAuthority(ROLE_HOKAGE)
                    //                    .permitAll()    // to create first user only
                    .requestMatchers("/DbUserHandling/getAllUser")
                    .hasAnyAuthority(ROLE_HOKAGE, ROLE_JONIN)
                    .requestMatchers("/DbUserHandling/getCurrentUser")
                    .permitAll()
                    .requestMatchers("/DbUserHandling/getUserById/**")
                    .hasAnyAuthority(ROLE_HOKAGE, ROLE_JONIN, ROLE_CHUNIN)
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(
            httpSecurityExceptionHandlingConfigurer ->
                httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(
                    authenticationEntryPoint))
        .httpBasic(Customizer.withDefaults());
    //        .formLogin(Customizer.withDefaults());

    return httpSecurity.build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new UserDetailsServiceImpl();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(userDetailsService());
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    return daoAuthenticationProvider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
