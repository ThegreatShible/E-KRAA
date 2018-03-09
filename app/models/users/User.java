package models.users;

import be.objectify.deadbolt.java.models.Subject;

import java.util.UUID;

public interface User extends Subject{
    UUID getID();




}
