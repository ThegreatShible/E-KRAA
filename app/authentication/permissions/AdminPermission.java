package authentication.permissions;

import be.objectify.deadbolt.java.models.Permission;

public class AdminPermission implements Permission {
    @Override
    public String getValue() {
        return "Admin";
    }
}
