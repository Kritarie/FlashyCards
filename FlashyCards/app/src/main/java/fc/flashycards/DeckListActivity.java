package fc.flashycards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Sean on 10/5/2014.
 *
 * This activity is created when the app is started. A list of
 * available decks is populated from internal storage.
 */

public class DeckListActivity extends Activity {

    private ListView deckListView;
    private ArrayAdapter<String> deckListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_list);
        deckListView = (ListView) findViewById(R.id.deck_list);

        updateDeckList();

        //On listview item clicked, open study mode for selected deck
        deckListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                //Start new study activity
                Intent intent = new Intent(getBaseContext(), StudyActivity.class);
                intent.putExtra("deckName", deckListView.getItemAtPosition(position).toString());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    //Add deck dialog
    public void Add(MenuItem item) {
        AddDeckDialog d = new AddDeckDialog();
        d.show(getFragmentManager(), "Add Deck Dialog");
    }

    public void updateDeckList() {
        //Get all the files in internal storage as list of names
        File[] decks = getFilesDir().listFiles();
        String[] deckNames = new String[decks.length];
        for (int i = 0; i < decks.length; i++) {
            deckNames[i] = decks[i].getName();
        }
        ArrayList<String> deckList = new ArrayList<String>();
        deckList.addAll(Arrays.asList(deckNames));

        //Create adapter for deckList
        deckListAdapter = new ArrayAdapter<String>(this, R.layout.deck_list_row, deckList);
        deckListView.setAdapter(deckListAdapter);
    }

    //Starts CardListActivity to edit the specified deck
    public void editDeck(String name) {
        //Start new study activity
        Intent intent = new Intent(getBaseContext(), CardListActivity.class);
        intent.putExtra("deckName", name);
        startActivity(intent);
    }
}
