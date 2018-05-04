package controllers;


import Persistance.DAOs.UserDAO;
import forms.AuthForm;
import forms.PupilForm;
import forms.TeacherForm;
import models.users.Pupil;
import models.users.Teacher;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Http;
import play.mvc.Result;
import scala.concurrent.ExecutionContext;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static play.mvc.Controller.request;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

public class UserController {

    private final Form<TeacherForm> teacherForm;
    private final UserDAO userDAO;
    private final ExecutionContext executionContext;
    private final Form<AuthForm> authForm;
    private final Form<PupilForm> pupilForm;

    @Inject
    public UserController(FormFactory formFactory, UserDAO userDAO, ExecutionContext executionContext) {
        teacherForm = formFactory.form(TeacherForm.class);
        authForm = formFactory.form(AuthForm.class);
        this.userDAO = userDAO;
        this.executionContext = executionContext;
        this.pupilForm = formFactory.form(PupilForm.class);
    }

    //TODO : add session and redirect to userpage


    //TODO : do something with file exception problem
    //TODO : check if file is jpg
    public CompletableFuture<Result> createTeacher() {

        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        final File profile = body.getFile("profilePicture").getFile();
        Form<TeacherForm> bindTeacherForm = teacherForm.bindFromRequest();
        if (bindTeacherForm.hasErrors()) {
            return CompletableFuture.supplyAsync(() -> {
                return badRequest("badRequest");
            });
        } else {

            TeacherForm bindTeacherFormEntity = bindTeacherForm.get();
            UUID userID = UUID.randomUUID();
            UUID fileid = UUID.randomUUID();

            Teacher teacher = bindTeacherFormEntity.toTeacher(userID, fileid.toString());
            return userDAO.createTeacher(teacher, bindTeacherFormEntity.getPassword()).thenApply(r -> {
                try {
                    File file = new File("C:\\Users\\nabih\\IdeaProjects\\E-KRAA-Exp\\public\\UserProfile\\" + fileid.toString() + ".png");
                    OutputStream outputStream = new FileOutputStream(file);
                    Files.copy(profile.toPath(), outputStream);
                    return ok(file.getAbsolutePath());

                } catch (Exception e) {
                    e.printStackTrace();
                    return badRequest();
                }

            });
        }
    }

    public CompletableFuture<Result> createPupil() {
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        final File profile = body.getFile("profilePicture").getFile();
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
            return userDAO.createPupil(pupil, bindPupilFormEntity.getPassword()).thenApply(r -> {
                try {
                    //TODO : Na7i absolute URL
                    File file = new File("C:\\Users\\nabih\\IdeaProjects\\E-KRAA-Exp\\public\\UserProfile\\" + fileid.toString() + ".png");
                    OutputStream outputStream = new FileOutputStream(file);
                    Files.copy(profile.toPath(), outputStream);
                    return ok(file.getAbsolutePath());

                } catch (Exception e) {
                    e.printStackTrace();
                    return badRequest();
                }

            });
        }


    }

    //TODO : Replace respondse add session
    public CompletableFuture<Result> authenticate() {
        Form<AuthForm> bindAuthForm = authForm.bindFromRequest();
        if (bindAuthForm.hasErrors())
            return CompletableFuture.supplyAsync(() -> badRequest());
        else {
            AuthForm af = bindAuthForm.get();
            return userDAO.authenticate(af.getEmail(), af.getPassword()).thenApply(e -> {
                if (e.isEmpty()) return ok("not found");
                return ok("found");
            });
        }
    }


}
