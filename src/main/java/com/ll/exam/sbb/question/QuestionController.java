package com.ll.exam.sbb.question;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class QuestionController {
    @RequestMapping("/question/list")
    //@ResponseBody가 없으면 question_list를 view로
    public String list() {
        return "question_list";
    }
}
