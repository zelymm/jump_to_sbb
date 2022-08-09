package com.ll.exam.sbb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class MainController {
    private int num = -1;

    @RequestMapping("/sbb")
    //함수의 리턴값을 브라우저에 dp
    @ResponseBody
    public String index() {
        // console
        System.out.println("Hello");

        return "할로";
    }

    @GetMapping("/page1")
    @ResponseBody
    public String showPage1() {
        return """
                <form method="POST" action="/page2">
                    <input type="number" name="age" placeholder="나이" />
                    <input type="submit" value="page2로 POST 방식으로 이동" />
                </form>
                """;
    }

    @PostMapping("/page2")
    @ResponseBody
    public String showPage2Post(@RequestParam(defaultValue = "0") int age) {
        return """
                <h1>입력된 나이 : %d</h1>
                <h1>안녕하세요, POST 방식으로 오셨군요.</h1>
                """.formatted(age);
    }

    @GetMapping("/page2")
    @ResponseBody
    public String showPage2Get(@RequestParam(defaultValue = "0") int age) {
        return """
                <h1>입력된 나이 : %d</h1>
                <h1>안녕하세요, POST 방식으로 오셨군요.</h1>
                """.formatted(age);
    }

    @GetMapping("/plus")
    @ResponseBody
    public int showPlusGet(int a, int b) {
        return a + b;
    }

    @GetMapping("/minus")
    @ResponseBody
    public int showMinusGet(int a, int b) {
        return a - b;
    }

    @GetMapping("/increase")
    @ResponseBody
    public int showIncreaseGet() {
        num ++;
        return num;
    }

    @GetMapping("/gugudan")
    @ResponseBody
    /*int a; null 불가능
    Integer a; null 허용*/
    public String showGuGuDan(Integer dan, Integer limit) {

        if (dan == null) {
            dan = 9;
        }
        if (limit == null) {
            limit = 9;
        }
        Integer finalDan = dan;
        return IntStream.rangeClosed(1, limit)
                .mapToObj(i -> "%d * %d = %d".formatted(finalDan, i, finalDan * i))
                .collect(Collectors.joining("<br>\n"));
    }

    @GetMapping("/mbti/{name}")
    @ResponseBody
    public String showMbti(@PathVariable String name) {

        return switch (name) {
            case "홍길동", "홍길삼" -> "INFP";
            case "홍길순" -> {
                char j = 'J';
                yield "INF" + j;
            }
            case "임꺽정" -> "ENFP";
            default -> "알 수 없음";
        };
    }

    @GetMapping("/saveSession/{name}/{value}")
    @ResponseBody
    public String saveSession(@PathVariable String name, @PathVariable String value, HttpServletRequest req) {

        HttpSession session = req.getSession();
        session.setAttribute(name, value);
        return "세션변수 %s의 값이 %s(으)로 설정되었습니다.".formatted(name, value);
    }
    @GetMapping("/getSession/{name}")
    @ResponseBody
    public String getSession(@PathVariable String name, HttpSession session) {

        String value = (String) session.getAttribute(name);
        return "세션변수 %s의 값이 %s 입니다.".formatted(name, value);
    }

    private List<Article> articles = new ArrayList<>(
            Arrays.asList(
                    new Article("제목", "내용"),
                    new Article("제목", "내용"))
    );

    @GetMapping("/addArticle")
    @ResponseBody
    public String addArticle(String title, String body) {
        Article article = new Article(title, body);
        articles.add(article);

        return "%d번 포스트가 등록되었습니다.".formatted(article.getId());
    }

    @GetMapping("/getArticle/{id}")
    @ResponseBody
    public Article getArticle(@PathVariable int id) {
        Article article = articles
                .stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);

        return article;
    }

    @GetMapping("/modifyArticle/{id}")
    @ResponseBody
    public String modifyArticle(@PathVariable int id, String title, String body) {
        Article article = articles
                .stream()
                .filter(a -> a.getId() == id) // 1번
                .findFirst()
                .orElse(null);

        if (article == null) {
            return "%d번 게시물은 존재하지 않습니다.".formatted(id);
        }

        article.setTitle(title);
        article.setBody(body);

        return "%d번 게시물을 수정하였습니다.".formatted(article.getId());
    }

    @GetMapping("/deleteArticle/{id}")
    @ResponseBody
    public String deleteArticle(@PathVariable int id) {
        Article article = articles
                .stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);

        if (article == null) {
            return "%d번 게시물은 존재하지 않습니다.".formatted(id);
        }

        articles.remove(article);
        return "%d번 게시물을 삭제하였습니다.".formatted(article.getId());
    }
}

@AllArgsConstructor
@Getter
@Setter
class Article {
    private static int lastId = 0;
    private int id;
    private String title;
    private String body;


    public Article(String title, String body) {
        this(++lastId, title, body);
    }
}