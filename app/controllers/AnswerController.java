package controllers;

import Persistance.DAOs.BookRepository;
import Persistance.DAOs.SessionDAO;
import Persistance.DAOs.UserDAO;
import com.fasterxml.jackson.databind.JsonNode;
import forms.UserAnswerForm;
import models.book.UserAnswer;
import models.book.UserAnswerCreationException;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.mailing.MailingServiceImpl;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

//DONE

public class AnswerController extends Controller {

    private BookRepository bookRepository;
    private SessionDAO sessionDAO;
    private UserDAO userDAO;
    private MailingServiceImpl mailingService;
    @Inject
    public AnswerController(BookRepository bookRepository, SessionDAO sessionDAO, UserDAO userDAO, MailingServiceImpl mailingService) {
        this.bookRepository = bookRepository;
        this.userDAO = userDAO;
        this.sessionDAO = sessionDAO;
        this.mailingService = mailingService;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletableFuture<Result> answerQuestion() {
        UUID userID = UUID.fromString(session("user"));
        final JsonNode jsonNode = request().body().asJson();
        final UserAnswerForm userAnswerForm = Json.fromJson(jsonNode, UserAnswerForm.class);
        final UUID sessionID = UUID.fromString(userAnswerForm.getSessionID());
        return sessionDAO.getSessionById(sessionID, userID).thenCompose(session -> {
            if (session.isEmpty())
                return CompletableFuture.supplyAsync(() -> notFound());
            else {
                return bookRepository.find(session.get().getIdBook()).thenCompose(book -> {
                    try {

                        UserAnswer userAnswer = UserAnswer.create(session.get(), userID, userAnswerForm.getQustionsAnswers(), book.getQuestions());
                        final int score = book.getScoreFromAnswer(userAnswer);
                        CompletableFuture<Result> res = sessionDAO.createUserAnswer(book, userAnswer).thenCompose(x -> {
                            return userDAO.getPupil(userID).thenApply(pupil -> {
                                mailingService.sendExamResult(pupil.get().getEmail(), score);
                                //TODO : replace done
                                return ok("done");
                            });
                        });
                        return res;
                    } catch (UserAnswerCreationException e) {
                        e.printStackTrace();
                        return CompletableFuture.supplyAsync(() -> internalServerError());
                    }

                });
            }
        });
    }

    public CompletableFuture<Result> getBookAnswer(String sessionid) {
        UUID userid = UUID.fromString(session("user"));
        // UUID userid = UUID.randomUUID();
        return sessionDAO.findUserAnswer(UUID.fromString(sessionid), userid).thenApply(x -> {
            if (x.isEmpty())
                return notFound();
            else {
                return x.map(answer -> {
                    JsonNode jsonNode = Json.toJson(answer);
                    return ok(jsonNode.toString());
                }).get();
            }
        });
    }

}
