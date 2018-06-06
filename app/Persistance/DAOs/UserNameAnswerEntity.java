package Persistance.DAOs;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class UserNameAnswerEntity {


    @EmbeddedId
    private UserNameAnswerEntityID userNameAnswerEntityID = new UserNameAnswerEntityID();
    @Column
    private short numanswer;
    @Column
    private long answerdate;




    public short getNumquestion() {
        return userNameAnswerEntityID.getNumquestion();
    }

    public void setNumquestion(short numquestion) {
        this.userNameAnswerEntityID.setNumquestion(numquestion);
    }

    public short getNumanswer() {
        return numanswer;
    }

    public void setNumanswer(short numanswer) {
        this.numanswer = numanswer;
    }

    public long getAnswerdate() {
        return answerdate;
    }

    public void setAnswerdate(long answerdate) {
        this.answerdate = answerdate;
    }

    public String getSessionid() {
        return userNameAnswerEntityID.getSessionid();
    }

    public void setSessionid(String sessionid) {
        this.userNameAnswerEntityID.setSessionid(sessionid);
    }

    public String getUserid() {
        return userNameAnswerEntityID.getUserid();
    }

    public void setUserid(String userid) {
        this.userNameAnswerEntityID.setUserid(userid);
    }

}
