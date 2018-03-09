package authentication;

import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import be.objectify.deadbolt.java.models.Subject;
import play.mvc.Http;
import play.mvc.Result;


import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import Persistance.UserDAO;

@Singleton
public class MyDeadboldHandler implements DeadboltHandler {
    private UserDAO userDAO;

    @Inject
    public MyDeadboldHandler(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    @Override
    public CompletionStage<Optional<Result>> beforeAuthCheck(Http.Context context) {
        return null;
    }

    @Override
    public CompletionStage<Optional<? extends Subject>> getSubject(Http.Context context) {
        String user = context.session().get("user");
        UUID userUUID = UUID.fromString(user);
        CompletionStage<Optional<? extends Subject>> sub = userDAO.getUser(userUUID);
        return sub;
    }

    @Override
    public CompletionStage<Result> onAuthFailure(Http.Context context, Optional<String> content) {
        return null;
    }

    @Override
    public CompletionStage<Optional<DynamicResourceHandler>> getDynamicResourceHandler(Http.Context context) {
        return null;
    }
}
