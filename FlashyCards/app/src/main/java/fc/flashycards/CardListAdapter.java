package fc.flashycards;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by sean on 10/7/14.
 *
 * Used to translate Card objects into meaningful
 * listviewitem objects for display in the CardListActivity
 */
public class CardListAdapter<Card> extends ArrayAdapter<Card> {
    private final Context context;
    private final ArrayList<Card> cards;

    public CardListAdapter(Context context, ArrayList<Card> cards) {
        super(context, R.layout.card_list_row, cards);
        this.context = context;
        this.cards = cards;
    }

    //TODO Pretty much this whole adapter, also card_list_row
}
