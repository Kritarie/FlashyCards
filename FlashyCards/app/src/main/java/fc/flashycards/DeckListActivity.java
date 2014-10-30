package fc.flashycards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
        decks = new ArrayList<Deck>();
        deckListAdapter = new DeckListAdapter(this, decks);
        deckListView.setAdapter(deckListAdapter);

        //populateDeckList();

        //On listview item clicked, open study mode for selected deck
        deckListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                //Start new study activity
                Intent intent = new Intent(getBaseContext(), StudyActivity.class);
                intent.putExtra("deck", (Deck) deckListView.getItemAtPosition(position));
                startActivity(intent);
            }
        });

        registerForContextMenu(deckListView);
    }

    //Creates our header and "add" button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.deck_list_menu, menu);
        return true;
    }

    //On long click, open menu of items defined in values.xml
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.deck_list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(decks.get(info.position).getName());
            String[] menuItems = getResources().getStringArray(R.array.deck_list_menu);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        Deck listItem = decks.get(info.position);
        Log.d("Deck", "This deck is: " + listItem.getId());
        //Do things based on what action was selected
        switch (menuItemIndex) {
            //Edit
            case 0:
                editDeck(listItem);
                break;
            //Delete
            case 1:
                deleteDeck(listItem);
                toggleEmptyText();
                break;
            default:
                break;
        }

        return true;
    }

    // Add deck dialog
    public void Add(MenuItem item) {
        AddDeckDialog d = new AddDeckDialog();
        d.show(getFragmentManager(), "Add Deck Dialog");
    }

    @Override
    public void onResume() {
        super.onResume();
        db = new DatabaseHandler(getApplicationContext());
        decks = db.getAllDecks();
        db.close();

        toggleEmptyText();

        deckListAdapter = new DeckListAdapter(this, decks);
        deckListView.setAdapter(deckListAdapter);
    }

    //Starts CardListActivity to edit the specified deck
    public void editDeck(Deck d) {
        Intent intent = new Intent(getBaseContext(), CardListActivity.class);
        intent.putExtra("deck", d);
        startActivity(intent);
    }

    //Remove deck from list and database
    public void deleteDeck(Deck d) {
        db = new DatabaseHandler(getApplicationContext());
        db.deleteDeck(d);
        db.close();
        Log.d("Delete Deck", "Deleted deck: " + d.getName() + " " + d.getId());
        decks.remove(d);
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
}
