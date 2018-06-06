package Persistance.DAOs;

import Persistance.DatabaseExecutionContext;
import models.book.*;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BookRepository {


    //Insertion Queries
    private final static String bookInsertQuery = "INSERT INTO public.book(content, lastmodifdate, language, difficulty,removed, title, creator) VALUES (?, ?, ?, ?, FALSE ,?, ?) Returning idbook";
    private final static String questionInsertQuery = "INSERT INTO public.questions(idbook,numquestion, content, multiplechoices, weight)VALUES (?, ?, ?, ?, ?) Returning numquestion";
    private final static String answerInsertQuery = "INSERT INTO public.answer(idbook, idquestion, idanswer, content, \"right\") VALUES (?, ?, ?,  ?, ?)";


    //Selection Queries with condition
    private final static String bookSelectQuery = "SELECT * FROM public.book WHERE idbook = ? AND removed = FALSE";
    private final static String categorieSelectQuery = "SELECT  categorie.idcategorie, categorie\n" +
            "\tFROM public.bookcategorie, public.categorie where idbook = ?";
    private final static String allCategories = "Select * from categorie";
    private final static String questionSelectQuery = "SELECT * FROM public.questions WHERE idbook = ?";
    private final static String answerSelectQuery = "SELECT * FROM public.answer WHERE idbook = ? AND idquestion = ?";
    private final static String setBookCategoriesQuery = "INSERT INTO public.bookcategorie(idbook, idcategorie) VALUES (?, ?)";
    private final static String allBooks = "SELECT * from book where creator = ?";
    //getting quesitonList from a book
    private final static String questionListQuery = "SELECT * FROM public.questions where idbook = ?";

    //Removing a book
    private final static String bookRemoveQuery = "update book set removed = ?";


    private JPAApi jpaApi;
    private DatabaseExecutionContext databaseExecutionContext;

    @Inject
    public BookRepository(JPAApi jpaApi, DatabaseExecutionContext databaseExecutionContext) {
        this.jpaApi = jpaApi;
        this.databaseExecutionContext = databaseExecutionContext;
    }



    public CompletableFuture<Integer> create(Book obj) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                Query q1 = em.createNativeQuery(bookInsertQuery);
                int inserted = (int) q1
                        .setParameter(1, obj.getContent())
                        .setParameter(2, new Date(), TemporalType.TIMESTAMP)
                        .setParameter(3, obj.getLanguage().getLanguage())
                        .setParameter(4, obj.getDifficulty().name())
                        .setParameter(5, obj.getTitle())
                        .setParameter(6, obj.getCreator().toString()).getSingleResult();
                for (Integer cat : obj.getCategories()) {
                    em.createNativeQuery(setBookCategoriesQuery).setParameter(1, inserted)
                            .setParameter(2, cat).executeUpdate();
                }
                return inserted;

            });
        }, databaseExecutionContext);
    }

    public CompletableFuture<Integer> addQuestionsToBook(int bookID, List<Question> questions) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                for (Question qs : questions) {
                    Query q3 = em.createNativeQuery(questionInsertQuery).setParameter(1, bookID)
                            .setParameter(2, qs.getQuestionNum())
                            .setParameter(3, qs.getContent())
                            .setParameter(4, qs.isMultipleChoice())
                            .setParameter(5, qs.getWeight());
                    short idq = (short) q3.getSingleResult();

                    for (Answer ans : qs.getAnswers()) {
                        Query q4 = em.createNativeQuery(answerInsertQuery)
                                .setParameter(1, bookID)
                                .setParameter(2, idq)
                                .setParameter(3, ans.getNumAnswer())
                                .setParameter(4, ans.getContent())
                                .setParameter(5, ans.isValid());
                        q4.executeUpdate();
                    }

                }
                return bookID;
            });
        }, databaseExecutionContext);
    }


    public CompletableFuture<Void> edit(Book obj) {
        return null;
    }

    public CompletableFuture<Void> destroy(Integer key) {
        return CompletableFuture.runAsync(() -> {
            jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                em.createNativeQuery(bookRemoveQuery).setParameter(1, true).executeUpdate();
            });
        }, databaseExecutionContext);
    }


    public CompletableFuture<Book> find(Integer key) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                List<Object[]> rawCategories = em.createNativeQuery(categorieSelectQuery)
                        .setParameter(1, key).getResultList();
                List<Object[]> rawQuestions = em.createNativeQuery(questionSelectQuery).setParameter(1, key).getResultList();
                List<Question> questions = new ArrayList<>();
                for (Object[] raw : rawQuestions) {
                    short idq = (short) raw[1];
                    List<Object[]> rawAnswers = em.createNativeQuery(answerSelectQuery).setParameter(1, key).setParameter(2, idq).getResultList();
                    List<Answer> answers = answersConverter(rawAnswers);
                    Question question = questionConverter(raw, answers);

                    questions.add(question);
                }
                List<Category> categories = categorieConverter(rawCategories);
                List<Integer> catIDs = new ArrayList<>();
                for (Category cat : categories) {
                    catIDs.add(cat.getCategoryID());
                }
                List<Object[]> bookList = em.createNativeQuery(bookSelectQuery).setParameter(1, key.longValue()).getResultList();
                Object[] book = bookList.get(0);
                return bookConverter(book, catIDs, questions);


            });
        }, databaseExecutionContext);
    }

    public CompletableFuture<Integer> count() {
        return null;
    }


    public CompletableFuture<List<Book>> findAll(UUID creator) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                List<Object[]> raws = jpaApi.em().createNativeQuery(allBooks)
                        .setParameter(1, creator.toString()).getResultList();
                List<Book> books = new ArrayList<>();
                for (Object[] raw : raws) {
                    books.add(bookConverter(raw, null, null));
                }
                return books;
            });
        }, databaseExecutionContext);
    }

    public CompletableFuture<List<Book>> find(int max, int first) {
        return null;
    }

    public CompletableFuture<List<Question>> getQuestionFromBook(long bookId) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                List<Question> questions = new ArrayList<>();
                List<Object[]> rawQuestion = em.createNativeQuery(questionListQuery).setParameter(1, bookId).getResultList();
                for (Object[] raw : rawQuestion) {
                    List<Object[]> rawAnswers = em.createNativeQuery(answerSelectQuery).setParameter(1, raw[0]).setParameter(2, raw[1]).getResultList();
                    List<Answer> answers = answersConverter(rawAnswers);
                    Question question = questionConverter(raw, answers);
                    questions.add(question);
                }
                return questions;
            });
        }, databaseExecutionContext);

    }


    public CompletableFuture<List<Category>> getCategories() {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                List<Object[]> raws = jpaApi.em().createNativeQuery(allCategories).getResultList();
                return categorieConverter(raws);
            });
        }, databaseExecutionContext);
    }


    private List<Category> categorieConverter(List<Object[]> raws) {
        List<Category> categories = new ArrayList<>();
        for (Object[] raw : raws) {
            Category category = new Category();
            category.setCategoryID((int) raw[0]);
            category.setCategorieName((String) raw[1]);
            categories.add(category);
        }
        return categories;
    }

    private List<Answer> answersConverter(List<Object[]> raws) {
        List<Answer> answers = new ArrayList<>();
        for (Object[] raw : raws) {
            short numAnswer = (short) raw[2];
            String content = (String) raw[3];
            boolean valid = (boolean) raw[4];
            answers.add(new Answer(numAnswer, content, valid));
        }
        return answers;
    }

    private Question questionConverter(Object[] raw, List<Answer> answers) {

        short numQuestion = (short) raw[1];
        String content = (String) raw[2];
        boolean multipleChoices = (boolean) raw[3];
        short weight = (short) raw[4];
        Question question = new Question(numQuestion, content, multipleChoices, weight, answers);
        return question;
    }

    private Book bookConverter(Object[] raws, List<Integer> categories, List<Question> questions) {
        int idBook = (int) raws[0];
        String content = (String) raws[1];
        Date lastModifDate = (Date) raws[2];
        Language language = null;
        try {
            language = Language.String2Language((String) raws[3]);
        } catch (BookCreationException bce) {
            bce.printStackTrace();
        }
        Difficulty difficulty = Difficulty.valueOf((String) raws[4]);
        String title = (String) raws[6];
        UUID creator = UUID.fromString((String) raws[7]);
        return new Book(idBook, title, content, language, lastModifDate, difficulty, questions, categories, creator);
    }
}
