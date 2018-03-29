package models.book;

import java.util.List;

public class Question {
    private final short questionNum;
    private String content;
    private boolean multipleChoice;
    private short weight;
    private List<Answer> answers;

    public Question(short questionNum, String content, boolean multipleChoice, short weight, List<Answer> answers) {
        this.questionNum = questionNum;
        this.content = content;
        this.multipleChoice = multipleChoice;
        this.weight = weight;
        this.answers = answers;
    }

    /**
     * must pass by this if the creation is done for the fist time (not from database)
     *
     * @param questionNum
     * @param content
     * @param multipleChoice
     * @param weight
     * @param answers
     * @return
     * @throws BookCreationException
     */

    public static Question create(short questionNum, String content, boolean multipleChoice, short weight, List<Answer> answers) throws BookCreationException {
        //TODO : Remove javascript from content
        boolean bool = false;
        int i = 0;
        for (Answer ans : answers) {
            if (ans.isValid()) {
                bool = true;
                i++;
            }
        }
        if (bool == false) throw new BookCreationException("aucune reponse ou aucune reponse juste");
        if (i > 1 && !multipleChoice)
            throw new BookCreationException("plusieurs reponse juste alors que le choix n'est pas multiple");
        return new Question(questionNum, content, multipleChoice, weight, answers);

    }

    public short getQuestionNum() {
        return questionNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isMultipleChoice() {
        return multipleChoice;
    }

    public void setMultipleChoice(boolean multipleChoice) {
        this.multipleChoice = multipleChoice;
    }

    public short getWeight() {
        return weight;
    }

    public void setWeight(short weight) {
        this.weight = weight;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}




