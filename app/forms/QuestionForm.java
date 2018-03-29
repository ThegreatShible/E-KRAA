package forms;

import models.book.Answer;
import models.book.BookCreationException;
import models.book.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionForm {
    private short questionNum;
    private String question;
    private List<AnswerForm> answers;
    private boolean multiple;
    private short weight;

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

    public short getWeight() {
        return weight;
    }

    public void setWeight(short weight) {
        this.weight = weight;
    }

    public short getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(short questionNum) {
        this.questionNum = questionNum;
    }

    public Question toQuesiton() throws BookCreationException {
        List<Answer> answerList = new ArrayList<>();
        for (AnswerForm af : answers) {
            answerList.add(af.toAnswer());
        }
        return Question.create(questionNum, question, multiple, weight, answerList);
    }
}
