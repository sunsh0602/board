package com.nhn.ep.answer;

import com.nhn.ep.question.Question;
import com.nhn.ep.question.QuestionService;
import com.nhn.ep.user.User;
import com.nhn.ep.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
}