package com.ll.exam.sbb_exam;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// 시큐리티 설정
@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests().requestMatchers(
        new AntPathRequestMatcher("/**")).permitAll()
        .and() // 문맥의 끝
        .csrf().ignoringRequestMatchers(
            new AntPathRequestMatcher("/h2-console/**"))
        .and()
        .headers()
        .addHeaderWriter(new XFrameOptionsHeaderWriter(
            XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
    return http.build();
  }
}