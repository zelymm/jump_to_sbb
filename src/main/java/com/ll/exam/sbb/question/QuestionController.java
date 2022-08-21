package com.ll.exam.sbb.question;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/question")
@Controller
@RequiredArgsConstructor // 생성자 주입
public class QuestionController {
    private final QuestionService questionService;;

    @RequestMapping("/list")
    //@ResponseBody가 없으면 question_list를 view로
    public String list(Model model) {
        List<Question> questionList = questionService.getList();

        // 미래에 실행된 question_list.html 에서
        // questionList 라는 이름으로 questionList 변수를 사용할 수 있다.
        model.addAttribute("questionList", questionList);

        return "question_list";
    }

    @RequestMapping("/detail/{id}")
    public String detail(Model model, @PathVariable int id) {
        Question question = questionService.getQuestion(id);

        model.addAttribute("question", question);

        return "question_detail";
    }
    @GetMapping("/create")
    public String questionCreate() {
        return "question_form";
    }

    @PostMapping("/create")
    public String questionCreate(Model model, QuestionForm questionForm) {
        boolean hasError = false;

        if (questionForm.getSubject() == null || questionForm.getSubject().trim().length() == 0) {
            model.addAttribute("subjectErrorMsg", "제목은 필수항목입니다.");
            hasError = true;
        }
        if (questionForm.getContent() == null || questionForm.getContent().trim().length() == 0) {
            model.addAttribute("contentErrorMsg", "내용은 필수항목입니다.");
            hasError = true;
        }

        if (hasError) {
            model.addAttribute("questionForm", questionForm);
            return "question_form";
        }
        questionService.create(questionForm.getSubject(), questionForm.getContent());

        return "redirect:/question/list";
    }


}
