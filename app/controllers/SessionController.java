package controllers;

import Persistance.DAOs.BookRepository;
import Persistance.DAOs.GroupDAO;
import Persistance.DAOs.SessionDAO;
import forms.SessionForm;
import models.book.Book;
import models.session.Session;
import models.users.Group;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
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
    private GroupDAO groupDAO;
    private BookRepository bookRepository;


    @Inject
    public SessionController(SessionDAO sessionDAO, schedulerImpl scheduler,
                             FormFactory formFactory, views.html.session.sessionCreation SessionCreation,
                             views.html.session.sessionList SessionList, BookRepository bookRepository,
                             GroupDAO groupDAO
    ) {
        this.sessionDAO = sessionDAO;
        this.scheduler = scheduler;
        this.sessionForm = formFactory.form(SessionForm.class);
        this.SessionCreation = SessionCreation;
        this.SessionList = SessionList;
        this.groupDAO = groupDAO;
        this.bookRepository = bookRepository;
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
            groups = groupDAO.getGroups().get(2, TimeUnit.SECONDS);
            books = bookRepository.findAll().get(2, TimeUnit.SECONDS);
            return ok(SessionCreation.render(groups, books));
        } catch (Exception e) {
            e.printStackTrace();
            return internalServerError();
        }

    }

}
