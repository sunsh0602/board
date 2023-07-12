package com.nhn.ep.user;

import com.nhn.ep.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    //요청받은 유저이름으로 유저 테이블에 존재하는지 확인해서 유저 정보가 있으면 꺼내온다. 없으면 에러 발생
    public User getUser(String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new DataNotFoundException("user not found");
        }
    }
}