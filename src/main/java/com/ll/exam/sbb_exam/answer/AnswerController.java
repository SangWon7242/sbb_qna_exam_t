package com.ll.exam.sbb_exam.answer;

import com.ll.exam.sbb_exam.DataNotFoundException;
import com.ll.exam.sbb_exam.question.Question;
import com.ll.exam.sbb_exam.question.QuestionService;
import com.ll.exam.sbb_exam.user.SiteUser;
import com.ll.exam.sbb_exam.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/answer")
@Controller
@RequiredArgsConstructor
public class AnswerController {
  private final QuestionService questionService;
  private final AnswerService answerService;
  private final UserService userService;

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/create/{id}")
  public String createAnswer(Principal principal, Model model,@PathVariable long id,
                             @Valid AnswerForm answerForm, BindingResult bindingResult) {
    Question question = questionService.getQuestion(id);

    if(bindingResult.hasErrors()) {
      model.addAttribute("question", question);
      return "question_detail";
    }

    SiteUser siteUser = userService.getUser(principal.getName());

    // 답변 등록 시작
    Answer answer = answerService.create(question, answerForm.getContent(), siteUser);
    // 답변 등록 끝

    return "redirect:/question/detail/%d#answer_%d".formatted(id, answer.getId());
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/modify/{id}")
  public String answerModify(AnswerForm answerForm, @PathVariable("id") Long id, Principal principal) {
    Answer answer = answerService.getAnswer(id);

    if(answer == null) {
      throw new DataNotFoundException("데이터가 없습니다.");
    }

    if (!answer.getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
    }

    answerForm.setContent(answer.getContent());
    return "answer_form";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/modify/{id}")
  public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
                             @PathVariable("id") Long id, Principal principal) {

    if (bindingResult.hasErrors()) {
      return "answer_form";
    }

    Answer answer = answerService.getAnswer(id);

    if (!answer.getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
    }

    answerService.modify(answer, answerForm.getContent());
    return "redirect:/question/detail/%d#answer_%d".formatted(answer.getQuestion().getId(), answer.getId());
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/delete/{id}")
  public String answerDelete(Principal principal, @PathVariable("id") Long id) {
    Answer answer = answerService.getAnswer(id);

    if (!answer.getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
    }

    answerService.delete(answer);

    return "redirect:/question/detail/%d".formatted(answer.getQuestion().getId());
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/vote/{id}")
  public String answerVote(Principal principal, @PathVariable("id") Long id) {
    Answer answer = answerService.getAnswer(id);
    SiteUser siteUser = userService.getUser(principal.getName());

    answerService.vote(answer, siteUser);
    return "redirect:/question/detail/%d#answer_%d".formatted(answer.getQuestion().getId(), answer.getId());
  }
}
