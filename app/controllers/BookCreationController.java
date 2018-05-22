package controllers;

import Persistance.DAOs.BookRepository;
import com.fasterxml.jackson.databind.JsonNode;
import forms.BookForm;
import models.book.Book;
import models.book.BookCreationException;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;


//DONE
//TODO : handle excpetions in contoller
public class BookCreationController extends Controller {

    private BookRepository bookRepository;
    private views.html.readbook readBookTemplate;
    private views.html.Quizz quizzTemplate;
    private views.html.book.bookCreation BookCreation;
    private views.html.book.bookList BookList;
    private views.html.book.quizzCreation QuizzCreation;

    @Inject
    public BookCreationController(BookRepository bookRepository,
                                  views.html.book.bookCreation BookCreation,
                                  views.html.book.bookList Booklist,
                                  views.html.book.quizzCreation QuizzCreation) {
        this.bookRepository = bookRepository;
        this.readBookTemplate = readBookTemplate;
        this.quizzTemplate = quizzTemplate;
        this.BookCreation = BookCreation;
        this.BookList = Booklist;
        this.QuizzCreation = QuizzCreation;

    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletableFuture<Result> addBook() throws BookCreationException {

        JsonNode jsonNode = request().body().asJson();
        String str = jsonNode.get("categories").toString();
        BookForm bookForm = Json.fromJson(jsonNode, BookForm.class);
        Book book = bookForm.toBook();

        return bookRepository.create(book).thenApply(e -> {
            return ok("done");
        });

    }

    public CompletableFuture<Result> getBook(int id) {
        return bookRepository.find(id).thenApply(b -> {
            return ok(readBookTemplate.render(b));
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

    public Result bookCreationPage() {
        return ok(BookCreation.render());
    }

    public Result quizzCreationPage() {
        return ok(QuizzCreation.render());
    }

    public Result bookListPage() {
        return ok(BookList.render());
    }




}
