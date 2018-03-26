package models.users;


import java.util.UUID;

public class Child {

    private final UUID id;
    public Child(UUID id){
            this.id = id;
    }


    public UUID getID() {
        return id;
    }
}
