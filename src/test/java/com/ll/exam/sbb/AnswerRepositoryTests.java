package com.ll.exam.sbb;

import com.ll.exam.sbb.answer.Answer;
import com.ll.exam.sbb.answer.AnswerRepository;
import com.ll.exam.sbb.question.Question;
import com.ll.exam.sbb.question.QuestionRepository;
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
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    private int lastSampleDataId;

    @BeforeEach
    void beforeEach() {
        clearData();
        createSampleData();
    }

    private void clearData() {
        QuestionRepositoryTests.clearData(questionRepository);
        answerRepository.deleteAll();
        answerRepository.truncateTable();
    }

    private void createSampleData() {
        QuestionRepositoryTests.createSampleData(questionRepository);

        Question q = questionRepository.findById(1).get();

        Answer a1 = new Answer();
        a1.setContent("sbb는 Q&A 게시판이다.");
        a1.setCreateDate(LocalDateTime.now());
        q.addAnswer(a1);

        Answer a2 = new Answer();
        a2.setContent("sbb에서는 스프링부트 내용을 다룬다.");
        a2.setCreateDate(LocalDateTime.now());
        q.addAnswer(a2);

        questionRepository.save(q);
    }

    @Test
    @Transactional
    @Rollback(false)
    void 저장() {
        Question q = questionRepository.findById(2).get();

        Answer a1 = new Answer();
        a1.setContent("네 자동으로 생성됩니다.");
        a1.setCreateDate(LocalDateTime.now());
        q.addAnswer(a1);

        Answer a2 = new Answer();
        a2.setContent("네네~");
        a2.setCreateDate(LocalDateTime.now());
        q.addAnswer(a2);

        //q에 add한 걸 save해주기
        questionRepository.save(q);
    }

    @Test
    @Transactional
    @Rollback(false)
    void 조회() {
        Answer a = this.answerRepository.findById(1).get();
        assertThat(a.getContent()).isEqualTo("sbb는 Q&A 게시판이다.");
    }

    @Test
    @Transactional
    @Rollback(false)
    void 관련된_question_조회() {
        Answer a = this.answerRepository.findById(1).get();
        Question q = a.getQuestion();

        assertThat(q.getId()).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback(false)
    void question으로부터_관련된_질문들_조회() {
        Question q = questionRepository.findById(1).get();

        List<Answer> answerList = q.getAnswerList();
        assertThat(answerList.size()).isEqualTo(2);
        assertThat(answerList.get(0).getContent()).isEqualTo("sbb는 Q&A 게시판이다.");
    }
}