package controllers;

import Persistance.DAOs.BookRepository;
import Persistance.DAOs.UserAnswerDAO;
import com.fasterxml.jackson.databind.JsonNode;
import forms.UserAnswerForm;
import models.book.UserAnswer;
import models.book.UserAnswerCreationException;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

public class AnswerController extends Controller {

    private BookRepository bookRepository;
    private UserAnswerDAO userAnswerDAO;

    @Inject
    public AnswerController(BookRepository bookRepository, UserAnswerDAO userAnswerDAO) {
        this.bookRepository = bookRepository;
        this.userAnswerDAO = userAnswerDAO;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletableFuture<Result> answerQuesions() {
        final JsonNode jsonNode = request().body().asJson();
        final UserAnswerForm userAnswerForm = Json.fromJson(jsonNode, UserAnswerForm.class);
        return bookRepository.getQuestionFromBook(userAnswerForm.getIdBook()).thenCompose(questionList -> {
            try {
                UserAnswer userAnswer = UserAnswer.create(userAnswerForm.getIdBook(), userAnswerForm.getQustionsAnswers(), questionList);
                //TODO : Replace created
                return userAnswerDAO.create(userAnswer).thenApply(x -> ok("created"));
            } catch (UserAnswerCreationException e) {
                e.printStackTrace();
                return CompletableFuture.supplyAsync(() -> internalServerError());
            }

        });
    }
}
