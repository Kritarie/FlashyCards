package fc.flashycards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private String[] decks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_list);
        deckListView = (ListView) findViewById(R.id.deck_list);
        updateDeckList();
        registerForContextMenu(deckListView);

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
            menu.setHeaderTitle(decks[info.position]);
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
        String listItemName = decks[info.position];
        //Do things based on what action was selected
        switch (menuItemIndex) {
            //Edit
            case 0:
                editDeck(listItemName);
                break;
            //Delete
            case 1:
                deleteDeck(listItemName);
                updateDeckList();
                break;
            default:
                break;
        }

        return true;
    }

    //Whenever this activity resumes, we want to update the deck list
    @Override
    public void onResume() {
        super.onResume();
        updateDeckList();
    }

    //Add deck dialog
    public void Add(MenuItem item) {
        AddDeckDialog d = new AddDeckDialog();
        d.show(getFragmentManager(), "Add Deck Dialog");
    }

    public void updateDeckList() {
        //Get all the files in internal storage as list of names
        File[] deckFiles = getFilesDir().listFiles();

        //If there are no decks, notify user and return
        TextView tv = (TextView) findViewById(R.id.no_decks_text);
        if (deckFiles.length == 0) {
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
        }

        decks = new String[deckFiles.length];
        for (int i = 0; i < deckFiles.length; i++) {
            decks[i] = deckFiles[i].getName();
        }
        ArrayList<String> deckList = new ArrayList<String>();
        deckList.addAll(Arrays.asList(decks));

        //Create adapter for deckList
        deckListAdapter = new ArrayAdapter<String>(this, R.layout.deck_list_row, deckList);
        deckListView.setAdapter(deckListAdapter);
    }

    //Starts CardListActivity to edit the specified deck
    public void editDeck(String name) {
        Intent intent = new Intent(getBaseContext(), CardListActivity.class);
        intent.putExtra("deckName", name);
        startActivity(intent);
    }

    public void deleteDeck(String name) {
        File file = new File(getFilesDir(), name);
        Context context = getApplicationContext();
        String notifyText;
        int time = Toast.LENGTH_SHORT;
        if (file.delete()) {
            notifyText = "The deck \"" + name + "\" was deleted";
        } else {
            notifyText = "The deck \"" + name + "\" could not be deleted";
        }
        Toast toast = Toast.makeText(context, notifyText, time);
        toast.show();
    }
}
