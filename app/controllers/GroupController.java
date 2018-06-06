package controllers;

import Persistance.DAOs.GroupDAO;
import forms.GroupForm;
import forms.JsonHelpers.GroupJson;
import forms.JsonHelpers.GroupListJson;
import models.users.Group;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GroupController extends Controller {

    private GroupDAO groupDAO;
    private Form<GroupForm> groupForm;
    private views.html.group.groupCreation GroupCreation;
    private views.html.group.groupList GroupList;

    @Inject
    public GroupController(GroupDAO groupDAO, FormFactory formFactory,
                           views.html.group.groupCreation GroupCreation, views.html.group.groupList GroupList) {

        this.groupDAO = groupDAO;
        this.groupForm = formFactory.form(GroupForm.class);
        this.GroupCreation = GroupCreation;
        this.GroupList = GroupList;
    }

    //TODO : groups are global
    public CompletableFuture<Result> getGroups() {
        return groupDAO.getGroups().thenApply(groups -> {
            List<GroupJson> groupJsons = new ArrayList<>();
            for (Group group : groups) {
                GroupJson groupJson = GroupJson.fromGroup(group);
                groupJsons.add(groupJson);
            }
            GroupListJson groupListJson = new GroupListJson();
            groupListJson.setGroups(groupJsons);
            String str  = Json.toJson(groupListJson).toString();
            return ok(GroupList.render(str));
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
                return redirect(routes.GroupController.groupList());
            });
        }
    }

    public CompletableFuture<Result> getGroup(final int groupid) {
        return groupDAO.getGroup(groupid).thenApply(group -> {
            return ok("done");
        });
    }

    public CompletableFuture<Result> groupList() {
        return groupDAO.getGroups().thenApply(groups -> {
            List<GroupJson> groupJsons = new ArrayList<>();
            for (Group group : groups) {
                GroupJson groupJson = GroupJson.fromGroup(group);
                groupJsons.add(groupJson);
            }
            GroupListJson groupListJson = new GroupListJson();
            groupListJson.setGroups(groupJsons);
            String str  = Json.toJson(groupListJson).toString();
            return ok(GroupList.render(str));
        });
    }

    public Result groupCreation() {
        return ok(GroupCreation.render());
    }
}
