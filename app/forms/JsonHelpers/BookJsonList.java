package forms.JsonHelpers;

import models.book.Book;

import java.util.ArrayList;
import java.util.List;

public class BookJsonList {
    private List<BookJson> bookJsonList;

    public List<BookJson> getBookJsonList() {
        return bookJsonList;
    }

    public void setBookJsonList(List<BookJson> bookJsonList) {
        this.bookJsonList = bookJsonList;
    }



    public static BookJsonList fromBookList(List<Book> books) {
        List<BookJson> bookJsons = new ArrayList<>();
        for (Book book : books) {
            bookJsons.add(BookJson.fromBook(book));
        }
        BookJsonList bookJsonList = new BookJsonList();
        bookJsonList.setBookJsonList(bookJsons);
        return bookJsonList;
    }
}
