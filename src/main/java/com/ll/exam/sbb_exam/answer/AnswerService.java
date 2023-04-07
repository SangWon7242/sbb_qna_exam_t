package com.ll.exam.sbb_exam.answer;

import com.ll.exam.sbb_exam.question.Question;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Store;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnswerService {
  private final AnswerRepository answerRepository;

  public void create(Question question, String content) {
    Answer answer = new Answer();
    answer.setContent(content);
    answer.setCreateDate(LocalDateTime.now());
    answer.setQuestion(question);
    answerRepository.save(answer);
  }
}
