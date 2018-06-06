package forms.JsonHelpers;

import models.book.Book;
import models.session.Session;

import java.util.ArrayList;
import java.util.List;

public class SessionJsonList {
    private List<SessionJson> sessionJsonList;

    public List<SessionJson> getSessionJsonList() {
        return sessionJsonList;
    }

    public void setSessionJsonList(List<SessionJson> sessionJsonList) {
        this.sessionJsonList = sessionJsonList;
    }

    public static SessionJsonList fromSessionList(List<Session> sessions) {
        List<SessionJson> sessionJsons = new ArrayList<>();
        for (Session session : sessions) {
            sessionJsons.add(SessionJson.fromSession(session));
        }
        SessionJsonList sessionJsonList = new SessionJsonList();
        sessionJsonList.setSessionJsonList(sessionJsons);
        return sessionJsonList;
    }
}
