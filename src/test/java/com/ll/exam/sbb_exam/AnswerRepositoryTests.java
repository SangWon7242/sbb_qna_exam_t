package com.ll.exam.sbb_exam;

import com.ll.exam.sbb_exam.answer.Answer;
import com.ll.exam.sbb_exam.answer.AnswerRepository;
import com.ll.exam.sbb_exam.question.Question;
import com.ll.exam.sbb_exam.question.QuestionRepository;
import com.ll.exam.sbb_exam.user.SiteUser;
import com.ll.exam.sbb_exam.user.UserRepository;
import com.ll.exam.sbb_exam.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AnswerRepositoryTests {
  @Autowired
  private UserService userService;
  @Autowired
  private QuestionRepository questionRepository;
  @Autowired
  private AnswerRepository answerRepository;
  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void beforeEach() {
    clearData();
    createSampleData();
  }

  public static void clearData(UserRepository userRepository,
                               AnswerRepository answerRepository,
                               QuestionRepository questionRepository) {
    UserServiceTests.clearData(userRepository, answerRepository, questionRepository);
  }

  private void clearData() {
    clearData(userRepository, answerRepository, questionRepository);
  }

  private void createSampleData() {
    QuestionRepositoryTests.createSampleData(userService, questionRepository);

    // 관련 답변이 하나도 없는 상태에서 쿼리 발생
    Question q = questionRepository.findById(1L).get();

    Answer a1 = new Answer();
    a1.setContent("sbb는 질문답변 게시판입니다.");
    a1.setAuthor(new SiteUser(1L));
    a1.setCreateDate(LocalDateTime.now());
    q.addAnswer(a1);

    Answer a2 = new Answer();
    a2.setContent("sbb에서는 주로 스프링관련 내용을 다룹니다.");
    a2.setAuthor(new SiteUser(2L));
    a2.setCreateDate(LocalDateTime.now());
    q.addAnswer(a2);

    questionRepository.save(q);
  }

  @Test
  @Transactional
  @Rollback(false)
  void 저장() {
    Question q = questionRepository.findById(2L).get();

    Answer a1 = new Answer();
    a1.setContent("네 자동으로 생성됩니다.");
    a1.setAuthor(new SiteUser(2L));
    a1.setCreateDate(LocalDateTime.now());
    q.addAnswer(a1);

    Answer a2 = new Answer();
    a2.setContent("네네~ 맞아요!");
    a2.setAuthor(new SiteUser(2L));
    a2.setCreateDate(LocalDateTime.now());
    q.addAnswer(a2);

    questionRepository.save(q);
  }

  @Test
  @Transactional
  @Rollback(false)
  void 조회() {
    Answer a = answerRepository.findById(1L).get();
    assertThat(a.getContent()).isEqualTo("sbb는 질문답변 게시판입니다.");
  }

  @Test
  @Transactional
  @Rollback(false)
  void 관련된_question_조회() {
    Answer a = answerRepository.findById(1L).get();
    Question q = a.getQuestion();

    assertThat(q.getId()).isEqualTo(1);
  }

  @Test
  @Transactional
  @Rollback(false)
  void question으로부터_관련된_질문들_조회() {
    Question q = questionRepository.findById(1L).get();

    List<Answer> answerList = q.getAnswerList();

    assertThat(answerList.size()).isEqualTo(2);
    assertThat(answerList.get(0).getContent()).isEqualTo("sbb는 질문답변 게시판입니다.");
  }
}
