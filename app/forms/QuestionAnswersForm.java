package forms;

import java.util.List;

public class QuestionAnswersForm {
    private int numQuestion;
    private List<Integer> answersNum;

    public int getNumQuestion() {
        return numQuestion;
    }

    public void setNumQuestion(int numQuestion) {
        this.numQuestion = numQuestion;
    }

    public List<Integer> getAnswersNum() {
        return answersNum;
    }

    public void setAnswersNum(List<Integer> answersNum) {
        this.answersNum = answersNum;
    }
}
