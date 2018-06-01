package forms;

import java.util.List;

public class QuestionsForm {
    private int bookID;
    private List<QuestionForm> questions;

    public QuestionsForm() {
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public List<QuestionForm> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionForm> questions) {
        this.questions = questions;
    }
}
