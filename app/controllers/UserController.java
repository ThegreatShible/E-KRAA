package controllers;


import Persistance.DAOs.GroupDAO;
import Persistance.DAOs.UserDAO;
import forms.AuthForm;
import forms.JsonHelpers.PupilJson;
import forms.JsonHelpers.PupilJsonList;
import forms.PupilForm;
import forms.TeacherForm;
import models.users.Group;
import models.users.Pupil;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.ExecutionContext;
import services.mailing.MailingService;
import services.mailing.MailingServiceImpl;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class UserController extends Controller {

    private final Form<TeacherForm> teacherForm;
    private final UserDAO userDAO;
    private final ExecutionContext executionContext;
    private final Form<AuthForm> authForm;
    private final Form<PupilForm> pupilForm;
    private final MailingService mailingService;
    private final views.html.pupil.pupilList PupilList;
    private final views.html.pupil.createPupil PupilCreation;
    private final GroupDAO groupDAO;


    @Inject
    public UserController(FormFactory formFactory, UserDAO userDAO,
                          ExecutionContext executionContext, MailingServiceImpl mailingService,
                          views.html.pupil.pupilList PupilList, views.html.pupil.createPupil PupilCreation,
                          GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
        teacherForm = formFactory.form(TeacherForm.class);
        authForm = formFactory.form(AuthForm.class);
        this.userDAO = userDAO;
        this.executionContext = executionContext;
        this.pupilForm = formFactory.form(PupilForm.class);
        this.mailingService = mailingService;
        this.PupilCreation = PupilCreation;
        this.PupilList = PupilList;
    }

    //TODO : add session and redirect to userpage
    //TODO : do something with file exception problem
    //TODO : check if file is jpg


    public Result createPupil() {

        Form<PupilForm> bindPupilForm = pupilForm.bindFromRequest();
        if (bindPupilForm.hasErrors()) {
            bindPupilForm.allErrors().forEach(er -> System.out.println(er));

                return badRequest("badRequest");
        } else {

            PupilForm bindPupilFormEntity = bindPupilForm.get();
            UUID userID = UUID.randomUUID();
            UUID fileid = UUID.randomUUID();
            Pupil pupil = bindPupilFormEntity.toPupil(userID, fileid.toString() + ".png");
            UUID tokenid = null;

            try {
                tokenid = userDAO.createPupil(pupil).get(3, TimeUnit.SECONDS);
                String link = routes.LoginController.confirmAuthentication(tokenid.toString()).url();
                String i = mailingService.sendSignUpConfirmationMail(pupil.getEmail(), link);
                return redirect(routes.UserController.pupilList());
            } catch (Exception e) {
                return notFound();
            }
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

    public Result pupilList() {
        UUID teacherID = UUID.fromString(session("user"));
        try {
            List<Group> groups = groupDAO.getGroups().get(3, TimeUnit.SECONDS);

            List<Pupil> pupils = new ArrayList<>();
            for (Group group : groups) {
                List<Pupil> pupilList = userDAO.getPupilsByGroup(group.getIdGroup()).get(3, TimeUnit.SECONDS);
                pupils.addAll(pupilList);
                pupilList.forEach(x -> System.out.println("PUPIL GR : "+x.getFirstName() + " " + group.getGroupName()));
            }
            List<PupilJson> pupilJsonList = new ArrayList<>();
            for (Pupil pupil : pupils) {
                PupilJson pupilJson = PupilJson.fromPupil(pupil);
                pupilJsonList.add(pupilJson);
            }
            PupilJsonList pupilJsonList1 = new PupilJsonList();
            pupilJsonList1.setPupils(pupilJsonList);
            String str = Json.toJson(pupilJsonList1).toString();
            return ok(PupilList.render(str));
        } catch (Exception e) {
            e.printStackTrace();
            return internalServerError();
        }
    }

    public CompletableFuture<Result> pupilCreation() {
        return groupDAO.getGroups().thenApply(groups -> {
            return ok(PupilCreation.render(groups));

        });
    }


    //TODO : Replace response



    //TODO : redirect to landing page


}
