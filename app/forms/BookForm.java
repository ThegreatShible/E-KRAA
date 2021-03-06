package forms;

import models.book.Book;
import models.book.BookCreationException;
import models.book.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookForm {


    private String title;
    private String difficulty;
    private List<Integer> categories;
    private String language;
    private String content;
    private List<QuestionForm> questions;

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

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<QuestionForm> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionForm> questions) {
        this.questions = questions;
    }

    public Book toBook(UUID creator) throws BookCreationException {
        List<Question> questionList = new ArrayList<>();
        if (questions != null) {
            for (QuestionForm question : questions) {
                questionList.add(question.toQuesiton());
            }
        }
        return Book.create(content, title, language, difficulty, questionList, categories, creator);
    }
}
