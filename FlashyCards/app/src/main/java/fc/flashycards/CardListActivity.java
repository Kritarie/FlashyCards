package fc.flashycards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import fc.flashycards.sql.Card;
import fc.flashycards.sql.DatabaseHandler;
import fc.flashycards.sql.Deck;


public class CardListActivity extends Activity {

    private ListView cardListView;
    private CardAdapter adapter;
    private Deck deck;
    private int deckId;

    private DatabaseHandler db;

    private Context context;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        cardListView = (ListView) findViewById(R.id.card_list);
        context = getApplicationContext();

        //Get deck from parent activity
        Intent i = getIntent();
        deckId = i.getIntExtra("deck", 0);

        //Get all cards as cursor and get deck as deck object
        db = new DatabaseHandler(this);
        Cursor cursor = db.getAllCardsCursor(deckId);
        deck = db.getDeck(deckId);
        db.close();

        setTitle(deck.getName());

        emptyText = (TextView) findViewById(R.id.no_cards_text);
        adapter = new CardAdapter(this, cursor, emptyText, deckId);
        cardListView.setAdapter(adapter);
        toggleEmptyText();
    }

    @Override
    public void onPause() {
        super.onPause();
        deck.setSize(adapter.getCount());
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
        builder.setTitle("Create Card")
                .setView(dialogView)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText frontBox = (EditText) dialogView.findViewById(R.id.new_card_front);
                        EditText backBox = (EditText) dialogView.findViewById(R.id.new_card_back);
                        String front = frontBox.getText().toString();
                        String back = backBox.getText().toString();
                        if (!front.isEmpty() && !back.isEmpty()) {
                            db = new DatabaseHandler(context);
                            db.addCard(new Card(deck.getId(), front, back, 0), deck);
                            Cursor c = db.getAllCardsCursor(deckId);
                            db.close();
                            adapter.swapCursor(c);
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
                            db = new DatabaseHandler(context);
                            db.addCard(new Card(deck.getId(), front, back, 0), deck);
                            Cursor c = db.getAllCardsCursor(deckId);
                            db.close();
                            adapter.swapCursor(c);
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

    //If deck is empty, display text instead
    public void toggleEmptyText() {
        if (adapter.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
        }
    }
}
