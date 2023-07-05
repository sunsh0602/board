package com.nhn.ep.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {

    // user 레파지토리 주입
    private final UserRepository userRepository;

    /**
     * 로그인 핵심 처리 (Override하여 세부 내용 구현)
     *
     * 입력받은 username으로 user 테이블을 조회해서 없으면 에러
     *
     * loadUserByUsername 이라는 스프링 메서드는
     * 사용자가 입력한 비밀번호와 DB의 패스워드와 일치하는지 확인하는 로직이 기본으로 포함되어 있다.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //repository 안에 메소드 추가
        //Optional<User> findByUsername(String username);

        //DB에서 로그인 요청받은 username이 있는지 확인
        Optional<User> findUser = this.userRepository.findByUsername(username);
        if (findUser.isEmpty()) {
            //username이 DB에 없는 경우 에러 발생
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }

        //username이 DB에 있을 경우
        User user = findUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equalsIgnoreCase(username)) {
            //유저 이름이 admin 이면 관리자 권한 처리
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
            //그 외에는 일반 사용자 권한 처리
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }

        //스프링 시큐리티의 User 객체를 생성하여 리턴
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}