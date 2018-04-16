package forms;

import java.util.List;
import java.util.Map;

public class UserAnswerForm {

    private Long idBook;
    private Map<Short, List<Short>> qustionsAnswers;

    public Long getIdBook() {
        return idBook;
    }

    public void setIdBook(Long idBook) {
        this.idBook = idBook;
    }

    public Map<Short, List<Short>> getQustionsAnswers() {
        return qustionsAnswers;
    }

    public void setQustionsAnswers(Map<Short, List<Short>> qustionsAnswers) {
        this.qustionsAnswers = qustionsAnswers;
    }
}
