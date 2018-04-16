package Persistance.DAOs;

import Persistance.DatabaseExecutionContext;
import models.book.UserAnswer;
import play.db.jpa.JPAApi;
import scala.Option;
import scala.Some;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static play.libs.Scala.None;

public class UserAnswerDAO {

    private final static String userAnswerInsert = "INSERT INTO public.useranswer(idbook, numquestion, numanswer, answerdate) VALUES ( ?, ?, ?, ?) RETURNING idbook";
    private final static String getUserAnswerSelectQuery = "Select * from useranswer";

    private JPAApi jpaApi;
    private DatabaseExecutionContext databaseExecutionContext;

    @Inject
    public UserAnswerDAO(JPAApi jpaApi, DatabaseExecutionContext databaseExecutionContext) {
        this.jpaApi = jpaApi;
        this.databaseExecutionContext = databaseExecutionContext;
    }

    public CompletableFuture<Integer> create(UserAnswer userAnswer) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                int i = 0;
                for (Map.Entry<Short, List<Short>> entry : userAnswer.getQuestionsAnswers().entrySet()) {
                    for (Short s : entry.getValue()) {
                        i = (Integer) em.createNativeQuery(userAnswerInsert).setParameter(1, userAnswer.getIdBook()).
                                setParameter(2, entry.getKey()).setParameter(3, s).setParameter(4, userAnswer.getAnswerTime()).getSingleResult();
                    }
                }
                return i;

            });
        }, databaseExecutionContext);
    }


    public CompletableFuture<Option<UserAnswer>> find(Long idBook) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                List<UserNameAnswerEntity> rawuserAnswer = em.createNativeQuery(getUserAnswerSelectQuery, UserNameAnswerEntity.class).getResultList();
                if (rawuserAnswer.isEmpty())
                    return None();

                Stream<UserNameAnswerEntity> stream = rawuserAnswer.stream();
                Map<Short, List<QA>> map = stream.map(x -> (new QA(x.getNumquestion(), x.getNumanswer()))).collect(Collectors.groupingBy(QA::getQuesion));
                Map<Short, List<Short>> map2 = map.entrySet().stream().collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue().stream().map(x -> x.getAnswer()).collect(Collectors.toList())
                ));

                int book = rawuserAnswer.get(0).getIdbook();
                int idAnswer = rawuserAnswer.get(0).getIdAnwser();
                long answerdate = rawuserAnswer.get(0).getAnswerdate();

                return Some.apply(new UserAnswer(book, map2, answerdate));

            });
        }, databaseExecutionContext);
    }
}
