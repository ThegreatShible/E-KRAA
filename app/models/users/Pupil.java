package models.users;

import services.EmailVerifier;

import javax.persistence.Entity;
import java.util.Date;
import java.util.UUID;

@Entity
public class Pupil extends User {
    private short level;
    private int score;
    private int groupID;

    public Pupil() {

    }

    public static Pupil create(UUID id, String firstName, String lastName, Date birthDate, String gender, String email) {
        GENDER gender1 = GENDER.valueOf(gender);
        if (!EmailVerifier.verify(email)) throw new IllegalArgumentException("incorrect mail format");
        Pupil pupil = new Pupil();
        pupil.setId(id);
        pupil.setFirstName(firstName);
        pupil.setLastName(lastName);
        pupil.setGender(gender);
        pupil.setBirthDate(birthDate);
        pupil.setEmail(email);
        pupil.setScore(0);
        short s = 1;
        pupil.setLevel(s);

        return pupil;
    }

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }
}
