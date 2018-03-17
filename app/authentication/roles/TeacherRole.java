package authentication.roles;


import be.objectify.deadbolt.java.models.Role;

public class TeacherRole implements Role {
    @Override
    public String getName() {
        return "Teacher";
    }
}
