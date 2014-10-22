package fc.flashycards.sql;

/**
 * Created by Sean on 10/21/14.
 */
public class Card {

    private int id;
    private String front;
    private String back;
    private int weight;

    private static final int MAX_WEIGHT = 10;

    // constructors
    public Card() {
    }

    public Card(String front, String back) {
        this.front = front;
        this.back = back;
    }

    public Card(int id, String front, String back) {
        this.id = id;
        this.front = front;
        this.back = back;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public void setBack(String back) {
        this.back = back;
    }

    // getters
    public long getId() {
        return this.id;
    }

    public String getFront() {
        return this.front;
    }

    public String getBack() {
        return this.back;
    }

    public int getWeight() {
        return this.weight;
    }

    // methods

    public boolean incWeight() {
        if (weight >= MAX_WEIGHT) return false;
        weight++;
        return true;
    }
}
