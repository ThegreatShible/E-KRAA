package models.book;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserAnswer {

    private long idBook;
    private Map<Short, List<Short>> questionsAnswers;
    private long answerTime;

    public UserAnswer(long idBook, Map<Short, List<Short>> questionsAnswers, long answerTime) {
        this.idBook = idBook;
        this.questionsAnswers = questionsAnswers;
        this.answerTime = answerTime;
    }

    //TODO : Implementation of idanswer
    public static UserAnswer create(long idBook, Map<Short, List<Short>> questionsAnswers, List<Question> questions) throws UserAnswerCreationException {
        final long currentTime = System.currentTimeMillis();
        Set<Short> set = new HashSet<>();
        for (Question question : questions) {
            set.add(question.getQuestionNum());
            final short qid = question.getQuestionNum();
            List<Short> answers = questionsAnswers.get(qid);
            if (answers.isEmpty())
                throw new UserAnswerCreationException("la question numero " + qid + "ne contient pas de reponse");
            if (!question.isMultipleChoice() && answers.size() > 1)
                throw new UserAnswerCreationException("Question a choix unique contient des reponses multiples");

            Set<Answer> answerSet = new HashSet(question.getAnswers());
            Set<Short> answerIDSet = new HashSet<>();
            for (Answer a : answerSet) {
                answerIDSet.add(a.getNumAnswer());
            }
            if (!answerIDSet.equals(new HashSet(answers)))
                throw new UserAnswerCreationException("Les reponse a une question ne sont pas conformes");

        }
        if (!set.equals(questionsAnswers.keySet()))
            throw new UserAnswerCreationException("les questions ne sont pas tous presentes");

        return new UserAnswer(idBook, questionsAnswers, currentTime);
    }

    public long getIdBook() {
        return idBook;
    }

    public void setIdBook(long idBook) {
        this.idBook = idBook;
    }

    public Map<Short, List<Short>> getQuestionsAnswers() {
        return questionsAnswers;
    }

    public void setQuestionsAnswers(Map<Short, List<Short>> questionsAnswers) {
        this.questionsAnswers = questionsAnswers;
    }

    public long getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(long answerTime) {
        this.answerTime = answerTime;
    }
}
