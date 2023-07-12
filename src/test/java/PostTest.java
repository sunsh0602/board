import com.nhn.ep.Main;
import com.nhn.ep.question.QuestionService;
import com.nhn.ep.user.User;
import com.nhn.ep.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest(classes = Main.class)
class PostTest {

    @Autowired
    private QuestionService questionService;

    @Autowired
    UserRepository userRepository;

    @Test
    void testJpa() {
        for (int i = 1; i <= 300; i++) {
            String subject = "테스트 데이터입니다" + i;
            String content = "내용무";
            this.questionService.create(subject, content);
        }
    }


    @Test
    void createUser(){
        User user = new User();
        user.setUsername("tester");
        user.setEmail("test@naver.com");
        user.setPassword("1234");
        userRepository.save(user);
    }

    @Test
    void testJpaWithUser() {

        Optional<User> findUser = userRepository.findByUsername("tester");

        if(findUser.isPresent()) {
            for (int i = 1; i <= 300; i++) {
                String subject = "테스트 데이터입니다" + i;
                String content = "내용무";

                this.questionService.create(subject, content, findUser.get());
            }
        }
    }
}