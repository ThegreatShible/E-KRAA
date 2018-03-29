package forms;

import java.util.List;

public class BookForm {
    private String title;
    private String difficulty;
    private List<String> categories;
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

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
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
}
