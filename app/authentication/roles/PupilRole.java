package authentication.roles;

import be.objectify.deadbolt.java.models.Role;

public class PupilRole implements Role {
    @Override
    public String getName() {
        return "Pupil";
    }
}
