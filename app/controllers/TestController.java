package controllers;


import Persistance.DAOs.BookRepository;
import com.fasterxml.jackson.databind.JsonNode;
import forms.AnswerForm;
import forms.QuestionForm;
import forms.QuestionsForm;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.libs.mailer.MailerClient;
import play.mvc.Controller;
import play.mvc.Result;
import services.mailing.MailingService;
import services.mailing.MailingServiceImpl;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

//TODO : Remove this controller

public class TestController extends Controller {


    private final views.html.test testTemplate;
    private BookRepository bookRepository;
    private MessagesApi messagesApi;
    private MailerClient mailerClient;
    private MailingService mailingService;
    private MessagesApi messageApi;

    @Inject
    public TestController(MessagesApi messagesApi, BookRepository bookRepository
            , views.html.test testTemplate, MailerClient mailerClient
            , MailingServiceImpl mailingService) {
        this.testTemplate = testTemplate;
        this.messagesApi = messagesApi;
        this.bookRepository = bookRepository;
        this.mailerClient = mailerClient;
        this.mailingService = mailingService;
        this.messageApi = messagesApi;
    }

    public Result test() {
        int i = 45;
        QuestionForm questionForm = new QuestionForm();
        questionForm.setQuestion("what");
        questionForm.setWeight((short) 5);
        questionForm.setMultiple(false);
        questionForm.setQuestionNum((short) 1);
        List<AnswerForm> answers = new ArrayList<>();
        AnswerForm answerForm = new AnswerForm();
        answerForm.setNumAnswer((short) 1);
        answerForm.setAnswer("yes");
        answerForm.setRight(true);
        answers.add(answerForm);
        questionForm.setAnswers(answers);
        QuestionsForm questionsForm = new QuestionsForm();
        questionsForm.setBookID(i);
        List<QuestionForm> questionFormList = new ArrayList<>();
        questionFormList.add(questionForm);
        questionsForm.setQuestions(questionFormList);

        JsonNode jsonNode = Json.toJson(questionsForm);
        return ok(jsonNode.toString());
    }



    public Result test2() {
        changeLang("fr");
        return ok();

    }

    public Result test3(int id) {
        return ok("error");
    }





    /*public Result createUserAnswerForm() {
        UserAnswerForm userAnswerForm = new UserAnswerForm();
        userAnswerForm.setIdBook(24l);
        Map<Short, List<Short>> map = new HashMap<>();
        short i = 26;
        short j = 83;
        List<Short> list = new ArrayList<>();
        list.add(j);
        map.put(i, list);
        userAnswerForm.setQustionsAnswers(map);
        JsonNode jsonNode = Json.toJson(userAnswerForm);
        return ok(jsonNode);
    }*/


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


    }


    public Result sendMail() {
        String str = "Jfldskjf";
        return ok(str);
    }


}
