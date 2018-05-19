package forms;

import models.users.Pupil;
import play.data.format.Formats;
import play.data.validation.Constraints;

import java.util.Date;
import java.util.UUID;

public class PupilForm {

    @Constraints.Required
    private String email;
    @Constraints.Required
    private String password;
    @Constraints.Required
    private String firstName;
    @Constraints.Required
    private String lastName;
    @Constraints.Required
    @Formats.DateTime(pattern = "dd/MM/yyyy")
    private Date birthDate;
    @Constraints.Required
    private String gender;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Pupil toPupil(UUID id, String link) {
        return Pupil.create(id, firstName, lastName, birthDate, link, gender, email);
    }


}
