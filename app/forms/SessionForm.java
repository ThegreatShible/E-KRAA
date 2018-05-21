package forms;

import models.session.Session;
import play.data.format.Formats;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

public class SessionForm {

    @Formats.DateTime(pattern = "dd-MM-YYYY hh:mm")
    private Date sessionStart;
    @Formats.DateTime(pattern = "dd-MM-YYYY hh:mm")
    private Date sessionEnd;
    private int bookID;
    private int groupID;

    public Date getSessionStart() {
        return sessionStart;
    }

    public void setSessionStart(Date sessionStart) {
        this.sessionStart = sessionStart;
    }


    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public Date getSessionEnd() {
        return sessionEnd;
    }

    public void setSessionEnd(Date sessionEnd) {
        this.sessionEnd = sessionEnd;
    }

    public Session toSession(UUID sessionID) {
        LocalDate localDate = LocalDate.of(sessionStart.getYear(), sessionStart.getMonth(), sessionStart.getDay());
        LocalTime localTime = LocalTime.of(sessionStart.getHours(), sessionStart.getMinutes());
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        long date1 = sessionStart.toInstant().toEpochMilli();
        long date2 = sessionEnd.toInstant().toEpochMilli();
        Duration duration = Duration.ofSeconds(date2 - date1);
        Session session = new Session(sessionID, localDateTime, duration, bookID, groupID);
        return session;
    }
}
