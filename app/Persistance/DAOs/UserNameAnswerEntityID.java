package Persistance.DAOs;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserNameAnswerEntityID implements Serializable {
    @Column
    private String userid;
    @Column
    private String sessionid;
    @Column
    private short numquestion;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public short getNumquestion() {
        return numquestion;
    }

    public void setNumquestion(short numquestion) {
        this.numquestion = numquestion;
    }
}
