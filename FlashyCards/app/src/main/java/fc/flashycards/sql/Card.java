package fc.flashycards.sql;

/**
 * Created by Sean on 10/21/14.
 *
 * Describes the contents of a single card in a deck.
 * Contains the id of the deck it belongs to as well as its individual
 * card id. Also contains the front, back, and weight of card for studying.
 */
public class Card {

    private int id;
    private int deckId;
    private String front;
    private String back;
    private int weight;

    private static final int MAX_WEIGHT = 10;

    // constructors
    public Card() {
    }

    public Card(int deckId, String front, String back, int weight) {
        this.deckId = deckId;
        this.front = front;
        this.back = back;
        this.weight = weight;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setDeckId(int deckId) { this.deckId = deckId; }

    public void setFront(String front) {
        this.front = front;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public void setWeight(int weight) { this.weight = weight; }

    // getters
    public int getId() {
        return this.id;
    }

    public int getDeckId() { return deckId; }

    public String getFront() {
        return this.front;
    }

    public String getBack() {
        return this.back;
    }

    public int getWeight() {
        return this.weight;
    }
}
