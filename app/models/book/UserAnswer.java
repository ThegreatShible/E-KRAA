package models.book;

import models.session.Session;

import java.util.*;

public class UserAnswer {

    private UUID idSession;
    private UUID user;
    private Map<Short, List<Short>> questionsAnswers;
    private long answerTime;

    public UserAnswer(UUID idSession, UUID user, Map<Short, List<Short>> questionsAnswers, long answerTime) {
        this.user = user;
        this.idSession = idSession;
        this.questionsAnswers = questionsAnswers;
        this.answerTime = answerTime;
    }

    //TODO : Implementation of idanswer (j'ai pas compris celui la)
    public static UserAnswer create(Session session, UUID user, Map<Short, List<Short>> questionsAnswers, List<Question> questions) throws UserAnswerCreationException {
        if (!session.isActive())
            throw new UserAnswerCreationException("Session non Active");
        final long currentTime = System.currentTimeMillis();
        Set<Short> set = new HashSet<>();
        for (Question question : questions) {
            set.add(question.getQuestionNum());
            final short qid = question.getQuestionNum();

            //user answers (Short)
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

            //try

            if (!answerIDSet.containsAll(answers)) {
                for (short i : answerIDSet) {
                    System.out.println("hs  "+ i);

                }
                for (short i : answers) {
                    System.out.println("answers  "+ answers);

                }
                throw new UserAnswerCreationException("Les reponse a une question ne sont pas conformes");
            }
        }
        if (!set.equals(questionsAnswers.keySet()))
            throw new UserAnswerCreationException("les questions ne sont pas tous presentes");

        return new UserAnswer(session.getSessionID(), user, questionsAnswers, currentTime);
    }

    public UUID getIdSession() {
        return idSession;
    }

    public void setIdSession(UUID idSession) {
        this.idSession = idSession;
    }

    public UUID getUser() {
        return user;
    }

    public void setUser(UUID user) {
        this.user = user;
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
