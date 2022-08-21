package com.ll.exam.sbb.question;

import com.ll.exam.sbb.answer.AnswerForm;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/question")
@Controller
@RequiredArgsConstructor // 생성자 주입
public class QuestionController {
    private final QuestionService questionService;;

    @GetMapping("/list")
    //@ResponseBody가 없으면 question_list를 view로
    public String list(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Question> paging = questionService.getList(page);
        model.addAttribute("paging", paging);

        return "question_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable long id, AnswerForm answerForm)  {
        Question question = questionService.getQuestion(id);

        model.addAttribute("question", question);

        return "question_detail";
    }
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    @PostMapping("/create")
    public String questionCreate(Model model, @Valid QuestionForm questionForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) { //has error?
            return "question_form"; //<- if yes,
        }
        questionService.create(questionForm.getSubject(), questionForm.getContent());

        return "redirect:/question/list";
    }
}
