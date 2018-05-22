package controllers;

import Persistance.DAOs.SessionDAO;
import forms.SessionForm;
import models.session.Session;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import services.scheduling.schedulerImpl;
import views.html.session.sessionCreation;
import views.html.session.sessionList;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


public class SessionController extends Controller {

    private SessionDAO sessionDAO;
    private schedulerImpl scheduler;
    private Form<SessionForm> sessionForm;
    private sessionList SessionList;
    private sessionCreation SessionCreation;


    @Inject
    public SessionController(SessionDAO sessionDAO, schedulerImpl scheduler,
                             FormFactory formFactory, sessionCreation SessionCreation,
                             sessionList SessionList
    ) {
        this.sessionDAO = sessionDAO;
        this.scheduler = scheduler;
        this.sessionForm = formFactory.form(SessionForm.class);
        this.SessionCreation = SessionCreation;
        this.SessionList = SessionList;
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


    public CompletableFuture<Result> createSession() {
        Form<SessionForm> bindSessionFrom = sessionForm.bindFromRequest();
        if (bindSessionFrom.hasErrors()) {
            return CompletableFuture.supplyAsync(() -> notFound());
        } else {
            UUID sessionID = UUID.randomUUID();
            Session session = bindSessionFrom.get().toSession(sessionID);
            UUID userid = UUID.fromString(session().get("user"));
            return CompletableFuture.supplyAsync(() -> {
                scheduler.scheduleSessionStart(session);
                return ok("done");
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
        return ok(SessionCreation.render());
    }

}
