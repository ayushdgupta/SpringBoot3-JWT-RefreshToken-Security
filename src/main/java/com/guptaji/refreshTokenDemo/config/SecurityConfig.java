package com.guptaji.refreshTokenDemo.config;

import static com.guptaji.refreshTokenDemo.constants.SecurityConstants.*;

import com.guptaji.refreshTokenDemo.Security.JwtAuthenticationEntryPoint;
import com.guptaji.refreshTokenDemo.filter.JwtAuthFilter;
import com.guptaji.refreshTokenDemo.model.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity    // this annotation is optional
public class SecurityConfig {

  @Autowired private JwtAuthenticationEntryPoint authenticationEntryPoint;

  @Autowired private JwtAuthFilter jwtAuthFilter;

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
                    //                    .permitAll()    // to create first user only in DB
                    .requestMatchers("/DbUserHandling/getAllUser")
                    .hasAnyAuthority(ROLE_HOKAGE, ROLE_JONIN)
                    .requestMatchers(
                        "/DbUserHandling/getCurrentUser",
                        "/DbUserHandling/login",
                        "/DbUserHandling/refreshJwt")
                    .permitAll()
                    .requestMatchers("/DbUserHandling/getUserById/**")
                    .hasAnyAuthority(ROLE_HOKAGE, ROLE_JONIN, ROLE_CHUNIN)
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(
            httpSecurityExceptionHandlingConfigurer ->
                httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(
                    authenticationEntryPoint))
        .sessionManagement(
            httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    // Never used httpBasic when using JWT Authentication because it'll override the JWT
    // Authentication
    //        .httpBasic(Customizer.withDefaults());
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

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }
}
