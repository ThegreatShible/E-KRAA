package Persistance.DAOs;

public class UserNameAnswerEntity {

    private int ibook;
    private short numquesion;
    private short numanswer;
    private int idAnwser;
    private long answerdate;

    public int getIbook() {
        return ibook;
    }

    public void setIbook(int ibook) {
        this.ibook = ibook;
    }

    public short getNumquesion() {
        return numquesion;
    }

    public void setNumquesion(short numquesion) {
        this.numquesion = numquesion;
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
