package com.nhn.ep.answer;

import com.nhn.ep.exception.DataNotFoundException;
import com.nhn.ep.question.Question;
import com.nhn.ep.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;


    public void create(Question question, String content, User author){

        Answer answer = new Answer();
        answer.setContent(content); //내용 셋팅
        answer.setCreateDate(LocalDateTime.now());

        // 답변을 작성하면   어떤 글의 답변
        // 글 1개당 답변 N개  1:N
        answer.setQuestion(question); //답변과 관계있는 question임을 셋팅

        answer.setAuthor(author);

        this.answerRepository.save(answer); //DB에 저장
    }

    // DB에서 답변이 있는지 조회
    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    // 답변 수정 save()로 DB 저장
    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }

    // 답변 삭제 delete()로 DB 삭제
    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }

}
