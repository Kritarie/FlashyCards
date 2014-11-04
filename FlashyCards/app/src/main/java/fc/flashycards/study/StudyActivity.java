package fc.flashycards.study;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

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

    public Deck deck;
    private int deckId;
    public Card currentCard;

    private FragmentManager fragmentManager;
    public CardPager adapterViewPager;
    public ViewPager vp;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        context = getApplicationContext();
        fragmentManager = getSupportFragmentManager();

        //Get deck from parent activity
        Intent i = getIntent();
        deckId = i.getIntExtra("deck", 0);

        DatabaseHandler db = new DatabaseHandler(this);
        deck = db.getDeck(deckId);
        db.close();

        setTitle(deck.getName());

        //initialize viewpager
        adapterViewPager = new CardPager(fragmentManager);
        vp = (ViewPager) findViewById(R.id.pager);
        vp.setAdapter(adapterViewPager);
    }

    @Override
    public void onPause() {
        super.onPause();
        DatabaseHandler db = new DatabaseHandler(context);
        db.updateDeck(deck);
        db.close();
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
