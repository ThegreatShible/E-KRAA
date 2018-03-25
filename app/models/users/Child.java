package models.users;

import authentication.permissions.AdminPermission;
import authentication.roles.AdminRole;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Child implements User {

    private final UUID id;
    public Child(UUID id){
            this.id = id;
    }



    @Override
    public UUID getID() {
        return id;
    }
}
