package Persistance.DAOs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserNameAnswerEntity {

    @Id
    private int idbook;
    @Column
    private short numquestion;
    @Column
    private short numanswer;
    @Column(name = "idanswer")
    private int idAnwser;
    @Column
    private long answerdate;

    public int getIdbook() {
        return idbook;
    }

    public void setIdbook(int ibook) {
        this.idbook = ibook;
    }

    public short getNumquestion() {
        return numquestion;
    }

    public void setNumquestion(short numquesion) {
        this.numquestion = numquesion;
    }

    public short getNumanswer() {
        return numanswer;
    }

    public void setNumanswer(short numanswer) {
        this.numanswer = numanswer;
    }

    public int getIdAnwser() {
        return idAnwser;
    }

    public void setIdAnwser(int idAnwser) {
        this.idAnwser = idAnwser;
    }

    public long getAnswerdate() {
        return answerdate;
    }

    public void setAnswerdate(long answerdate) {
        this.answerdate = answerdate;
    }
}
