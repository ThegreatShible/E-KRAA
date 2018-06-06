package models.book;

public enum Difficulty {
    EASY((short) 10),
    INTERMEDIATE((short) 20),
    HARD((short) 30);

    private short weight;

    Difficulty(short weight) {
        this.weight = weight;
    }

    static public Difficulty string2Difficulty(String str) throws BookCreationException {
        if (str.equals("EASY")) return EASY;
        else if (str.equals("INTERMEDIATE")) return INTERMEDIATE;
        else if (str.equals("HARD")) return HARD;
        else throw new BookCreationException("wrong difficulty");

    }

    public short getWeight() {
        return weight;
    }

}
