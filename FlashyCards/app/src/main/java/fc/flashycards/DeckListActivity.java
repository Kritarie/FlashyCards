package fc.flashycards;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

    private DeckAdapter adapter;

    private DatabaseHandler db;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_list);
        ListView deckListView = (ListView) findViewById(R.id.deck_list);

        db = new DatabaseHandler(this);
        Cursor cursor = db.getAllDecksCursor();
        db.close();

        emptyText = (TextView) findViewById(R.id.no_decks_text);
        adapter = new DeckAdapter(this, cursor, emptyText);
        deckListView.setAdapter(adapter);

        //On listview item clicked, open study mode for selected deck
        deckListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                //Start new study activity
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                int deckId = cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_ID));
                Intent intent = new Intent(getBaseContext(), StudyActivity.class);
                intent.putExtra("deck", deckId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        db = new DatabaseHandler(this);
        Cursor cursor = db.getAllDecksCursor();
        db.close();
        adapter.swapCursor(cursor);
        toggleEmptyText();
        System.out.println("Adapter refreshed");
    }

    //Creates our header and "add" button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.deck_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_add:
                newDeck();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //New deck dialog
    public void newDeck() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogView = inflater.inflate(R.layout.dialog_new_deck, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Deck")
                .setView(dialogView)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText textBox = (EditText) dialogView.findViewById(R.id.new_deck_name);
                        String name = textBox.getText().toString();
                        if (!name.isEmpty()) {
                            db = new DatabaseHandler(getApplicationContext());
                            db.addDeck(new Deck(name, 0));
                            Cursor cursor = db.getAllDecksCursor();
                            db.close();
                            adapter.swapCursor(cursor);
                            toggleEmptyText();
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

    public void toggleEmptyText() {
        if (adapter.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
        }
    }

}
