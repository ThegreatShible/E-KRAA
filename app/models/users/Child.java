package models.users;

import authentication.permissions.AdminPermission;
import authentication.roles.AdminRole;
import be.objectify.deadbolt.java.models.Permission;
import be.objectify.deadbolt.java.models.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Child implements User {

    private final UUID id;
    public Child(UUID id){
            this.id = id;
    }

    @Override
    public List<? extends Role> getRoles() {
        List<Role> list = new  ArrayList();
        list.add(new AdminRole());
        return list;
    }

    @Override
    public List<? extends Permission> getPermissions() {
        ArrayList<Permission> list = new ArrayList<>();
        list.add(new AdminPermission());
        return new ArrayList<Permission>(list);
    }

    @Override
    public String getIdentifier() {
        return "Child";
    }

    @Override
    public UUID getID() {
        return id;
    }
}
