package com.nhn.ep.answer;

import com.nhn.ep.question.Question;
import com.nhn.ep.question.QuestionService;
import com.nhn.ep.user.User;
import com.nhn.ep.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;

@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/answer/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id,
                               @Valid AnswerForm answerForm,
                               BindingResult bindingResult,
                               Principal principal) {

        Question question = this.questionService.getQuestion(id);
        // answerService를 이용해서 답변을 저장한다.

        User author = this.userService.getUser(principal.getName());

        if(bindingResult.hasErrors()){  //answerForm에 에러가 포함된 경우
            model.addAttribute("question", question);
            return "question_detail"; //다시 질문글 조회화면으로 간다
        }

        this.answerService.create(question, answerForm.getContent(), author);  // DB 저장

        return String.format("redirect:/question/detail/%s", id); // 답변을 저장하고, 클릭했던 글주소로 리다이렉트 이동한다.
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/answer/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.answerService.delete(answer);
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }

    //수정 요청을 하다가 에러난 경우 이전 내용을 다시 보여주기 위한 GET 처리 메서드
    // 만약 현재 로그인한 사용자와 질문의 작성자가 동일하지 않을 경우에는 "수정권한이 없습니다." 오류가 발생하도록 했다.
    // 그리고 수정할 답변의 내용을 화면에 보여주기 위해 questionForm 객체에 값을 담아서 템플릿으로 전달했다.
    // (이 과정이 없다면 화면에 "내용"의 값이 채워지지 않아 비워져 보인다.)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/answer/modify/{id}")
    public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        answerForm.setContent(answer.getContent());
        return "answer_form";
    }

    //답변 수정 POST 처리 메소드
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/answer/modify/{id}")
    public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
                               @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "answer_form";
        }
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.answerService.modify(answer, answerForm.getContent());
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }
}