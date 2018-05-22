package models.book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

//TODO : chouf fi netbeans les exception de persistance
public class Book {

    //Generated from database
    private final int idBook;
    private String content;
    private Language language;
    private Date lastModifDate;
    private Difficulty difficulty;
    private String title;
    private List<Question> questions = new ArrayList();
    private List<Category> categories = new ArrayList<>();

    public Book(int idBook, String title, String content, Language language,
                Date lastModifDate, Difficulty difficulty, List<Question> questions, List<Category> categories) {
        this.idBook = idBook;
        this.content = content;
        this.language = language;
        this.difficulty = difficulty;
        this.questions = questions;
        this.categories = categories;
        this.lastModifDate = lastModifDate;
        this.title = title;
    }

    public static Book create(String content, String title, String language, String difficulty,
                              List<Question> questions, List<Category> categories) throws BookCreationException {
        //TODO : Remove javascript tags
        //TODO : Verify length of arguments before database

        Difficulty diff = Difficulty.valueOf(difficulty);
        Language lang = Language.String2Language(language);
        short acc = 0;
        for (Question question : questions) {
            acc += question.getWeight();
        }
        if (acc != diff.getWeight()) throw new BookCreationException("somme des poinds non conforme");
        if (categories.isEmpty()) throw new BookCreationException("au moins une categorie");
        if (questions.isEmpty()) throw new BookCreationException("au moins une question");

        return new Book(0, title, content, lang, new Date(), diff, questions, categories);

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getLastModifDate() {
        return lastModifDate;
    }

    public void setLastModifDate(Date lastModifDate) {
        this.lastModifDate = lastModifDate;
    }

    public int getIdBook() {
        return idBook;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public int getScoreFromAnswer(UserAnswer userAnswer) {
        int score = 0;
        for (Map.Entry<Short, List<Short>> entry : userAnswer.getQuestionsAnswers().entrySet()) {
            Stream<Question> questions = getQuestions().stream();
            Stream<Question> newQuestions = questions.filter(question ->
                    question.getQuestionNum() == entry.getKey());
            Question question = newQuestions.findFirst().get();
            score += getScoreFromQuestion(question, entry.getValue());
        }
        return score;

    }

    private int getScoreFromQuestion(Question question, List<Short> answers) {
        Stream<Short> sts = question.getAnswers().stream().filter(ans -> ans.isValid()).map(Answer::getNumAnswer).sorted();
        Stream<Short> answs = answers.stream().sorted();
        int i = 0;
        if (sts.equals(answs)) i = question.getWeight();
        return i;
    }
}
