package models.session;

public class SessionCreationException extends Exception {
    public SessionCreationException() {
        super();
    }

    public SessionCreationException(String message) {
        super(message);
    }
}
