package controllers;

import Persistance.DAOs.UserDAO;
import forms.AuthForm;
import forms.PupilForm;
import forms.TeacherForm;
import models.users.Teacher;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Http;
import play.mvc.Result;
import scala.concurrent.ExecutionContext;
import services.mailing.MailingService;
import services.mailing.MailingServiceImpl;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static play.mvc.Controller.request;
import static play.mvc.Controller.session;
import static play.mvc.Results.*;

public class LoginController {


    private final views.html.auth.login Login;
    private final Form<TeacherForm> teacherForm;
    private final UserDAO userDAO;
    private final ExecutionContext executionContext;
    private final Form<AuthForm> authForm;
    private final Form<PupilForm> pupilForm;
    private final MailingService mailingService;

    @Inject
    public LoginController(FormFactory formFactory, UserDAO userDAO,
                           ExecutionContext executionContext, MailingServiceImpl mailingService,
                           views.html.auth.login Login) {
        teacherForm = formFactory.form(TeacherForm.class);
        authForm = formFactory.form(AuthForm.class);
        this.userDAO = userDAO;
        this.executionContext = executionContext;
        this.pupilForm = formFactory.form(PupilForm.class);
        this.mailingService = mailingService;
        this.Login = Login;
    }


    public Result loginPage() {
        return ok(Login.render());
    }

    public CompletableFuture<Result> authenticate() {
        Form<AuthForm> bindAuthForm = authForm.bindFromRequest();
        if (bindAuthForm.hasErrors())
            return CompletableFuture.supplyAsync(() -> badRequest());
        else {
            AuthForm af = bindAuthForm.get();

            return userDAO.authenticate(af.getEmail(), af.getPassword()).thenApply(e -> {

                if (e.isEmpty()) return ok("not found");
                session().put("user", e.get().getId().toString());
                return ok("found");
            });
        }
    }

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
            return userDAO.createTeacher(teacher, bindTeacherFormEntity.getPassword()).thenApply(tokenid -> {
                try {

                    //TODO : remove absolute path

                    //String filePath = routes.Assets.versioned("")
                    File file = new File("C:\\Users\\nabih\\IdeaProjects\\E-KRAA-Exp\\public\\UserProfile\\" + fileid.toString() + ".png");
                    OutputStream outputStream = new FileOutputStream(file);
                    Files.copy(profile.toPath(), outputStream);
                    mailingService.sendSignUpConfirmationMail(teacher.getEmail(), tokenid.toString());
                    return ok(file.getAbsolutePath());

                } catch (Exception e) {
                    e.printStackTrace();
                    return badRequest();
                }

            });
        }
    }

    public CompletableFuture<Result> confirmAuthentication(String tokenID) {
        UUID token = UUID.fromString(tokenID);
        //token = UUID.randomUUID();
        return userDAO.confirmUserByToken(token).thenApply(e -> {
            if (e.isEmpty()) {
                return badRequest("token errone");
            } else {
                session().put("user", e.get().toString());
                return redirect("");
            }
        });

    }
}
