package authTest;

import models.users.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

public class TestUserDAO {

    private static Map<UUID,User> userList =  new HashMap();
    static {


    }


    public CompletionStage<Optional<User>> getUser(UUID id) {
        return null;
    }

    public CompletionStage<Optional<User>> getUser(String id) {
        return null;
    }
}
