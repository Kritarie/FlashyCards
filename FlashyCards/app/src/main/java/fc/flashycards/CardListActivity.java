package fc.flashycards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import fc.flashycards.sql.Card;
import fc.flashycards.sql.DatabaseHandler;
import fc.flashycards.sql.Deck;


public class CardListActivity extends Activity {

    private ListView cardListView;
    public CardListAdapter cardListAdapter;
    public List<Card> cards;
    public Deck deck;

    private DatabaseHandler db;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        context = getApplicationContext();

        cardListView = (ListView) findViewById(R.id.card_list);

        //Get deck from parent activity
        Intent i = getIntent();
        deck = i.getParcelableExtra("deck");
        setTitle(deck.getName());

        Log.d("Deck", "This deck is: " + deck.getId());


        populateCardList();
    }

    private void populateCardList() {
        db = new DatabaseHandler(context);
        cards = db.getAllCards(deck.getId());
        db.close();

        toggleEmptyText();

        //Create adapter for deckList
        cardListAdapter = new CardListAdapter(this, cards);
        cardListView.setAdapter(cardListAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        deck.setSize(cards.size());
        DatabaseHandler db = new DatabaseHandler(context);
        db.updateDeck(deck);
        db.close();
    }

    //Inflate action bar layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.card_list_menu, menu);
        return true;
    }

    //Button for user to add card(s)
    public void Add(MenuItem item) {
        AddCardDialog d = new AddCardDialog();
        d.setMenuItem(item);
        d.show(getFragmentManager(), "Add Card Dialog");
    }

    //If deck is empty, display text instead
    public void toggleEmptyText() {
        TextView tv = (TextView) findViewById(R.id.no_cards_text);
        if (cards.size() == 0) {
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
        }
    }
}
