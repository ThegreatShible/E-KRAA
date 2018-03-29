package forms;

import java.util.List;
//TODO : Check if list or collection when parsing Json

public class QuestionForm {
    private String question;
    private List<AnswerForm> answers;
    private boolean multiple;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<AnswerForm> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerForm> answers) {
        this.answers = answers;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }
}
