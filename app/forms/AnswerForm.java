package forms;

import models.book.Answer;

public class AnswerForm {
    private String answer;
    private boolean right;
    private short numAnswer;

    public String getAnswer() {

        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public short getNumAnswer() {
        return numAnswer;
    }

    public void setNumAnswer(short numAnswer) {
        this.numAnswer = numAnswer;
    }

    public Answer toAnswer() {
        return new Answer(numAnswer, answer, right);
    }
}
