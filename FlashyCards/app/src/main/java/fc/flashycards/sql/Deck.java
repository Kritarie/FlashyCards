package fc.flashycards.sql;

/**
 * Created by Sean on 10/21/14.
 */
public class Deck {
    private int id;
    private String name;
    private int size;

    // constructors
    public Deck() {
        this.size = 0;
    }

    public Deck(String name) {
        this.name = name;
        this.size = 0;
    }

    public Deck(int id, String name) {
        this.id = id;
        this.name = name;
        this.size = 0;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    // getters
    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int size() {
        return this.size;
    }
}
