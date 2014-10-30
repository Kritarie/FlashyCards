package fc.flashycards.study;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import fc.flashycards.R;
import fc.flashycards.sql.Card;
import fc.flashycards.sql.DatabaseHandler;
import fc.flashycards.sql.Deck;

/**
 * Created by Sean on 10/5/2014.
 *
 * This activity is created when a deck is selected by the user.
 */

public class StudyActivity extends FragmentActivity {

    private Deck deck;
    private DatabaseHandler db;
    private List<Card> cards;
    public static ArrayList<Integer> randCardPicker;

    private FragmentManager fragmentManager;
    public CardPager adapterViewPager;
    public ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        fragmentManager = getSupportFragmentManager();

        //Get deck from parent activity
        Intent i = getIntent();
        deck = i.getParcelableExtra("deck");
        setTitle(deck.getName());

        //Get all cards in this deck
        db = new DatabaseHandler(this);
        cards = db.getAllCards(deck.getId());
        db.close();

        //For each card, create new instance in randCardPicker
        randCardPicker = new ArrayList<Integer>();
        for (int x = 0; x < cards.size(); x++) {
            Card c = cards.get(x);
            int w = c.getWeight();
            //Card weight is minimum, add only one instance
            if (w == 0) {
                randCardPicker.add(x);
                continue;
            }
            //Add weight instances of this card
            for (int j = 0; j < w; j++) {
                randCardPicker.add(x);
            }
        }

        //initialize viewpager
        adapterViewPager = new CardPager(fragmentManager);
        vp = (ViewPager) findViewById(R.id.pager);
        vp.setAdapter(adapterViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.study_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
