package com.ll.exam.sbb_exam;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
  @RequestMapping("/sbb")
  // 아래 함수의 리턴값을 그대로 브라우저에 표시
  // 아래 함수의 리턴값을 문자열화 해서 브라우저 응답을 바디에 담는다.
  @ResponseBody
  public String index() {
    // 서버에서 실행
    System.out.println("Hello");

    // 먼 미래에서 브라우저에서 실행
    return "안녕하세요.asdasd";
  }
}
