package fc.flashycards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import fc.flashycards.sql.Card;
import fc.flashycards.sql.DatabaseHandler;
import fc.flashycards.sql.Deck;


public class CardListActivity extends Activity {

    private ListView cardListView;
    public CardListAdapter cardListAdapter;
    public ArrayList<Card> cards;
    public Deck deck;

    private DatabaseHandler db;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        context = getApplicationContext();

        //Get deck from parent activity
        Intent i = getIntent();
        deck = i.getParcelableExtra("deck");
        setTitle(deck.getName());

        //Get cards from deck and display in list view
        cardListView = (ListView) findViewById(R.id.card_list);
        db = new DatabaseHandler(context);
        cards = db.getAllCards(deck.getId());
        db.close();
        toggleEmptyText();
        cardListAdapter = new CardListAdapter(this, cards);
        cardListView.setAdapter(cardListAdapter);

        //Enable action bar home button
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            getActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_add:
                newCard();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //Button for user to add card(s)
    public void newCard() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogView = inflater.inflate(R.layout.dialog_new_card, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Deck")
                .setView(dialogView)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText frontBox = (EditText) dialogView.findViewById(R.id.new_card_front);
                        EditText backBox = (EditText) dialogView.findViewById(R.id.new_card_back);
                        String front = frontBox.getText().toString();
                        String back = backBox.getText().toString();
                        if (!front.isEmpty() && !back.isEmpty()) {
                            db = new DatabaseHandler(getApplicationContext());
                            db.addCard(new Card(deck.getId(), front, back, 0), deck);
                            cards = db.getAllCards(deck.getId());
                            db.close();
                            cardListAdapter.clear();
                            cardListAdapter.addAll(cards);
                            cardListAdapter.notifyDataSetChanged();
                            toggleEmptyText();
                        }
                    }
                })
                .setNeutralButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText frontBox = (EditText) dialogView.findViewById(R.id.new_card_front);
                        EditText backBox = (EditText) dialogView.findViewById(R.id.new_card_back);
                        String front = frontBox.getText().toString();
                        String back = backBox.getText().toString();
                        if (!front.isEmpty() && !back.isEmpty()) {
                            db = new DatabaseHandler(getApplicationContext());
                            db.addCard(new Card(deck.getId(), front, back, 0), deck);
                            cards = db.getAllCards(deck.getId());
                            db.close();
                            cardListAdapter.clear();
                            cardListAdapter.addAll(cards);
                            cardListAdapter.notifyDataSetChanged();
                            toggleEmptyText();
                            newCard();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog add = builder.create();
        if (getResources().getConfiguration().keyboard == Configuration.KEYBOARD_NOKEYS ) {
            add.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        add.show();
    }

    public void deleteCard(Card card) {
        cards.remove(card);
        toggleEmptyText();
        db = new DatabaseHandler(getApplicationContext());
        db.deleteCard(card);
        db.close();
        cardListAdapter.notifyDataSetChanged();
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
