package fc.flashycards;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Home extends Activity {

    private ListView deckListView;
    private ArrayAdapter<String> deckListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        deckListView = (ListView) findViewById(R.id.deck_list);

        updateDeckList();
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

    //TODO Get list of files from internal storage and populate the listview
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
}
