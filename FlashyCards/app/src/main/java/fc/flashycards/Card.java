package fc.flashycards;

/**
 * Created by Sean on 10/5/2014.
 *
 * This class defines the Card object and contains information
 * about each individual card located within a deck, including
 * the front/back, and the weight
 */

public class Card {
    private String front;
    private String back;
    private int weight;
    private final int WEIGHT_LIMIT = 5;

    public Card(String front, String back) {
        this.front = front;
        this.back = back;
        this.weight = 1;
    }

    //Add weight if limit not exceeded
    public void addWeight() {
        weight += weight < WEIGHT_LIMIT ? 1 : 0;
    }

    //Lose wight to min of 1
    public void loseWeight() {
        weight -= weight > 1 ? 1 : 0;
    }

    public void resetWeight() {
        weight = 1;
    }

    public String getFront() {
        return this.front;
    }

    public String getBack() {
        return this.back;
    }

    public int getWeight() { return this.weight; }
}
