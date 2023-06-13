import com.nhn.ep.Main;
import com.nhn.ep.question.Question;
import com.nhn.ep.question.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest(classes = Main.class)
public class MainTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void DB_질문테이블에_입력() {
        Question q1 = new Question();
        q1.setSubject("클라우드 뜻이 무엇인가요?");
        q1.setContent("클라우드에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q1);  // 첫번째 질문 저장

        Question q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        q2.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q2);  // 두번째 질문 저장
    }


    @Test
    void 질문테이블을_조회() {
        //전체를 조회하자 findAll
        List<Question> all = this.questionRepository.findAll();
        System.out.println("질문 테이블 총 갯수: " + all.size());

        if(!all.isEmpty()) {
            //조회한 것에서 0번째 질문을 가져오자.
            Question q1 = all.get(0);
            //0번째 질문 제목이 "클라우드 뜻이 무엇인가요?"와 같다면 성공
            assertEquals("클라우드 뜻이 무엇인가요?", q1.getSubject());
            System.out.println("결과 출력: " + q1.getSubject());

            Question q2 = all.get(1);
            //1번째 질문 제목이 "스프링부트 모델 질문입니다."와 같다면 성공
            assertEquals("스프링부트 모델 질문입니다.", q2.getSubject());
            System.out.println("결과 출력: " + q2.getSubject());
        }
    }

    @Test
    void 질문테이블_수정() {
        //전체를 조회하자 findAll
        List<Question> all = this.questionRepository.findAll();
        System.out.println("질문 테이블 총 갯수: " + all.size());

        if(!all.isEmpty()) {
            //조회한 것에서 0번째 질문을 가져오자.
            Question q1 = all.get(0);
            q1.setSubject("제목을 수정했습니다.");
            questionRepository.save(q1);
        }
    }

    @Test
    void 질문테이블을_수정_확인() {
        //전체를 조회하자 findAll
        List<Question> all = this.questionRepository.findAll();
        System.out.println("질문 테이블 총 갯수: " + all.size());

        if(!all.isEmpty()) {
            //조회한 것에서 0번째 질문을 가져오자.
            Question q1 = all.get(0);
            System.out.println("제목 출력: " + q1.getSubject());
        }
    }
    @Test
    void 질문테이블_글삭제() {
        this.questionRepository.deleteAll();

        //전체를 조회하자 findAll
        List<Question> all2 = this.questionRepository.findAll();
        System.out.println("질문 테이블 총 갯수: " + all2.size());
    }
}