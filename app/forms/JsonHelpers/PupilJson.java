package forms.JsonHelpers;

import models.users.Pupil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PupilJson {
    private String firstName;
    private String lastName;
    private String gender;
    private String birthDate;
    private String groupID;

    public static PupilJson fromPupil(Pupil pupil) {
        PupilJson pupilJson = new PupilJson();
        pupilJson.setFirstName(pupil.getFirstName());
        pupilJson.setLastName(pupil.getLastName());
        pupilJson.setGender(pupil.getGender());
        pupilJson.setGroupID(new Integer(pupil.getGroupID()).toString());
        DateFormat format = new SimpleDateFormat("dd/mm/yyyy");
        String strDate = format.format(pupil.getBirthDate());
        pupilJson.setBirthDate(strDate);
        return pupilJson;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }
}
