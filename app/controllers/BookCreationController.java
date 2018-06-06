package controllers;

import Persistance.DAOs.BookRepository;
import com.fasterxml.jackson.databind.JsonNode;
import forms.BookForm;
import forms.JsonHelpers.BookJsonList;
import forms.QuestionForm;
import forms.QuestionsForm;
import models.book.Book;
import models.book.BookCreationException;
import models.book.Question;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


//DONE
//TODO : handle excpetions in contoller
public class BookCreationController extends Controller {

    private BookRepository bookRepository;

    private views.html.book.bookCreation BookCreation;
    private views.html.book.bookList BookList;
    private views.html.book.quizzCreation QuizzCreation;
    private Form<BookForm> bookForm;
    private Form<QuestionsForm> questionsForm;

    @Inject
    public BookCreationController(BookRepository bookRepository,
                                  views.html.book.bookCreation BookCreation,
                                  views.html.book.bookList Booklist,
                                  views.html.book.quizzCreation QuizzCreation,
                                  FormFactory formFactory) {
        this.bookRepository = bookRepository;
        this.BookCreation = BookCreation;
        this.BookList = Booklist;
        this.QuizzCreation = QuizzCreation;
        this.bookForm = formFactory.form(BookForm.class);
        this.questionsForm = formFactory.form(QuestionsForm.class);


    }


    public CompletableFuture<Result> addBook() throws BookCreationException {

        Form<BookForm> bindedBookForm = bookForm.bindFromRequest();
        if (bindedBookForm.hasErrors()) {
            bindedBookForm.allErrors().forEach(x -> System.out.println(x));
            return CompletableFuture.supplyAsync(() -> internalServerError());
        } else {
            BookForm bf = bindedBookForm.get();
            //TODO : modifye this
            UUID userid = UUID.fromString(session("user"));
            //UUID userid = UUID.fromString("b7635aaa-77e6-4c64-b38c-fcfa8005a39e");
            Book book = bf.toBook(userid);
            return bookRepository.create(book).thenApply(e -> {
                return redirect(routes.BookCreationController.quizzCreationPage(e));
            });
        }
    }

    public CompletableFuture<Result> getBook(int id) {
        return bookRepository.find(id).thenApply(b -> {
            return ok();
        });

    }

    //TODO : replace done with quizz template
    public CompletableFuture<Result> getQuestions(int id) {
        return bookRepository.find(id).thenApply(b -> {
            return ok("done");
        });

    }

    public CompletableFuture<Result> removeBook(int id) {
        return bookRepository.destroy(id).thenApply(e -> {
            return ok("destroyed");
        });
    }

    public CompletableFuture<Result> bookCreationPage() {
        return bookRepository.getCategories().thenApply(cats -> {
            return ok(BookCreation.render(cats));
        });

    }

    public Result quizzCreationPage(int bookID) {
        try {
            Book b = bookRepository.find(bookID).get(2, TimeUnit.SECONDS);
            return ok(QuizzCreation.render(b));
        } catch (Exception e) {
            return unauthorized();
        }

    }

    public CompletableFuture<Result> bookListPage() {
        UUID uuid = UUID.fromString(session("user"));
        return bookRepository.findAll(uuid).thenApply(books -> {
            BookJsonList bookJsonList = BookJsonList.fromBookList(books);
            String str = Json.toJson(bookJsonList).toString();
            return ok(BookList.render(str));
        });

    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result addQuestions() {
        UUID uuid = UUID.fromString(session("user"));
        //UUID uuid = UUID.randomUUID();
        JsonNode jsonNode = request().body().asJson();
        QuestionsForm questionsForm = Json.fromJson(jsonNode, QuestionsForm.class);

        try {
            List<Question> questions = new ArrayList<>();
            for (QuestionForm ques : questionsForm.getQuestions()) {
                questions.add(ques.toQuesiton());
            }

            bookRepository.addQuestionsToBook(questionsForm.getBookID(), questions).get(2, TimeUnit.SECONDS);
            return redirect(routes.BookCreationController.bookCreationPage());
        } catch (Exception e) {
            e.printStackTrace();
            return ok("lol");
        }
    }
}
