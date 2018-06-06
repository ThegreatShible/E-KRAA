package forms.JsonHelpers;

import models.users.Pupil;

import java.util.ArrayList;
import java.util.List;

public class PupilJsonList {
    private List<PupilJson> pupils;

    public static PupilJsonList fromPupilList(List<Pupil> pupilList) {
        PupilJsonList pupilJsonList = new PupilJsonList();
        List<PupilJson> pupilJsonList1 = new ArrayList<>();
        for (Pupil pupil : pupilList) {
            PupilJson pupilJson = PupilJson.fromPupil(pupil);
            pupilJsonList1.add(pupilJson);
        }
        pupilJsonList.setPupils(pupilJsonList1);
        return pupilJsonList;
    }

    public List<PupilJson> getPupils() {
        return pupils;
    }

    public void setPupils(List<PupilJson> pupils) {
        this.pupils = pupils;
    }
}
