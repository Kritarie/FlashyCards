package fc.flashycards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fc.flashycards.sql.DatabaseHandler;
import fc.flashycards.sql.Deck;
import fc.flashycards.study.StudyActivity;

/**
 * Created by Sean on 10/5/2014.
 *
 * This activity is created when the app is started. A list of
 * available decks is populated from internal storage.
 */

public class DeckListActivity extends Activity {

    private ListView deckListView;
    public DeckListAdapter deckListAdapter;
    public List<Deck> decks;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_list);
        deckListView = (ListView) findViewById(R.id.deck_list);

        db = new DatabaseHandler(getApplicationContext());
        decks = db.getAllDecks();
        db.close();
        deckListAdapter = new DeckListAdapter(this, decks);
        deckListView.setAdapter(deckListAdapter);

        //On listview item clicked, open study mode for selected deck
        deckListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                //Start new study activity
                Intent intent = new Intent(getBaseContext(), StudyActivity.class);
                intent.putExtra("deck", (Deck) deckListView.getItemAtPosition(position));
                startActivity(intent);
            }
        });
    }

    //Creates our header and "add" button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.deck_list_menu, menu);
        return true;
    }

    // Add deck dialog
    public void Add(MenuItem item) {
        AddDeckDialog d = new AddDeckDialog(new DialogDismissHandler());
        d.show(getFragmentManager(), "Add Deck Dialog");
    }

    // Delete deck dialog
    public void Delete(Deck deck) {
//        DeleteDeckDialog d = new DeleteDeckDialog(deck, new DialogDismissHandler());
//        d.show(getFragmentManager(), "Delete Deck Dialog");
        decks.remove(deck);
        db = new DatabaseHandler(getApplicationContext());
        db.deleteDeck(deck);
        db.close();
        deckListAdapter.notifyDataSetChanged();
    }

    //If there are no decks, display textview
    public void toggleEmptyText() {
        TextView tv = (TextView) findViewById(R.id.no_decks_text);
        if (decks.size() == 0) {
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
        }
    }

    private class DialogDismissHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: //Add deck
                    if (msg.obj != null) {
                        System.out.println("Message found");
                        db = new DatabaseHandler(getApplicationContext());
                        db.addDeck((Deck) msg.obj);
                        decks = db.getAllDecks();
                        db.close();
                        toggleEmptyText();
                        deckListAdapter.notifyDataSetChanged();
                    }
                    break;
                case 1: //Delete deck
                    break;
                default:
                    break;
            }
        }
    }
}
