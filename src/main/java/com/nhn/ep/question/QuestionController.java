package com.nhn.ep.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor //롬복 DI 주입. questionRepository를 주입한다.
@Controller
public class QuestionController {

//    private final QuestionRepository questionRepository; // DB repository

    private final QuestionService questionService;

    @GetMapping("/question/list")
    public String list(Model model) {
//        List<Question> questionList = this.questionRepository.findAll(); // DB 테이블 전체 내용 가져오기.

        List<Question> questionList = questionService.getList();
        model.addAttribute("questionList", questionList);
        return "question_list";
    }

    @GetMapping(value = "/question/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id); //questionService 서비스의 getQuestion 호출
        model.addAttribute("question", question);
        return "question_detail";
    }

    @GetMapping("/question/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form"; // 질문 등록 폼으로 이동
    }

    @PostMapping("/question/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {  //QuestionForm 기준에 에러 있을 경우
//            return "question_form"; // 질문 등록 폼으로 이동
//        }
        this.questionService.create(questionForm.getSubject(), questionForm.getContent());
        return "redirect:/question/list";
    }
}