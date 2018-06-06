package Persistance.DAOs;

import Persistance.DatabaseExecutionContext;
import models.book.Book;
import models.book.UserAnswer;
import models.session.Session;
import models.users.ScoreLevel;
import play.db.jpa.JPAApi;
import scala.Option;
import scala.Some;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SessionDAO {

    //TODO : complete insert when session is present a revoir
    private final static String userAnswerInsert = "INSERT INTO public.useranswer(numquestion, numanswer, answerdate, sessionid, userid) VALUES (?, ?, ?, ?, ?) returning sessionid";
    private final static String getUserAnswerSelectQuery = "SELECT numquestion, numanswer, answerdate, session.sessionid, userid\n" +
            "\tFROM public.useranswer , session where\n" +
            "\t userid = ? and session.sessionid = ? AND  session.removed = FALSE";
    private final static String removeSessionQuery = "UPDATE public.session\n" +
            "\tSET  removed= TRUE\n" +
            "\tWHERE sessionid = ? ";
    private final static String getLevel = "select level from level join (select max(maxscore) as max from level where maxscore <= ?) as t on maxscore = max";
    private final static String getUserScore = "select score from public.\"user\" join auth on \"user\".userid = auth.userid where confirmed = TRUE and auth.userid = ? ";
    private final static String setUserScoreLevel = "UPDATE public.\"user\"\n" +
            "\tSET  score=?, level=?\n" +
            "\tWHERE userid  = ? returing score, level";
    private static String sessionCreationQuery = "INSERT INTO public.session(\n" +
            "\tsessionid, idbook, startdate, duration, removed, groupid)\n" +
            "\tVALUES (?, ?, ?, ?, FALSE, ?)";
    private static String selectSessionByIDTeacherQuery = "SELECT sessionid, idbook, startdate, duration, removed, session.groupid as group\n" +
            "\tFROM public.session,public.group where sessionid = ? and  removed = FALSE and owner = ?";
    private static String selectSessionByID = "SELECT sessionid, idbook, startdate, duration ,removed, session.groupid as group\n" +
            "\tFROM public.session,public.group where sessionid = ? and  removed = FALSE";
    private static String selectSessionsByTeacher = "SELECT sessionid, session.idbook as ibook, startdate, duration,session.removed as rem\n, groupid" +
            "\tFROM public.session join book on session.idbook = book.idbook\n" +
            "\twhere creator = ? and session.removed = FALSE";
    @Inject
    private JPAApi jpaApi;
    @Inject
    private DatabaseExecutionContext databaseExecutionContext;

    //TODO : Timezone Pronblem
    public CompletableFuture<Void> createSession(Session session) {
        return CompletableFuture.runAsync(() -> {
            jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                long beginTime = session.getStartDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                long duration = session.getDuration().toMillis();
                em.createNativeQuery(sessionCreationQuery)
                        .setParameter(1, session.getSessionID())
                        .setParameter(2, session.getIdBook())
                        .setParameter(3, beginTime)
                        .setParameter(4, duration)
                        .setParameter(5, session.getGroupID())
                        .executeUpdate();
            });
        }, databaseExecutionContext);
    }

    //TODO check date before removing
    //TODO include userID in the query
    public CompletableFuture<Void> removeSession(UUID sessionID, UUID userID) {
        return CompletableFuture.runAsync(() -> {
            jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                em.createNativeQuery(removeSessionQuery)
                        .setParameter(1, sessionID.toString())
                        //.setParameter(2, userID.toString())
                        .executeUpdate();
            });
        }, databaseExecutionContext);
    }


    public CompletableFuture<Option<Session>> getSessionById(UUID sessionID, UUID userID) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                List<Object[]> sessions = em.createNativeQuery(selectSessionByIDTeacherQuery)
                        .setParameter(1, sessionID.toString())
                        .setParameter(2, userID.toString()).getResultList();
                if (sessions.isEmpty())
                    return Option.empty();
                else {
                    Object[] session = sessions.get(0);
                    return Option.apply(getSessionFromRaw(session));
                }
            });
        }, databaseExecutionContext);
    }


    public CompletableFuture<Option<Session>> getSessionById(UUID sessionID) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                List<Object[]> sessions = em.createNativeQuery(selectSessionByID)
                        .setParameter(1, sessionID.toString())
                        .getResultList();
                if (sessions.isEmpty())
                    return Option.empty();
                else {
                    Object[] session = sessions.get(0);
                    return Option.apply(getSessionFromRaw(session));
                }
            });
        }, databaseExecutionContext);
    }


    public CompletableFuture<List<Session>> getSessionByTeacherID(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                List<Object[]> sessions = em.createNativeQuery(selectSessionsByTeacher)
                        .setParameter(1, id.toString() ).getResultList();
                List<Session> sessionList = new ArrayList<>();
                for (Object[] raw : sessions) {
                    sessionList.add(getSessionFromRaw(raw));
                }
                return sessionList;

            });
        }, databaseExecutionContext);
    }

    private Session getSessionFromRaw(Object[] raw) {
        UUID sessionID = UUID.fromString((String) raw[0]);
        int bookid = (int) raw[1];
        LocalDateTime startTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(((BigInteger) raw[2]).longValue()), ZoneId.systemDefault());
        Duration duration = Duration.ofSeconds(((BigInteger) raw[3]).longValue());
        int groupID = (int) raw[5];
        Session session = new Session(sessionID, startTime, duration, bookid, groupID);
        return session;
    }

    public CompletableFuture<ScoreLevel> createUserAnswer(Book book, UserAnswer userAnswer) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                String i = "";
                for (Map.Entry<Short, List<Short>> entry : userAnswer.getQuestionsAnswers().entrySet()) {
                    for (Short s : entry.getValue()) {
                        i = (String) em.createNativeQuery(userAnswerInsert).
                                setParameter(1, entry.getKey())
                                .setParameter(2, s)
                                .setParameter(3, userAnswer.getAnswerTime())
                                .setParameter(4, userAnswer.getIdSession())
                                .setParameter(5, userAnswer.getUser().toString()).getSingleResult();
                    }
                }
                int score = book.getScoreFromAnswer(userAnswer);
                int userScore = (Integer) em.createNativeQuery(getUserScore).setParameter(1,userAnswer.getUser().toString()).getSingleResult();
                int finalscore = score + userScore;
                int finalLevel = (Integer) em.createNativeQuery(getLevel).setParameter(1, finalscore).getSingleResult();
                em.createNativeQuery(setUserScoreLevel).setParameter(1, finalscore)
                        .setParameter(2, finalLevel).setParameter(3, userAnswer.getUser().toString())
                        .getResultList();

                return new ScoreLevel(finalscore, finalLevel);

            });
        }, databaseExecutionContext);
    }


    public CompletableFuture<Option<UserAnswer>> findUserAnswer(UUID sessionid, UUID userid) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                List<UserNameAnswerEntity> rawuserAnswer = em.createNativeQuery(getUserAnswerSelectQuery, UserNameAnswerEntity.class)
                        .setParameter(1, userid.toString()).setParameter(2, sessionid.toString())
                        .getResultList();
                if (rawuserAnswer.isEmpty())
                    return Option.empty();

                Stream<UserNameAnswerEntity> stream = rawuserAnswer.stream();
                Map<Short, List<QA>> map = stream.map(x -> (new QA(x.getNumquestion(), x.getNumanswer()))).collect(Collectors.groupingBy(QA::getQuesion));
                Map<Short, List<Short>> map2 = map.entrySet().stream().collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue().stream().map(x -> x.getAnswer()).collect(Collectors.toList())
                ));


                long answerdate = rawuserAnswer.get(0).getAnswerdate();

                return Some.apply(new UserAnswer(sessionid, userid, map2, answerdate));

            });
        }, databaseExecutionContext);
    }

    public CompletableFuture<Option<Session>> getByID(UUID sessionID) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                List<Object[]> sessions = em.createNativeQuery(selectSessionByID)
                        .setParameter(1, sessionID.toString())
                        .getResultList();
                if (sessions.isEmpty())
                    return Option.empty();
                else {
                    Object[] session = sessions.get(0);
                    return Option.apply(getSessionFromRaw(session));
                }
            });
        }, databaseExecutionContext);
    }
}
