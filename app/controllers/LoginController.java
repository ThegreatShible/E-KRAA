package controllers;

import Persistance.DAOs.UserDAO;
import forms.AuthForm;
import forms.PupilForm;
import forms.TeacherForm;
import models.users.Teacher;
import models.users.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import scala.Option;
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
import java.util.concurrent.TimeUnit;

/////////////////////////////////


public class LoginController extends Controller {


    private final views.html.auth.login Login;
    private final Form<TeacherForm> teacherForm;
    private final UserDAO userDAO;
    private final ExecutionContext executionContext;
    private final Form<AuthForm> authForm;
    private final Form<PupilForm> pupilForm;
    private final MailingService mailingService;
    private final views.html.auth.waitingforconfirmation waitingforconfirmation;
    @Inject
    public LoginController(FormFactory formFactory, UserDAO userDAO,
                           ExecutionContext executionContext, MailingServiceImpl mailingService,
                           views.html.auth.login Login, views.html.auth.waitingforconfirmation wf) {

        teacherForm = formFactory.form(TeacherForm.class);
        authForm = formFactory.form(AuthForm.class);
        this.userDAO = userDAO;
        this.executionContext = executionContext;
        this.pupilForm = formFactory.form(PupilForm.class);
        this.mailingService = mailingService;
        this.Login = Login;
        this.waitingforconfirmation = wf;
    }


    public Result loginPage() {
        return ok(Login.render());
    }

    public Result authenticate() {
        Form<AuthForm> bindAuthForm = authForm.bindFromRequest();
        if (bindAuthForm.hasErrors())
            return notFound("erreure dans le formulaire");
        else {
            AuthForm af = bindAuthForm.get();

            CompletableFuture<Option<User>> u = userDAO.authenticate(af.getEmail(), af.getPassword());
            try {
                Option<User> op = u.get(3, TimeUnit.SECONDS);
                if (op.isEmpty()) {
                    return redirect(routes.LoginController.loginPage());
                } else {
                    session("user", op.get().getId().toString());
                    return redirect(routes.SessionController.sessionList());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return internalServerError();
            }
        }
    }

    public CompletableFuture<Result> createTeacher() {
        //TODO : revoir cela urgent
        /*Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> profile = body.getFile("profilePicture");*/
        Form<TeacherForm> bindTeacherForm = teacherForm.bindFromRequest();
        if (bindTeacherForm.hasErrors()) {
            return CompletableFuture.supplyAsync(() -> {
                bindTeacherForm.allErrors().forEach(x -> System.out.println("**************" + x));
                return ok("error in form");
            });
        } else {

            TeacherForm bindTeacherFormEntity = bindTeacherForm.get();
            UUID userID = UUID.randomUUID();
            Http.MultipartFormData.FilePart<File> profile = null;
            String fileid;
            if (profile == null) {
                fileid = "defaultProfilePic.png";
            } else {
                fileid = UUID.randomUUID().toString() + ".png";
            }

            Teacher teacher = bindTeacherFormEntity.toTeacher(userID, fileid.toString());
            return userDAO.createTeacher(teacher, bindTeacherFormEntity.getPassword()).thenApply(tokenid -> {
                try {

                    //TODO : remove absolute path

                    File file = new File("C:\\Users\\nabih\\IdeaProjects\\E-KRAA-Exp\\public\\UserProfile\\" + fileid.toString());
                    if (profile != null) {
                        //String filePath = routes.Assets.versioned("")
                        OutputStream outputStream = new FileOutputStream(file);
                        File uploadedProfile = profile.getFile();
                        Files.copy(uploadedProfile.toPath(), outputStream);
                    }
                    String link = routes.LoginController.confirmAuthentication(tokenid.toString()).url();
                    mailingService.sendSignUpConfirmationMail(teacher.getEmail(), link);
                    return redirect(routes.LoginController.afterSignUpPage());


                } catch (Exception e) {
                    e.printStackTrace();
                    return redirect(routes.LoginController.loginPage());

                }

            });
        }
    }

    public Result confirmAuthentication(String tokenID) {
        UUID token = UUID.fromString(tokenID);
        //token = UUID.randomUUID();
        CompletableFuture<Option<UUID>> res = userDAO.confirmUserByToken(token);

        try {
            Option<UUID> uuid = res.get(3, TimeUnit.SECONDS);
            if (uuid.isEmpty()) {
                return notFound();
            } else {
                return redirect(routes.LoginController.loginPage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return internalServerError();
        }


    }

    public Result logout() {
        session().clear();
        return redirect(routes.LoginController.loginPage());
    }

    public Result afterSignUpPage() {
        return ok(waitingforconfirmation.render());
    }


}
