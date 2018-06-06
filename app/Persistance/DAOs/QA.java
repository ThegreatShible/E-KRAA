package Persistance.DAOs;

public class QA {
    private short quesion;
    private short answer;

    public QA(short quesion, short answer) {
        this.quesion = quesion;
        this.answer = answer;
    }

    public short getQuesion() {
        return quesion;
    }

    public void setQuesion(short quesion) {
        this.quesion = quesion;
    }

    public short getAnswer() {
        return answer;
    }

    public void setAnswer(short answer) {
        this.answer = answer;
    }
}
