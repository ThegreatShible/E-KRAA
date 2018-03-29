package controllers;

import Persistance.DAOs.BookDAO;
import Persistance.DAOs.BookDAOQuery;
import com.fasterxml.jackson.databind.JsonNode;
import forms.AnswerForm;
import forms.BookForm;
import forms.QuestionForm;
import models.book.Answer;
import models.book.Book;
import models.book.Categorie;
import models.book.Questions;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;


//TODO : Check auto increment annotation
//TODO : Check book creation constraint like 1 answer if no multiple choice, exact weighting

public class BookCreationController extends Controller {

    private BookDAO bookDAO;
    private BookDAOQuery bookDAOQuery;

    @Inject
    public BookCreationController(BookDAO bookDAO, BookDAOQuery bookDAOQuery) {
        this.bookDAO = bookDAO;
        this.bookDAOQuery = bookDAOQuery;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletableFuture<Result> addBook() {

        JsonNode jsonNode = request().body().asJson();
        String str = jsonNode.get("categories").toString();
        BookForm bookForm = Json.fromJson(jsonNode, BookForm.class);

        Date date = new Date();
        List<Categorie> categoriesList = new ArrayList<>();
        for (String cat : bookForm.getCategories()) {
            Categorie categories = new Categorie();
            categories.getCategoriePK().setCategorie(cat);
            categoriesList.add(categories);
        }
        List<Questions> questionsList = new ArrayList<>();
        for (QuestionForm questionForm : bookForm.getQuestions()) {
            Questions questions = new Questions();
            questions.setContent(questionForm.getQuestion());
            questions.setMultiplechoices(questionForm.isMultiple());
            List<Answer> answers = new ArrayList<>();
            for (AnswerForm answerForm : questionForm.getAnswers()) {
                Answer answer = new Answer();
                answer.setContent(answerForm.getAnswer());
                answer.setRight(answerForm.isRight());
                answers.add(answer);
            }
            questions.setAnswerList(answers);
            questionsList.add(questions);
        }

        Book book = new Book(bookForm.getContent(), date, bookForm.getLanguage(), bookForm.getDifficulty(),
                categoriesList, questionsList);


        return bookDAOQuery.create(book).thenApply(e -> {
            return ok("done");
        });


    }

    public CompletableFuture<Result> getAllBook() {
        return CompletableFuture.supplyAsync(() -> {
            return TODO;
        });
    }


}
