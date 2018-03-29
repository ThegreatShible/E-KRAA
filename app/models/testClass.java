package models;

import java.util.ArrayList;
import java.util.List;

//TODO : Remove
public class testClass {

    private String name;
    private List<String> names = new ArrayList();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addChild(String child) {
        names.add(child);
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
