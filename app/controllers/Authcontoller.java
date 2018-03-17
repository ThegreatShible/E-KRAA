package controllers;

import Persistance.UserDAO;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.cache.HandlerCache;
import forms.UserForm;
import models.users.User;
import org.springframework.context.annotation.Role;

import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class Authcontoller extends Controller {

    private HandlerCache handlerCache;
    private FormFactory formFactory;
    private UserDAO userDAO;

    @Inject()
    public Authcontoller(HandlerCache handlerCache, FormFactory formFactory, UserDAO userDAO){
        this.handlerCache = handlerCache;
        this.formFactory = formFactory;

    }

    public CompletionStage<Result> authenticate() {
        Form<UserForm> authForm = formFactory.form(UserForm.class).bindFromRequest();
        if(authForm.hasErrors())
            return CompletableFuture.supplyAsync(() -> badRequest());
        else{
            UserForm userForm = authForm.get();
            String email = userForm.getEmail();
            CompletionStage<Optional<User>> userFuture = userDAO.getUser(email);
            return userFuture.thenApply(user -> ok(user.toString()));
            //TODO : remplacer par la page d'acceuil
            //TODO: check if the DAO's return type is compatible with hibernate

        }





    }





}
