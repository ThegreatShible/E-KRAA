package Persistance;

import be.objectify.deadbolt.java.models.Subject;
import models.users.User;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

public interface UserDAO <User extends Subject> {
    CompletionStage<Optional<User>> getUser(UUID id);

    CompletionStage<Optional<User>> getUser(String id);
}
