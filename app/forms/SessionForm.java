package forms;

import models.session.Session;
import play.data.format.Formats;

import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class SessionForm {

    @Formats.DateTime(pattern = "yyyy/MM/dd mm:ss")
    private Date sessionStart;
    @Formats.DateTime(pattern = "yyyy/MM/dd mm:ss")
    private Date sessionEnd;
    private int book;
    private int Group;

    public Date getSessionStart() {
        return sessionStart;
    }

    public void setSessionStart(Date sessionStart) {
        this.sessionStart = sessionStart;
    }


    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }

    public int getGroup() {
        return Group;
    }

    public void setGroup(int group) {
        Group = group;
    }

    public Date getSessionEnd() {
        return sessionEnd;
    }

    public void setSessionEnd(Date sessionEnd) {
        this.sessionEnd = sessionEnd;
    }

    public Session toSession(UUID sessionID) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(sessionStart);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(sessionEnd);
        LocalDate localDate1 = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        LocalTime localTime1 = LocalTime.of(cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
        LocalDateTime localDateTime1 = LocalDateTime.of(localDate1, localTime1);
        LocalDate localDate2 = LocalDate.of(cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH) + 1, cal2.get(Calendar.DAY_OF_MONTH));
        LocalTime localTime2 = LocalTime.of(cal2.get(Calendar.MINUTE), cal2.get(Calendar.SECOND));
        LocalDateTime localDateTime2 = LocalDateTime.of(localDate2, localTime2);


        Duration duration = Duration.between(localDateTime1, localDateTime2);
        System.out.println("DURATION : : : "+ duration);
        Session session = new Session(sessionID, localDateTime1, duration, book, Group);
        return session;
    }
}
