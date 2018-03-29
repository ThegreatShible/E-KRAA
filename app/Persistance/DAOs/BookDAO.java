package Persistance.DAOs;

import Persistance.DatabaseExecutionContext;
import models.book.Book;
import models.book.Categorie;
import models.book.Questions;
import play.Logger;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


//TODO : Checking if could be declared singleton
public class BookDAO implements CRUD_DAO<Book, Integer> {

    private DatabaseExecutionContext executionContext;
    private JPAApi jpaApi;

    @Inject
    public BookDAO(DatabaseExecutionContext executionContext, JPAApi jpaApi) {
        this.executionContext = executionContext;
        this.jpaApi = jpaApi;
    }


    @Override
    public CompletableFuture<Integer> create(final Book obj) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                /////////////////////////////////////////////////
                if (obj.getCategorieList() == null) {
                    obj.setCategorieList(new ArrayList<Categorie>());
                }
                if (obj.getQuestionsList() == null) {
                    obj.setQuestionsList(new ArrayList<Questions>());
                }
                List<Categorie> attachedCategorieList = new ArrayList<Categorie>();
                for (Categorie categoriesListCategorieToAttach : obj.getCategorieList()) {

                    categoriesListCategorieToAttach = em.getReference(categoriesListCategorieToAttach.getClass(), categoriesListCategorieToAttach.getCategoriePK());

                    attachedCategorieList.add(categoriesListCategorieToAttach);
                }
                obj.setCategorieList(attachedCategorieList);
                Logger.info("mileStone 1");

                List<Questions> attachedQuestionsList = new ArrayList<Questions>();
                for (Questions questionsListQuestionsToAttach : obj.getQuestionsList()) {

                    questionsListQuestionsToAttach = em.getReference(questionsListQuestionsToAttach.getClass(), questionsListQuestionsToAttach.getQuestionsPK());
                    attachedQuestionsList.add(questionsListQuestionsToAttach);
                }
                obj.setQuestionsList(attachedQuestionsList);
                em.persist(obj);
                Logger.info("mileStone 2");
                for (Categorie categoriesListCategorie : obj.getCategorieList()) {
                    Book oldBookOfCategorieListCategorie = categoriesListCategorie.getBook();
                    categoriesListCategorie.setBook(obj);
                    Logger.info("milestone 2.5");
                    categoriesListCategorie = em.merge(categoriesListCategorie);
                    if (oldBookOfCategorieListCategorie != null) {
                        oldBookOfCategorieListCategorie.getCategorieList().remove(categoriesListCategorie);
                        oldBookOfCategorieListCategorie = em.merge(oldBookOfCategorieListCategorie);
                    }
                }
                Logger.info("mileStone 3");

                for (Questions questionsListQuestions : obj.getQuestionsList()) {
                    Book oldBookOfQuestionsListQuestions = questionsListQuestions.getBook();
                    questionsListQuestions.setBook(obj);
                    questionsListQuestions = em.merge(questionsListQuestions);
                    if (oldBookOfQuestionsListQuestions != null) {
                        oldBookOfQuestionsListQuestions.getQuestionsList().remove(questionsListQuestions);
                        oldBookOfQuestionsListQuestions = em.merge(oldBookOfQuestionsListQuestions);
                    }
                }
                //TODO  : check if changed
                return obj.getIdbook();

