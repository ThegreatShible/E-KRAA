package controllers;

import Persistance.DAOs.BookRepository;
import Persistance.DAOs.GroupDAO;
import Persistance.DAOs.SessionDAO;
import Persistance.DAOs.UserDAO;
import forms.SessionForm;
import models.book.Book;
import models.session.Session;
import models.users.Group;
import models.users.Pupil;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import scala.Option;
import services.scheduling.schedulerImpl;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


public class SessionController extends Controller {

    private SessionDAO sessionDAO;
    private schedulerImpl scheduler;
    private Form<SessionForm> sessionForm;
    private views.html.session.sessionList SessionList;
    private views.html.session.sessionCreation SessionCreation;
    private views.html.session.answerQuizz answerQuizz;
    private GroupDAO groupDAO;
    private BookRepository bookRepository;
    private UserDAO userDAO;


    @Inject
    public SessionController(SessionDAO sessionDAO, schedulerImpl scheduler,
                             FormFactory formFactory, views.html.session.sessionCreation SessionCreation,
                             views.html.session.sessionList SessionList, BookRepository bookRepository,
                             GroupDAO groupDAO, UserDAO userDAO, views.html.session.answerQuizz answerQuizz
    ) {
        this.sessionDAO = sessionDAO;
        this.scheduler = scheduler;
        this.sessionForm = formFactory.form(SessionForm.class);
        this.SessionCreation = SessionCreation;
        this.SessionList = SessionList;
        this.groupDAO = groupDAO;
        this.bookRepository = bookRepository;
        this.userDAO = userDAO;
        this.answerQuizz = answerQuizz;
    }



    public CompletableFuture<Result> getSession(String sessionID) {
        UUID session = UUID.fromString(sessionID);
        UUID userid = UUID.fromString(session().get("user"));
        //UUID session = UUID.randomUUID();
        //UUID userid = UUID.randomUUID();
        return sessionDAO.getSessionById(session, userid).thenApply(sessionOp -> {
            return ok("done");
        });
    }


    //TODO : Add some constraint

    public CompletableFuture<Result> createSession() {
        Form<SessionForm> bindSessionFrom = sessionForm.bindFromRequest();
        if (bindSessionFrom.hasErrors()) {
            bindSessionFrom.allErrors().forEach(x -> System.out.println(x));
            return CompletableFuture.supplyAsync(() -> ok("thanks"));
        } else {
            UUID sessionID = UUID.randomUUID();
            Session session = bindSessionFrom.get().toSession(sessionID);
            //UUID userid = UUID.fromString(session().get("user"));
            return CompletableFuture.supplyAsync(() -> {
                scheduler.scheduleSessionStart(session);
                return redirect(routes.SessionController.sessionList());
            });
        }
    }

    public CompletableFuture<Result> removeSession(String sessionID) {
        UUID session = UUID.fromString(sessionID);
        UUID userID = UUID.fromString(session().get("user"));
        return CompletableFuture.supplyAsync(() -> {
            scheduler.cancelSession(session, userID);
            return ok("done");
        });
    }

    public Result sessionList() {
        return ok(SessionList.render());
    }

    public Result sessionCreation() {
        List<Group> groups = null;
        List<Book> books;

        try {
            UUID uuid = UUID.fromString(session("user"));
            groups = groupDAO.getGroups().get(2, TimeUnit.SECONDS);
            books = bookRepository.findAll(uuid).get(2, TimeUnit.SECONDS);
            return ok(SessionCreation.render(groups, books));
        } catch (Exception e) {
            e.printStackTrace();
            return internalServerError();
        }

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
                            Book book = bookRepository.find(sessionf.get().getIdBook()).get(2, TimeUnit.SECONDS);
                            ctx().session().put("user", user);
                            return ok(answerQuizz.render(book));
                        } else {
                            return badRequest("vous n'apparetenez pas a cette sesison");
                        }
                    }
                }
            }
        } catch (Exception e) {
            return internalServerError();
        }
    }
}
