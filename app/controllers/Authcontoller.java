package controllers;

import Persistance.TestDAO;
import models.TestEntity;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import play.i18n.Lang;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class Authcontoller extends Controller {

    /*private HandlerCache handlerCache;
    private FormFactory formFactory;
    private UserDAO userDAO;*/

    /*@Inject()
    public Authcontoller(HandlerCache handlerCache, FormFactory formFactory, UserDAO userDAO){
        this.handlerCache = handlerCache;
        this.formFactory = formFactory;
    }*/
    private MessagesApi messagesApi;
    private TestDAO testDAO;

    @Inject
    public Authcontoller(MessagesApi messagesApi, TestDAO testDAO){

        this.messagesApi = messagesApi;
        this.testDAO = testDAO;

    }

    /*public CompletionStage<Result> authenticate() {
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
    }*/

    public Result test(){
        Collection<Lang> candidates = Collections.singletonList(new Lang(Locale.forLanguageTag("ar")));

        Messages messages = messagesApi.preferred(candidates);

        String message = messages.at("hello");
        String title = messagesApi.get(Lang.forCode("ar"), "hello");

        return ok(views.html.test.render(messages,title));

    }


    public CompletableFuture<Result> test2(String name){
        return testDAO.test(name).thenApply(str -> {
            return ok(str);
        });
    }

    public Result test3(String name) {
        Session session = new Configuration().configure().buildSessionFactory().openSession();
        session.beginTransaction();
        TestEntity te = new TestEntity();
        te.setName(name);
        session.save(te);
        session.getTransaction().commit();
        return ok("done");
    }






}
