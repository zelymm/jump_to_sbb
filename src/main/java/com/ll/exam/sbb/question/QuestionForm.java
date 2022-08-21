package com.ll.exam.sbb.question;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionForm {
    @NotEmpty(message = "제목은 필수항목입니다.")
    @Size(max = 100, message = "제목은 100자 이내로 입력하십시오.")
    private String subject;

    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;
}
