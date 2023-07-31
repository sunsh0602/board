package com.nhn.ep.answer;

import com.nhn.ep.question.Question;
import com.nhn.ep.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity //테이블 객체 선언
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @ManyToOne  // N:1 관계.   답변 N개: 질문글 1개
    private Question question;

    //작성자를 추가로 남기기 위해 추가한 필드
    @ManyToOne  // N:1 관계.   답변 N개: 유저 1명
    private User author;

    private LocalDateTime modifyDate;

}