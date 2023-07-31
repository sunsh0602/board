package com.nhn.ep.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    //페이징
    Page<Question> findAll(Pageable pageable);


    //JPQL(JPQL: Java Persistence Query Language) 방식: 직접 쿼리 작성
    @Query("SELECT q FROM Question q WHERE q.subject LIKE %:keyword% OR q.content LIKE %:keyword%")
    Page<Question> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
