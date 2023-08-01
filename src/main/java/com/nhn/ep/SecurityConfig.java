package com.nhn.ep;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests().requestMatchers(
                        // 전체 페이지 접근 허용
                        new AntPathRequestMatcher("/**")).permitAll()
                .and()
                .csrf().ignoringRequestMatchers(new AntPathRequestMatcher("/project/**"))
                .and()
                // h2 db 페이지. csrf 검증 무시
                .csrf().ignoringRequestMatchers(new AntPathRequestMatcher("/h2/**"))
                .and()
                // h2 db 페이지. h2 console의 frame 에러 해결
                .headers()
                .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                .and()
                // 시큐리티 로그인 처리
                .formLogin() //폼 로그인 방식으로 인증하겠다
                .loginPage("/user/login")
                .defaultSuccessUrl("/") //로그인 성공 시 페이지 이동
                .and()
                // 시큐리티 로그아웃 처리
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/") //로그아웃 성공 시 페이지 이동
                .invalidateHttpSession(true) // 사용자 세션 삭제
        ;
        return http.build();
    }

    // 패스워드 인코더 빈 등록
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); // BCryptPasswordEncoder는 BCrypt 해싱 함수(BCrypt hashing function)를 사용해서 비밀번호를 암호화


    }

    // 시큐리티의 인증을 담당하는 Authentication Manager
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}