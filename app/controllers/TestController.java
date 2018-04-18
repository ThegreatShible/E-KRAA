package controllers;


import Persistance.DAOs.BookRepository;
import com.fasterxml.jackson.databind.JsonNode;
import forms.AnswerForm;
import forms.BookForm;
import forms.QuestionForm;
import forms.UserAnswerForm;
import models.book.Book;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

//TODO : add orderby into database
//TODO : Remove this controller

public class TestController extends Controller {


    private final views.html.test testTemplate;
    private BookRepository bookRepository;
    private MessagesApi messagesApi;

    @Inject
    public TestController(MessagesApi messagesApi, BookRepository bookRepository, views.html.test testTemplate) {
        this.testTemplate = testTemplate;
        this.messagesApi = messagesApi;
        this.bookRepository = bookRepository;
    }

    public Result test() {
        return ok(testTemplate.render());
    }



    public Result test2() {
        return ok("done");
    }

    public CompletableFuture<Result> test3(int id) {
        CompletableFuture<Book> fb = bookRepository.find(id);
        return fb.thenApply(b -> {
            String json = Json.toJson(b).toString();
            return ok(json);
        });
    }

    public Result createBookForm() {
        BookForm bookForm = new BookForm();
        AnswerForm answerForm = new AnswerForm();
        answerForm.setRight(true);
        answerForm.setAnswer("yes");
        answerForm.setNumAnswer((short) 1);
        List<AnswerForm> answerForms = new ArrayList<>();
        answerForms.add(answerForm);
        QuestionForm questionForm = new QuestionForm();
        questionForm.setMultiple(true);
        questionForm.setQuestion("what?");
        questionForm.setAnswers(answerForms);
        questionForm.setQuestionNum((short) 1);
        questionForm.setWeight((short) 10);
        List<QuestionForm> questionForms = new ArrayList<>();
        questionForms.add(questionForm);
        bookForm.setQuestions(questionForms);
        bookForm.setTitle("title");
        bookForm.setLanguage("FR");
        bookForm.setDifficulty("EASY");
        bookForm.setContent("something");
        List<String> categoreis = new ArrayList<>();
        categoreis.add("sport");
        bookForm.setCategories(categoreis);
        String bookJson = Json.toJson(bookForm).toString();
        return ok(bookJson);


    }


    public Result createUserAnswerForm() {
        UserAnswerForm userAnswerForm = new UserAnswerForm();
        userAnswerForm.setIdBook(24l);
        Map<Short, List<Short>> map = new HashMap<>();
        short i = 26;
        short j = 83;
        List<Short> list = new ArrayList<>();
        list.add(j);
        map.put(i, list);
        userAnswerForm.setQustionsAnswers(map);
        JsonNode jsonNode = Json.toJson(userAnswerForm);
        return ok(jsonNode);
    }


    class Person {
        protected String name;
        protected List<String> childrenNames = new ArrayList<>();


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getChildrenNames() {
            return childrenNames;
        }

        public void setChildrenNames(List<String> childrenNames) {
            this.childrenNames = childrenNames;
        }

        public void addChild(String child) {
            childrenNames.add(child);
        }
    }


}
