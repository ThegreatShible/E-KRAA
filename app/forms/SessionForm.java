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

    @Formats.DateTime(pattern = "dd-MM-YYYY hh:mm:ss")
    private Date sessionStart;
    private int hours;
    private int minutes;
    private int bookID;
    private int groupID;

    public Date getSessionStart() {
        return sessionStart;
    }

    public void setSessionStart(Date sessionStart) {
        this.sessionStart = sessionStart;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
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

    public Session toSession(UUID sessionID) {
        LocalDate localDate = LocalDate.of(sessionStart.getYear(), sessionStart.getMonth(), sessionStart.getDay());
        LocalTime localTime = LocalTime.of(sessionStart.getHours(), sessionStart.getMinutes());
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        Duration duration = Duration.ofMinutes(60 * hours + minutes);
        Session session = new Session(sessionID, localDateTime, duration, bookID, groupID);
        return session;
    }
}
