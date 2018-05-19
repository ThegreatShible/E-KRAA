package models.users;

public class Level {
    private int levelnum;
    private int levelmax;

    public Level(int levelnum, int levelmax) {
        this.levelnum = levelnum;
        this.levelmax = levelmax;
    }

    public int getLevelnum() {
        return levelnum;
    }

    public void setLevelnum(int levelnum) {
        this.levelnum = levelnum;
    }

    public int getLevelmax() {
        return levelmax;
    }

    public void setLevelmax(int levelmax) {
        this.levelmax = levelmax;
    }
}
