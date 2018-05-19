package forms;

import java.util.List;
import java.util.Map;

public class UserAnswerForm {

    private String sessionID;
    private Map<Short, List<Short>> qustionsAnswers;

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public Map<Short, List<Short>> getQustionsAnswers() {
        return qustionsAnswers;
    }

    public void setQustionsAnswers(Map<Short, List<Short>> qustionsAnswers) {
        this.qustionsAnswers = qustionsAnswers;
    }
}
