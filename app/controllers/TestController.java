package controllers;


import Persistance.DAOs.TestDAO2;
import com.fasterxml.jackson.databind.JsonNode;
import forms.AnswerForm;
import forms.BookForm;
import forms.QuestionForm;
import models.TestEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class TestController extends Controller {


    private TestDAO2 testDAO2;

    private MessagesApi messagesApi;

    @Inject
    public TestController(MessagesApi messagesApi, TestDAO2 testDAO2) {

        this.messagesApi = messagesApi;

        this.testDAO2 = testDAO2;
    }

    public Result test() {
        String str = testDAO2.test();
        return ok(str);

    }

    /*public CompletionStage<Result> authenticate() {
        Form<UserForm> authForm = formFactory.form(UserForm.class).bindFromRequest();
        if(authForm.hasErrors())
            return CompletableFuture.supplyAsync(() -> badRequest());
        else{
            UserForm userForm = authForm.get();
            String email = userForm.getEmail();
            CompletionStage<Optional<User>> userFuture = userDAO.getUser(email);
            return userFuture.thenApply(user -> ok(user.toString()));
            //TODO : remplacer par la page d'acceuil
            //TODO: check if the DAO's return type is compatible with hibernate

        }
    }*/

    @BodyParser.Of(BodyParser.Json.class)
    public Result test2() {

        JsonNode jsonNode = request().body().asJson();
        Person p = Json.fromJson(jsonNode, Person.class);
        String rejson = Json.toJson(p).toString();
        return ok(rejson);

    }

    public Result test3(String name) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        TestEntity te = new TestEntity();
        te.setName(name);
        session.save(te);
        session.getTransaction().commit();
        return ok("done");
    }

    public Result createBookForm() {
        BookForm bookForm = new BookForm();
        List<QuestionForm> questionFormList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            QuestionForm questionForm = new QuestionForm();
            List<AnswerForm> answerFormList = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                AnswerForm answerForm = new AnswerForm();
                answerForm.setAnswer("bla" + j);
                if (j == 1)
                    answerForm.setRight(true);
                else answerForm.setRight(false);
                answerFormList.add(answerForm);
            }
            questionForm.setAnswers(answerFormList);
            questionForm.setQuestion("ques" + i);
            questionForm.setMultiple(false);
            questionFormList.add(questionForm);
        }
        List<String> categories = new ArrayList<>();
        categories.add("sport");
        categories.add("music");
        bookForm.setCategories(categories);
        bookForm.setContent("some content such as json or html in this case");
        bookForm.setDifficulty("Easy");
        bookForm.setLanguage("fr");
        bookForm.setTitle("customTitle");
        bookForm.setQuestions(questionFormList);

        String bookJson = Json.toJson(bookForm).toString();

        return ok(bookJson);


    }

    class Person {
        protected String name;
        protected List<String> childrenNames = new ArrayList<>();


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getChildrenNames() {
            return childrenNames;
        }

        public void setChildrenNames(List<String> childrenNames) {
            this.childrenNames = childrenNames;
        }

        public void addChild(String child) {
            childrenNames.add(child);
        }
    }






}
