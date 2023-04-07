package com.ll.exam.sbb_exam.answer;

import com.ll.exam.sbb_exam.question.Question;
import com.ll.exam.sbb_exam.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/answer")
@Controller
@RequiredArgsConstructor
public class AnswerController {
  private final QuestionService questionService;
  private final AnswerService answerService;

  @PostMapping("/create/{id}")
  public String createAnswer(Model model, @PathVariable int id, String content) {
    Question question = questionService.getQuestion(id);

    // 답변 등록 시작
    this.answerService.create(question, content);
    // 답변 등록 끝

    return "redirect:/question/detail/%d".formatted(id);
  }
}
