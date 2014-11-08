package fc.flashycards.study;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;

import fc.flashycards.R;
import fc.flashycards.sql.Card;
import fc.flashycards.sql.DatabaseHandler;
import fc.flashycards.sql.Deck;

/**
 * Created by Sean on 11/7/2014.
 *
 * Study activity which uses viewflipper instead of pager.
 */
public class StudyActivity extends Activity {

    private int deckId;
    private Deck deck;
    private ArrayList<Card> cards;
    private CardSelector selector;
    private Card currentCard;

    private ViewFlipper flipper;
    private TextView front;
    private TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        //Initialize UI elements
        flipper = (ViewFlipper) findViewById(R.id.card_flipper);
        flipper.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
        front = (TextView) findViewById(R.id.card_front_text);
        back = (TextView) findViewById(R.id.card_back_text);

        //Get deck from parent activity
        Intent i = getIntent();
        deckId = i.getIntExtra("deck", 0);

        //Get deck and card info from sql
        DatabaseHandler db = new DatabaseHandler(this);
        deck = db.getDeck(deckId);
        cards = db.getAllCards(deckId);
        db.close();

        setTitle(deck.getName());

        //Initialize card selector
        selector = new CardSelector(cards);

        //Select first card
        currentCard = selector.generate();
        front.setText(currentCard.getFront());
    }

    //Called on screen press
    public void flipCard(View view) {
        if (flipper.getDisplayedChild() == 0) {
            back.setText(currentCard.getBack());
            flipper.showNext();
        }
    }

    //Called on green button press
    public void answerCorrect(View view) {
        currentCard.decrementWeight();
        currentCard = selector.generate();
        front.setText(currentCard.getFront());
        flipper.showNext();
    }

    //Called on red button press
    public void answerWrong(View view) {
        currentCard.incrementWeight();
        currentCard = selector.generate();
        front.setText(currentCard.getFront());
        flipper.showNext();
    }

    @Override
    public void onPause() {
        super.onPause();
        DatabaseHandler db = new DatabaseHandler(this);
        //Update the weight of each card in sql
        for (Card c : cards) {
            db.updateCard(c);
        }
        db.updateDeck(deck);
        db.close();
    }
}
