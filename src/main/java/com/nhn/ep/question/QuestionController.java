package com.nhn.ep.question;

import com.nhn.ep.answer.AnswerForm;
import com.nhn.ep.user.User;
import com.nhn.ep.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor //롬복 DI 주입. questionRepository를 주입한다.
@Controller
public class QuestionController {

//    private final QuestionRepository questionRepository; // DB repository

    private final QuestionService questionService;
    //유저 서비스 추가
    private final UserService userService;

//    @GetMapping("/question/list")
//    public String list(Model model) {
////        List<Question> questionList = this.questionRepository.findAll(); // DB 테이블 전체 내용 가져오기.
//
//        List<Question> questionList = questionService.getList();
//        model.addAttribute("questionList", questionList);
//        return "question_list";
//    }

    //페이징 처리
    @GetMapping("/question/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        Page<Question> paging = this.questionService.getPageList(page); //pageList 서비스 호출
        model.addAttribute("paging", paging); //paging으로 모델 설정
        return "question_list"; // 질문 리스트 페이지 출력
    }


    @GetMapping(value = "/question/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id); //questionService 서비스의 getQuestion 호출
        model.addAttribute("question", question);
        return "question_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/question/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form"; // 질문 등록 폼으로 이동
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/question/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {  //QuestionForm 기준에 에러 있을 경우
            return "question_form"; // 질문 등록 폼으로 이동
        }

        //principal을 통해 현재 로그인 된 사용자의 이름을 UserService를 통해 조회한다.
        User author = userService.getUser(principal.getName());

        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), author);
        return "redirect:/question/list";
    }
}