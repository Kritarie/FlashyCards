package fc.flashycards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import fc.flashycards.sql.Deck;

/**
 * Created by Sean on 10/21/14.
 *
 * This class translates Deck objects into ListView items.
 * ViewHolder allows for 25% better load performance.
 */
public class DeckListAdapter extends ArrayAdapter<Deck> {

    private final DeckListActivity activity;
    private final Context context;
    private final List<Deck> decks;

    static class ViewHolder {
        public TextView name;
        public TextView size;
        public ImageButton delete;
        public ImageButton edit;
    }

    public DeckListAdapter(Context context, List<Deck> decks) {
        super(context, R.layout.deck_list_row, decks);
        this.context = context;
        this.decks = decks;
        this.activity = (DeckListActivity) context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.deck_list_row, parent, false);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) rowView.findViewById(R.id.deck_name);
            viewHolder.size = (TextView) rowView.findViewById(R.id.deck_size);
            viewHolder.delete = (ImageButton) rowView.findViewById(R.id.btn_deck_delete);
            viewHolder.edit = (ImageButton) rowView.findViewById(R.id.btn_deck_edit);
            rowView.setTag(viewHolder);
        }

        final Deck deck = decks.get(position);
        ViewHolder holder = (ViewHolder) rowView.getTag();
        //Set views according to Card object
        holder.name.setText(deck.getName());
        holder.size.setText(String.valueOf(deck.size()));

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.deleteDeck(deck);
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.editDeck(deck);
            }
        });

        return rowView;
    }
}
