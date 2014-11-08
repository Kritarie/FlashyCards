package fc.flashycards.study;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import fc.flashycards.sql.Card;

/**
 * Created by Sean on 11/7/2014.
 *
 * This class defines the methods by which a Card
 * will be intelligently selected pseudo-randomly
 * based on weight
 */
public class CardSelector {

    private static final String TAG = "Selector";

    private ArrayList<Card> cards;
    private ArrayList<Integer> selector;
    private Random rand;

    public CardSelector(ArrayList<Card> cards) {
        this.cards = cards;
        this.selector = refreshSelector();
        rand = new Random();
    }

    //Grabs a new random index from the selector which pulls from cards
    public Card generate() {
        if (selector.size() == 0) {
            selector = refreshSelector();
        }
        Log.d(TAG, "Selector has " + selector.size() + " more elements");
        return cards.get(selector.remove(randInt(0, selector.size() - 1)));
    }

    //Fill selector with indices representing each card in the deck
    //based on the weight of that card
    private ArrayList<Integer> refreshSelector() {
        ArrayList<Integer> temp = new ArrayList<Integer>();
        for (int i = 0; i < cards.size(); i++) {
            Log.d(TAG, "Card weight for " + cards.get(i).getFront() + " is " + cards.get(i).getWeight());
            temp.add(i);
            for (int j = 1; j < cards.get(i).getWeight(); j++) {
                temp.add(i);
            }
        }
        return temp;
    }

    private int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }
}
