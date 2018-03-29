package controllers;


import Persistance.DAOs.BookRepository;
import com.fasterxml.jackson.databind.JsonNode;
import forms.AnswerForm;
import forms.BookForm;
import forms.QuestionForm;
import models.book.Answer;
import models.book.Book;
import models.book.BookCreationException;
import models.book.Question;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

//TODO : add orderby into database

public class TestController extends Controller {


    private BookRepository bookRepository;
    private MessagesApi messagesApi;

    @Inject
    public TestController(MessagesApi messagesApi, BookRepository bookRepository) {

        this.messagesApi = messagesApi;
        this.bookRepository = bookRepository;
    }

    public Result test() throws BookCreationException {
        Answer answer = new Answer((short) 1, "yes", true);
        List<Answer> answers = new ArrayList<>();
        answers.add(answer);
        Question q = Question.create((short) 1, "what?", true, (short) 10, answers);
        List<Question> questions = new ArrayList<>();
        questions.add(q);
        List<String> cat = new ArrayList<>();
        cat.add("sport");
        cat.add("Math");
        Book book = Book.create("something", "sometitle", "FR", "EASY", questions, cat);
        book.setLastModifDate(new Date());
        JsonNode jsonNode = Json.toJson(book);
        return ok(jsonNode.toString());

    }


    @BodyParser.Of(BodyParser.Json.class)

    public Result test2() {

        JsonNode jsonNode = request().body().asJson();
        Person p = Json.fromJson(jsonNode, Person.class);
        String rejson = Json.toJson(p).toString();
        return ok(rejson);

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
