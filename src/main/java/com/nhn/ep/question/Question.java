package com.nhn.ep.question;

import com.nhn.ep.answer.Answer;
import com.nhn.ep.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    // 1:N 관계.  질문글1개: 답글 여러개
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    private LocalDateTime createDate;

    //작성자를 추가로 남기기 위해 추가한 필드
    @ManyToOne  // N:1 관계.   답변 N개: 유저 1명
    private User author;

    private LocalDateTime modifyDate;

}