                ///////////////////////////////////////////////
            });
        }, executionContext);
    }

    @Override
    public CompletableFuture<Void> edit(final Book obj) {
        CompletableFuture.runAsync(() -> {
            jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                ///////////////////////////////////////
                Book persistentBook = em.find(Book.class, obj.getIdbook());
                List<Categorie> categoriesListOld = persistentBook.getCategorieList();
                List<Categorie> categoriesListNew = obj.getCategorieList();
                List<Questions> questionsListOld = persistentBook.getQuestionsList();
                List<Questions> questionsListNew = obj.getQuestionsList();
                List<String> illegalOrphanMessages = null;
                for (Categorie categoriesListOldCategorie : categoriesListOld) {
                    if (!categoriesListNew.contains(categoriesListOldCategorie)) {
                        if (illegalOrphanMessages == null) {
                            illegalOrphanMessages = new ArrayList<String>();
                        }
                        illegalOrphanMessages.add("You must retain Categories " + categoriesListOldCategorie + " since its books field is not nullable.");
                    }
                }
                for (Questions questionsListOldQuestions : questionsListOld) {
                    if (!questionsListNew.contains(questionsListOldQuestions)) {
                        if (illegalOrphanMessages == null) {
                            illegalOrphanMessages = new ArrayList<String>();
                        }
                        illegalOrphanMessages.add("You must retain Questions " + questionsListOldQuestions + " since its books field is not nullable.");
                    }
                }
               /* if (illegalOrphanMessages != null) {
                    throw new IllegalOrphanException(illegalOrphanMessages);
                }*/
                List<Categorie> attachedCategorieListNew = new ArrayList<Categorie>();
                for (Categorie categoriesListNewCategorieToAttach : categoriesListNew) {
                    categoriesListNewCategorieToAttach = em.getReference(categoriesListNewCategorieToAttach.getClass(), categoriesListNewCategorieToAttach.getCategoriePK());
                    attachedCategorieListNew.add(categoriesListNewCategorieToAttach);
                }
                categoriesListNew = attachedCategorieListNew;
                obj.setCategorieList(categoriesListNew);
                List<Questions> attachedQuestionsListNew = new ArrayList<Questions>();
                for (Questions questionsListNewQuestionsToAttach : questionsListNew) {
                    questionsListNewQuestionsToAttach = em.getReference(questionsListNewQuestionsToAttach.getClass(), questionsListNewQuestionsToAttach.getQuestionsPK());
                    attachedQuestionsListNew.add(questionsListNewQuestionsToAttach);
                }
                questionsListNew = attachedQuestionsListNew;
                obj.setQuestionsList(questionsListNew);
                Book obj2 = em.merge(obj);
                for (Categorie categoriesListNewCategorie : categoriesListNew) {
                    if (!categoriesListOld.contains(categoriesListNewCategorie)) {
                        Book oldBookOfCategorieListNewCategorie = categoriesListNewCategorie.getBook();
                        categoriesListNewCategorie.setBook(obj2);
                        categoriesListNewCategorie = em.merge(categoriesListNewCategorie);
                        if (oldBookOfCategorieListNewCategorie != null && !oldBookOfCategorieListNewCategorie.equals(obj2)) {
                            oldBookOfCategorieListNewCategorie.getCategorieList().remove(categoriesListNewCategorie);
                            oldBookOfCategorieListNewCategorie = em.merge(oldBookOfCategorieListNewCategorie);
                        }
                    }
                }
                for (Questions questionsListNewQuestions : questionsListNew) {
                    if (!questionsListOld.contains(questionsListNewQuestions)) {
                        Book oldBookOfQuestionsListNewQuestions = questionsListNewQuestions.getBook();
                        questionsListNewQuestions.setBook(obj2);
                        questionsListNewQuestions = em.merge(questionsListNewQuestions);
                        if (oldBookOfQuestionsListNewQuestions != null && !oldBookOfQuestionsListNewQuestions.equals(obj2)) {
                            oldBookOfQuestionsListNewQuestions.getQuestionsList().remove(questionsListNewQuestions);
                            oldBookOfQuestionsListNewQuestions = em.merge(oldBookOfQuestionsListNewQuestions);
                        }
                    }
                }
                //////////////////////////////////////
            });
        }, executionContext);
        return null;
    }

    @Override
    public CompletableFuture<Void> destroy(Integer key) {
        CompletableFuture.runAsync(() -> {
            jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                ///////////////////////////////
                Book books;
                books = em.getReference(Book.class, key);
                books.getIdbook();

                List<String> illegalOrphanMessages = null;
                List<Categorie> categoriesListOrphanCheck = books.getCategorieList();
                for (Categorie categoriesListOrphanCheckCategorie : categoriesListOrphanCheck) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("This Books (" + books + ") cannot be destroyed since the Categories " + categoriesListOrphanCheckCategorie + " in its categoriesList field has a non-nullable books field.");
                }
                List<Questions> questionsListOrphanCheck = books.getQuestionsList();
                for (Questions questionsListOrphanCheckQuestions : questionsListOrphanCheck) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("This Books (" + books + ") cannot be destroyed since the Questions " + questionsListOrphanCheckQuestions + " in its questionsList field has a non-nullable books field.");
                }
                /*if (illegalOrphanMessages != null) {
                    throw new IllegalOrphanException(illegalOrphanMessages);
                }*/
                em.remove(books);

                ///////////////////////////////
            });
        }, executionContext);
        return null;
    }

    @Override
    public CompletableFuture<Book> find(Integer key) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                return em.find(Book.class, key);
            });
        }, executionContext);

    }

    @Override
    public CompletableFuture<Integer> count() {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
                Root<Book> rt = cq.from(Book.class);
                cq.select(em.getCriteriaBuilder().count(rt));
                Query q = em.createQuery(cq);
                return ((Integer) q.getSingleResult()).intValue();
            });
        }, executionContext);
    }

    @Override
    public CompletableFuture<List<Book>> findAll() {
        return find(true, -1, -1);
    }

    @Override
    public CompletableFuture<List<Book>> find(int max, int first) {
        return find(false, max, first);
    }


    private CompletableFuture<List<Book>> find(boolean all, int max, int first) {
        return CompletableFuture.supplyAsync(() -> {
            return jpaApi.withTransaction(() -> {
                EntityManager em = jpaApi.em();
                CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
                cq.select(cq.from(Book.class));
                Query q = em.createQuery(cq);
                if (!all) {
                    q.setMaxResults(max);
                    q.setFirstResult(first);
                }
                return q.getResultList();
            });
        }, executionContext);
    }
}