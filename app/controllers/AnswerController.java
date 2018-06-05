package controllers;

import Persistance.DAOs.BookRepository;
import Persistance.DAOs.SessionDAO;
import Persistance.DAOs.UserDAO;
import com.fasterxml.jackson.databind.JsonNode;
import forms.QuestionAnswersForm;
import forms.UserAnswerForm;
import models.book.Book;
import models.book.UserAnswer;
import models.book.UserAnswerCreationException;
import models.session.Session;
import models.users.Pupil;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import scala.Option;
import services.mailing.MailingServiceImpl;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

//DONE

public class AnswerController extends Controller {

    private BookRepository bookRepository;
    private SessionDAO sessionDAO;
    private UserDAO userDAO;
    private MailingServiceImpl mailingService;
    private views.html.session.answerQuizz answerQuizz;

    @Inject
    public AnswerController(BookRepository bookRepository, SessionDAO sessionDAO,
                            UserDAO userDAO, MailingServiceImpl mailingService,
                            views.html.session.answerQuizz answerQuizz) {
        this.bookRepository = bookRepository;
        this.userDAO = userDAO;
        this.sessionDAO = sessionDAO;
        this.mailingService = mailingService;
        this.answerQuizz = answerQuizz;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletableFuture<Result> answerQuestion() {

        final JsonNode jsonNode = request().body().asJson();
        final UserAnswerForm userAnswerForm = Json.fromJson(jsonNode, UserAnswerForm.class);
        System.out.println("json : " + jsonNode);
        //final UUID sessionID = UUID.fromString(userAnswerForm.getSessionID());
        final UUID sessionID = UUID.fromString("b16bf9fc-d709-4a79-9d83-46ba5faee7b9");
        //final UUID userID = UUID.fromString(userAnswerForm.getUserID());
        final UUID userID = UUID.fromString("0abec3ef-f602-43e0-a0eb-ae4fb7c88c9b");
        return sessionDAO.getSessionById(sessionID).thenCompose(session -> {
            if (session.isEmpty())
                return CompletableFuture.supplyAsync(() -> notFound("session not found"));
            else {
                return bookRepository.find(session.get().getIdBook()).thenCompose(book -> {
                    try {
                        Map<Short, List<Short>> map = new HashMap<>();
                        for (QuestionAnswersForm qaf : userAnswerForm.getQuestionsAnswers()) {
                            short nq = (short) qaf.getNumQuestion();
                            List<Short> ans = new ArrayList<>();
                            for (Integer i : qaf.getAnswersNum()) {
                                short j = i.shortValue();
                                ans.add(j);
                            }
                            map.put(nq, ans);
                        }


                        UserAnswer userAnswer = UserAnswer.create(session.get(), userID, map, book.getQuestions());
                        final int score = book.getScoreFromAnswer(userAnswer);
                        CompletableFuture<Result> res = sessionDAO.createUserAnswer(book, userAnswer).thenCompose(x -> {
                            return userDAO.getPupil(userID).thenApply(pupil -> {
                                ctx().session().clear();
                                return redirect(routes.LoginController.loginPage());
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

    public Result answerQuizz(String session, String user) {
        UUID sessionID = UUID.fromString(session);
        UUID userID = UUID.fromString(user);
        try {
            Option<Session> sessionf = sessionDAO.getByID(sessionID).get(3, TimeUnit.SECONDS);
            Option<Pupil> userf = userDAO.getPupil(userID).get(3, TimeUnit.SECONDS);
            if (userf.isEmpty()) {
                return notFound("user not found");
            } else {
                if (sessionf.isEmpty())
                    return notFound("aucune session");
                else {
                    if (!sessionf.get().isActive()) {
                        return badRequest("session not active");
                    } else {
                        if (sessionf.get().getGroupID() == userf.get().getGroupID()) {
                            Book book = bookRepository.find(sessionf.get().getIdBook()).get(3, TimeUnit.SECONDS);
                            ctx().session().put("user", user);
                            return ok(answerQuizz.render(book));
                        } else {
                            return badRequest("vous n'apparetenez pas a cette sesison");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return internalServerError();
        }
    }

}
