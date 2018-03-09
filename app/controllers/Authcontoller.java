package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.cache.HandlerCache;
import org.springframework.context.annotation.Role;

import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.UUID;

public class Authcontoller extends Controller {

    private HandlerCache handlerCache;

    @Inject()
    public Authcontoller(HandlerCache handlerCache){
        this.handlerCache = handlerCache;
    }
    @Restrict(@Group("Admin"))
    public Result connect(){
        return ok("you are connected");
    }

    public Result createAccount(){

    }
}
