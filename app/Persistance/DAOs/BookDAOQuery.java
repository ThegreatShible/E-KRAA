package Persistance.DAOs;

import Persistance.DatabaseExecutionContext;
import models.book.Answer;
import models.book.Book;
import models.book.Categorie;
import models.book.Questions;
import play.Logger;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class BookDAOQuery {

    private final static String bookInsertQuery = "INSERT INTO public.book(content, lastmodifdate, language, difficulty) VALUES (?, ?, ?, ?) Returning idbook";
    private final static String categorieInsertQuery = "INSERT INTO public.categorie(idbook, categorie) VALUES (?, ?)";
    private final static String questionInsertQuery = "INSERT INTO public.questions(idbook, content, multiplechoices)VALUES (?, ?, ?) Returning numquestion";
    private final static String answerInsertQuery = "INSERT INTO public.answer(idbook, idquestion, content, \"right\") VALUES (?, ?, ?, ?)";

    private JPAApi jpaApi;
    private DatabaseExecutionContext databaseExecutionContext;

    @Inject
    public BookDAOQuery(JPAApi jpaApi, DatabaseExecutionContext databaseExecutionContext) {
        this.jpaApi = jpaApi;
        this.databaseExecutionContext = databaseExecutionContext;
    }

    public CompletableFuture<Void> create(Book book) {
        return CompletableFuture.runAsync(() -> {
            jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                Logger.info("query0");
                Query q1 = em.createNativeQuery(bookInsertQuery);
                int id = (int) q1.setParameter(1, book.getContent()).setParameter(2, new Date(), TemporalType.DATE).
                        setParameter(3, book.getLanguage()).setParameter(4, book.getDifficulty()).getSingleResult();
                Logger.info("query 1");

                // int id = (int)em.createNativeQuery("INSERT INTO public.book(content, lastmodifdate, language, difficulty) VALUES ("+book.getContent()+",22-09-1995, "+book.getLanguage()+", "+book.getDifficulty()+") Returning idbook").getSingleResult();
                Logger.info("query 2");
                for (Categorie cat : book.getCategorieList()) {

                    Query q2 = em.createNativeQuery(categorieInsertQuery).setParameter(1, id).setParameter(2, cat.getCategoriePK().getCategorie());
                    q2.executeUpdate();
                    //Query q2 = em.createNativeQuery("INSERT INTO public.categorie(idbook, categorie) VALUES ("+id+", "+cat.getCategoriePK().getCategorie()+");");
                }
                Logger.info("query3");
                for (Questions qs : book.getQuestionsList()) {
                    Query q3 = em.createNativeQuery(questionInsertQuery).setParameter(1, id).setParameter(2, qs.getContent()).setParameter(3, qs.getMultiplechoices());
                    short idq = (short) q3.getSingleResult();
                    //int idq = (int)em.createNativeQuery("INSERT INTO public.questions(idbook, content, multiplechoices)VALUES ("+id+", "+qs.getContent()+", "+qs.getMultiplechoices()+") Returning numquestion;").getSingleResult();
                    Logger.info("query 4");
                    for (Answer ans : qs.getAnswerList()) {
                        Query q4 = em.createNativeQuery(answerInsertQuery).setParameter(1, id).setParameter(2, idq).setParameter(3, ans.getContent()).setParameter(4, ans.getRight());
                        q4.executeUpdate();
                        //em.createNativeQuery("INSERT INTO public.answer(idbook, idquestion, content, \"right\") VALUES ("+id+", "+idq+", "+ans.getContent()+", "+ans.getRight()+");").executeUpdate();
                    }

                }
            });
        }, databaseExecutionContext);
    }
}

