package controllers;

import Persistance.DAOs.GroupDAO;
import models.users.Group;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

public class GroupController extends Controller {

    GroupDAO groupDAO;

    @Inject
    public GroupController(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    public CompletableFuture<Result> getGroups() {
        return groupDAO.getGroups().thenApply(groups -> {
            return ok("done");
        });
    }

    public CompletableFuture<Result> createGroup() {
        //TODO : This is dangerous until i create the group form
        Group group = null;
        return groupDAO.createGroup(group).thenApply(groupid -> {
            return ok("done");
        });
    }

    public CompletableFuture<Result> getGroup(final int groupid) {
        return groupDAO.getGroup(groupid).thenApply(group -> {
            return ok("done");
        });
    }
}
