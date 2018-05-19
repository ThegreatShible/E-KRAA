package models.session;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;


public class Session {
    private final UUID sessionID;
    private final LocalDateTime startDate;
    private final Duration duration;
    private final int idBook;
    private final int groupID;


    public Session(UUID sessionID, LocalDateTime startDate, Duration duration, int idBook, int groupID) {
        this.sessionID = sessionID;
        this.startDate = startDate;
        this.duration = duration;
        this.idBook = idBook;
        this.groupID = groupID;
    }

    public static Session create(LocalDateTime startDate, Duration duration, int idBook, int groupID) throws SessionCreationException {
        UUID sessionID = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        if (startDate.isBefore(now)) throw new SessionCreationException();
        else return new Session(sessionID, startDate, duration, idBook, groupID);
    }

    public UUID getSessionID() {
        return sessionID;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public Duration getDuration() {
        return duration;
    }

    public int getIdBook() {
        return idBook;
    }

    public Boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return startDate.isBefore(now) && startDate.plus(duration).isAfter(now);
    }

    public int getGroupID() {
        return groupID;
    }
}
