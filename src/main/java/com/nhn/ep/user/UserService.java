package com.nhn.ep.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    //주입 받음
    private final UserRepository userRepository;
    //주입 받음
    private final PasswordEncoder passwordEncoder;

    // 유저 생성하는 메소드
    public User create(String username, String email, String password) {

        // 빈 user 객체 생성
        User user = new User();
        user.setUsername(username); // user 로그인 Id set
        user.setEmail(email); // user email set

        // password는 암호화한다.
        user.setPassword(passwordEncoder.encode(password)); // user pwd set

        //DB user 테이블에 user 저장
        this.userRepository.save(user);

        return user;
    }
}