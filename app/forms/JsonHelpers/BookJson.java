package forms.JsonHelpers;

import models.book.Book;

public class BookJson {
    private String title;
    private String difficulty;
    private String Categorie;
    private String language;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategorie() {
        return Categorie;
    }

    public void setCategorie(String categorie) {
        Categorie = categorie;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public static BookJson fromBook(Book book) {
        BookJson bookJson = new BookJson();
        return bookJson;
    }
}
