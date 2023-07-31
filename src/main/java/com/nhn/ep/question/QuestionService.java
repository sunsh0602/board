package com.nhn.ep.question;

import com.nhn.ep.exception.DataNotFoundException;
import com.nhn.ep.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    //질문 게시판 DB의 모든 내용을 읽는다.
    public List<Question> getList() {
        return this.questionRepository.findAll();
    }

    //질문글 페이지를 리턴한다
    public Page<Question> getPageList(int page, String keyword){

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate")); //글 작성 날짜 역순으로 정렬. 최신 글부터 보이게

        //Pageable 객체로 검색 조건을 정의한다.
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts)); // 각 페이지당 글은 10개씩, 정렬 조건

//        Page<Question> pages = this.questionRepository.findAll(pageable); //page를 리턴하는 repository 메소드 호출
        Page<Question> pages = this.questionRepository.findAllByKeyword(keyword, pageable);
        return pages;
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }
    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    // 질문을 DB에 저장한다.
    public void create(String subject, String content, User author) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());

        //작성자를 저장하기 위해 추가
        q.setAuthor(author);
        this.questionRepository.save(q);
    }

    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    // 질문을 DB에 저장한다.
    public void create(String subject, String content) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());

        this.questionRepository.save(q);
    }
}