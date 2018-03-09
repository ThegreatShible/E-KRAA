package authTest;

import Persistance.UserDAO;
import models.users.Child;
import models.users.User;

import java.util.*;
import java.util.concurrent.CompletionStage;

public class TestUserDAO implements UserDAO <User> {

    private static Map<UUID,User> userList =  new HashMap();
    static {
        userList.put(UUID.fromString("1"), new Child(UUID.fromString("1")));
        userList.put(UUID.fromString("2"), new Child(UUID.fromString("2")));
        userList.put(UUID.fromString("3"), new Child(UUID.fromString("3")));

    }

    @Override
    public CompletionStage<Optional<User>> getUser(UUID id) {
        return null;
    }

    @Override
    public CompletionStage<Optional<User>> getUser(String id) {
        return null;
    }
}
