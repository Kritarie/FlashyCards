package fc.flashycards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sean on 10/7/14.
 *
 * Used to translate Card objects into meaningful
 * listviewitem objects for display in the CardListActivity
 */
public class CardListAdapter extends ArrayAdapter<Card> {
    private final CardListActivity activity;
    private final Context context;
    private final ArrayList<Card> cards;

    static class ViewHolder {
        public TextView front;
        public TextView back;
        public ImageButton delete;
    }

    public CardListAdapter(Context context, ArrayList<Card> cards) {
        super(context, R.layout.card_list_row, cards);
        this.context = context;
        this.cards = cards;
        this.activity = (CardListActivity) context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.card_list_row, parent, false);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.front = (TextView) rowView.findViewById(R.id.front);
            viewHolder.back = (TextView) rowView.findViewById(R.id.back);
            viewHolder.delete = (ImageButton) rowView.findViewById(R.id.delete);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        //Set views according to Card object
        holder.front.setText(cards.get(position).getFront());
        holder.back.setText(cards.get(position).getBack());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cards.remove(position);
                notifyDataSetChanged();
                activity.toggleEmptyText();
            }
        });

        return rowView;
    }
}