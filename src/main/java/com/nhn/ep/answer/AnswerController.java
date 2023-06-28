package com.nhn.ep.answer;

import com.nhn.ep.question.Question;
import com.nhn.ep.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    @PostMapping("/answer/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id, @RequestParam String content) {

        Question question = this.questionService.getQuestion(id);
        // answerService를 이용해서 답변을 저장한다.

        this.answerService.create(question, content);  // DB 저장

        return String.format("redirect:/question/detail/%s", id); // 답변을 저장하고, 클릭했던 글주소로 리다이렉트 이동한다.
    }
}