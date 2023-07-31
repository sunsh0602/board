package com.nhn.ep.question;

import com.nhn.ep.answer.AnswerForm;
import com.nhn.ep.user.User;
import com.nhn.ep.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Slf4j
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
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page,
                       @RequestParam(value = "keyword", defaultValue = "") String keyword) {

        log.info("/question/list 페이지 접속하셨습니다.");
        log.debug("/question/list 페이지 debug 로그 접속하셨습니다.");

        Page<Question> paging = this.questionService.getPageList(page, keyword); //pageList 서비스 호출
        model.addAttribute("paging", paging); //paging으로 모델 설정
        model.addAttribute("keyword", keyword);
        return "question_list"; // 질문 리스트 페이지 출력
    }

    //수정 요청을 하다가 에러난 경우 이전 내용을 다시 보여주기 위한 GET 처리 메서드
    // 만약 현재 로그인한 사용자와 질문의 작성자가 동일하지 않을 경우에는 "수정권한이 없습니다." 오류가 발생하도록 했다.
    //  그리고 수정할 질문의 제목과 내용을 화면에 보여주기 위해 questionForm 객체에 값을 담아서 템플릿으로 전달했다.
    //  (이 과정이 없다면 화면에 "제목", "내용"의 값이 채워지지 않아 비워져 보인다.)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/question/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) { //글을 작성한 유저이름이랑 스프링시큐리티 principal username이랑 다를 경우
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다."); //에러 처리
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    //수정 요청을 받아줄 POST 처리 메서드
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/question/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

    //삭제 메소드
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/question/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
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