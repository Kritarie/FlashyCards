package fc.flashycards;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class CardListActivity extends Activity {

    public ListView cardListView;
    public ArrayAdapter<Card> cardListAdapter;
    public ArrayList<Card> cards;
    public String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        cardListView = (ListView) findViewById(R.id.card_list);
        cards = new ArrayList<Card>();

        //Create adapter for deckList
        cardListAdapter = new CardListAdapter(this, cards);
        cardListView.setAdapter(cardListAdapter);

        //Get file name from parent activity
        fileName = getIntent().getExtras().getString("deckName");
        setTitle(fileName);
    }

    //Load deck from file
    @Override
    protected void onResume() {
        super.onResume();
        File file = new File(getFilesDir() + "/" + fileName);
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            String[] cardData;
            while ((line = in.readLine()) != null) {
                cardData = line.split("~");
                cards.add(new Card(cardData[0], cardData[1]));
            }
            in.close();
        } catch (IOException e) {
            //TODO Handle this correctly
            e.printStackTrace();
        }
        toggleEmptyText();
        cardListAdapter.notifyDataSetChanged();
    }

    //Save deck to file
    @Override
    protected void onStop() {
        super.onStop();
        FileOutputStream fos;
        try {
            fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            for (Card c : cards) {
                fos.write((c.getFront() + "~" + c.getBack() + "\n").getBytes());
            }
            fos.close();
        } catch (IOException e) {
            //TODO Handle this correctly
            e.printStackTrace();
        }
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
