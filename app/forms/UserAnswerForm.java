package forms;

import java.util.List;

public class UserAnswerForm {

    private String sessionID;
    private String userID;
    private List<QuestionAnswersForm> questionsAnswers;


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSessionID() {
        return sessionID;
    }


    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public List<QuestionAnswersForm> getQuestionsAnswers() {
        return questionsAnswers;
    }

    public void setQuestionsAnswers(List<QuestionAnswersForm> questionsAnswers) {
        this.questionsAnswers = questionsAnswers;
    }
}
