package fc.flashycards;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;


public class CardListActivity extends Activity {

    private ListView cardListView;
    private ArrayAdapter<Card> cardListAdapter;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        //Get file name from parent activity
        fileName = getIntent().getExtras().getString("deckName");
        setTitle("Edit Deck: " + fileName);

        updateCardList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.card_list, menu);
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

    //TODO Finish this method
    public void updateCardList() {
        //Get all cards in the deck as a list of Card objects
        File deck = new File(getFilesDir() + "/" + fileName);
        ArrayList<Card> cardList = new ArrayList<Card>();

        try {
            BufferedReader in = new BufferedReader(new FileReader(deck));
        } catch (FileNotFoundException e) {
            //handle this error
        }

        //Create adapter for deckList
        cardListAdapter = new CardListAdapter<Card>(this, cardList);
        cardListView.setAdapter(cardListAdapter);
    }
}
