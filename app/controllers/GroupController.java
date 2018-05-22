package controllers;

import Persistance.DAOs.GroupDAO;
import forms.GroupForm;
import models.users.Group;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GroupController extends Controller {

    private GroupDAO groupDAO;
    private Form<GroupForm> groupForm;

    @Inject
    public GroupController(GroupDAO groupDAO, FormFactory formFactory) {

        this.groupDAO = groupDAO;
        this.groupForm = formFactory.form(GroupForm.class);
    }

    public CompletableFuture<Result> getGroups() {
        return groupDAO.getGroups().thenApply(groups -> {
            return ok("done");
        });
    }

    public CompletableFuture<Result> createGroup() {
        Form<GroupForm> groupFromBind = groupForm.bindFromRequest();
        if (groupFromBind.hasErrors()) {
            return CompletableFuture.supplyAsync(() -> notFound());
        } else {
            String session = session().get("user");
            UUID userID = UUID.fromString(session);
            Group group = groupFromBind.get().toGroup(0, userID);
            return groupDAO.createGroup(group).thenApply(groupid -> {
                return ok("done");
            });
        }
    }

    public CompletableFuture<Result> getGroup(final int groupid) {
        return groupDAO.getGroup(groupid).thenApply(group -> {
            return ok("done");
        });
    }
}
