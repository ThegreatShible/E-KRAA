package models.book;

public class Answer {
    private final short numAnswer;
    private String content;
    private boolean valid;

    public Answer(short numAnswer, String content, boolean valid) {
        this.numAnswer = numAnswer;
        this.content = content;
        this.valid = valid;
    }

    public short getNumAnswer() {
        return numAnswer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
