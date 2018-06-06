package models.book;

import com.fasterxml.jackson.annotation.JsonFormat;

public enum Language {
    ENGLISH("EN"),
    FRENCH("FR"),
    ARABIC("AR");

    private String language;

    Language(String lang) {
        this.language = lang;
    }

    public static Language String2Language(String string) throws BookCreationException {
        if (string.equals("FR")) return FRENCH;
        else if (string.equals("EN")) return ENGLISH;
        else if (string.equals("AR")) return ARABIC;
        else throw new BookCreationException("Language not supported");

    }

    @JsonFormat
    public String getLanguage() {
        return language;
    }


}
