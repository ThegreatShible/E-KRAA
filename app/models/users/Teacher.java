package models.users;

import services.EmailVerifier;

import javax.persistence.Column;
import java.util.Date;
import java.util.UUID;

public class Teacher extends User {
    @Column(name = "photolink")
    private String photoLink;
    //TODO : define excepiton for gender creation and email validation
    public static Teacher create(UUID id, String firstName, String lastName, Date birthDate, String photoLink, String gender, String email) {
        GENDER gender1 = GENDER.valueOf(gender);
        if (!EmailVerifier.verify(email)) throw new IllegalArgumentException("incorrect mail format");
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        teacher.setGender(gender);
        teacher.setBirthDate(birthDate);
        teacher.setEmail(email);
        teacher.setPhotoLink(photoLink);

        return teacher;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }
}
