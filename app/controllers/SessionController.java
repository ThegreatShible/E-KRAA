package controllers;

import Persistance.DAOs.SessionDAO;
import models.session.Session;
import play.mvc.Controller;
import play.mvc.Result;
import services.scheduling.schedulerImpl;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


public class SessionController extends Controller {

    @Inject
    SessionDAO sessionDAO;
    @Inject
    schedulerImpl scheduler;


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
        //TODO : remove null ,until i create session form
        Session session = null;
        UUID userid = UUID.fromString(session().get("user"));
        return CompletableFuture.supplyAsync(() -> {
            scheduler.scheduleSessionStart(session);
            return ok("done");
        });
    }

    public CompletableFuture<Result> removeSession(String sessionID) {
        UUID session = UUID.fromString(sessionID);
        UUID userID = UUID.fromString(session().get("user"));
        return CompletableFuture.supplyAsync(() -> {
            scheduler.cancelSession(session, userID);
            return ok("done");
        });
    }

}
