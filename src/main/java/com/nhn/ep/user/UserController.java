package com.nhn.ep.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class UserController {


    @GetMapping("/user/login")
    public String login() {
        return "login_form";
    }


    //UserService 생성자 주입 DI
    private final UserService userService;

    //회원가입 링크 들어왔을 때 보여주는 페이지. 회원가입 폼 보여준다.
    @GetMapping("/user/signup")
    public String signup(UserForm userForm) {
        return "signup_form";
    }

    //회원가입 폼을 전송할 때 POST 전송
    @PostMapping("/user/signup")
    public String signup(@Valid UserForm userForm, BindingResult bindingResult) {

        // 폼에 에러가 있을 경우 hasErrors 에 해당
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        // 사용자가 입력한 암호 2개가 일치하는지 확인
        if (!userForm.getPassword1().equals(userForm.getPassword2())) {
            //bindingResult.rejectValue(필드명, 오류코드, 에러메시지)
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }

        try{
            //유저 생성 서비스 호출
            userService.create(userForm.getUsername(), userForm.getEmail(), userForm.getPassword1());
        } catch(DataIntegrityViolationException e) {
            //DB에 이미 있는 계정인 경우 id, email이 user 엔티티에 unique로 선언했기 때문에 중복 에러 발생
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.(아이디 또는 이메일 중복)");
            return "signup_form";
        } catch(Exception e) {
            //기타 다른 이유로 에러 발생
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }

        return "redirect:/"; // localhost:8080/ 으로 이동
    }
}