package controllers;


import Persistance.DAOs.UserDAO;
import forms.AuthForm;
import forms.PupilForm;
import forms.TeacherForm;
import models.users.Pupil;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;
import scala.concurrent.ExecutionContext;
import services.mailing.MailingService;
import services.mailing.MailingServiceImpl;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

public class UserController {

    private final Form<TeacherForm> teacherForm;
    private final UserDAO userDAO;
    private final ExecutionContext executionContext;
    private final Form<AuthForm> authForm;
    private final Form<PupilForm> pupilForm;
    private final MailingService mailingService;

    @Inject
    public UserController(FormFactory formFactory, UserDAO userDAO,
                          ExecutionContext executionContext, MailingServiceImpl mailingService) {
        teacherForm = formFactory.form(TeacherForm.class);
        authForm = formFactory.form(AuthForm.class);
        this.userDAO = userDAO;
        this.executionContext = executionContext;
        this.pupilForm = formFactory.form(PupilForm.class);
        this.mailingService = mailingService;
    }

    //TODO : add session and redirect to userpage
    //TODO : do something with file exception problem
    //TODO : check if file is jpg



    public CompletableFuture<Result> createPupil() {

        Form<PupilForm> bindPupilForm = pupilForm.bindFromRequest();
        if (bindPupilForm.hasErrors()) {
            return CompletableFuture.supplyAsync(() -> {
                return badRequest("badRequest");
            });
        } else {

            PupilForm bindPupilFormEntity = bindPupilForm.get();
            UUID userID = UUID.randomUUID();
            UUID fileid = UUID.randomUUID();
            Pupil pupil = bindPupilFormEntity.toPupil(userID, fileid.toString() + ".png");
            return userDAO.createPupil(pupil).thenApply(tokenid -> {
                try {
                    //String filePath = routes.Assets.versioned("")
                    mailingService.sendSignUpConfirmationMail(pupil.getEmail(), tokenid.toString());
                    return ok("done");

                } catch (Exception e) {
                    e.printStackTrace();
                    return badRequest();
                }

            });
        }


    }

    public CompletableFuture<Result> getTeacher(String userID) {
        UUID uuid = UUID.fromString(userID);
        //UUID uuid = UUID.randomUUID();
        return userDAO.getTeacher(uuid).thenApply(t -> {
            return ok("done");
        });
    }


    public CompletableFuture<Result> getPupil(String userID) {
        UUID uuid = UUID.fromString(userID);
        //UUID uuid = UUID.randomUUID();
        return userDAO.getPupil(uuid).thenApply(p -> {
            return ok("done");
        });
    }

    public CompletableFuture<Result> getPupils(int groupID) {
        return userDAO.getPupilsByGroup(groupID).thenApply(pupils -> {
            return ok("done");
        });
    }


    //TODO : Replace response



    //TODO : redirect to landing page


}
