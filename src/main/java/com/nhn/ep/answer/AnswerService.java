package com.nhn.ep.answer;

import com.nhn.ep.question.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;


    public void create(Question question, String content){

        Answer answer = new Answer();
        answer.setContent(content); //내용 셋팅
        answer.setCreateDate(LocalDateTime.now());

        // 답변을 작성하면   어떤 글의 답변

        // 글 1개당 답변 N개  1:N
        answer.setQuestion(question); //답변과 관계있는 question임을 셋팅

        this.answerRepository.save(answer); //DB에 저장
    }

}
