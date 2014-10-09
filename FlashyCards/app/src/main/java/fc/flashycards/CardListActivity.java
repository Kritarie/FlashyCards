package fc.flashycards;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class CardListActivity extends Activity {

    private ListView cardListView;
    private ArrayAdapter<Card> cardListAdapter;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        cardListView = (ListView) findViewById(R.id.card_list);

        //Get file name from parent activity
        fileName = getIntent().getExtras().getString("deckName");
        setTitle(fileName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCardList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.card_list_menu, menu);
        return true;
    }

    //TODO Finish this method
    public void updateCardList() {
        //Get all cards in the deck as a list of Card objects
        File deck = new File(getFilesDir() + "/" + fileName);
        ArrayList<Card> cardList = new ArrayList<Card>();

        //If deck is empty, notify user and return
//        TextView tv = (TextView) findViewById(R.id.no_cards_text);
//        if (deck.length() == 0) {
//            tv.setVisibility(View.VISIBLE);
//            return;
//        } else {
//            tv.setVisibility(View.GONE);
//        }

        try {
            BufferedReader in = new BufferedReader(new FileReader(deck));
            String line;
            String[] cardData;
            while ((line = in.readLine()) != null) {
                cardData = line.split("~");
                cardList.add(new Card(cardData[0], cardData[1]));
            }
            in.close();
        } catch (IOException e) {
            //TODO handle this
            e.printStackTrace();
        }

        for (Card c : cardList) {
            System.out.println(c.getFront() + "~" + c.getBack());
        }
        //Create adapter for deckList
        cardListAdapter = new CardListAdapter(this, cardList);
        cardListView.setAdapter(cardListAdapter);
    }

    //Button for user to add card(s)
    public void Add(MenuItem item) {
        AddCardDialog d = new AddCardDialog();
        d.setMenuItem(item);
        d.show(getFragmentManager(), "Add Card Dialog");
    }

    //When a child fragment needs the filename
    public String getFileName() {
        return this.fileName;
    }
}
