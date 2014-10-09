package fc.flashycards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sean on 10/7/14.
 *
 * Used to translate Card objects into meaningful
 * listviewitem objects for display in the CardListActivity
 */
public class CardListAdapter extends ArrayAdapter<Card> {
    private final Context context;
    private final ArrayList<Card> cards;

    public CardListAdapter(Context context, ArrayList<Card> cards) {
        super(context, R.layout.card_list_row, cards);
        this.context = context;
        this.cards = cards;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Grab the row layout and its views
        View rowView = inflater.inflate(R.layout.card_list_row, parent, false);
        TextView frontView = (TextView) rowView.findViewById(R.id.front);
        TextView backView = (TextView) rowView.findViewById(R.id.back);
        TextView weightView = (TextView) rowView.findViewById(R.id.weight);

        //Set views according to Card object
        frontView.setText(cards.get(position).getFront());
        backView.setText(cards.get(position).getBack());
        weightView.setText(String.valueOf(cards.get(position).getWeight()));

        return rowView;
    }
}