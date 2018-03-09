package authentication.roles;

import be.objectify.deadbolt.java.models.Role;

public class AdminRole implements Role {
    @Override
    public String getName() {
        return "Admin";
    }
}
