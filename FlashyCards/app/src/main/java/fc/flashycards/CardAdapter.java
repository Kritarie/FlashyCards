package fc.flashycards;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import fc.flashycards.sql.DatabaseHandler;

/**
 * Created by Sean on 11/3/2014.
 *
 */
public class CardAdapter extends ResourceCursorAdapter{

    private Context context;
    private TextView emptyText;
    private int deckId;

    public CardAdapter(Context context, Cursor cursor, TextView emptyText, int deckId){
        super(context,R.layout.card_list_row, cursor);
        this.context = context;
        this.emptyText = emptyText;
        this.deckId = deckId;
    }

    @Override
    public void bindView(View view, Context ctx, Cursor cursor) {
        final int cardId = cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_ID));

        TextView front = (TextView) view.findViewById(R.id.cardfront);
        front.setText(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_FRONT)));

        TextView back = (TextView) view.findViewById(R.id.cardback);
        back.setText(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_BACK)));

        ImageButton delete = (ImageButton) view.findViewById(R.id.btn_card_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler db = new DatabaseHandler(context);
                db.deleteCard(cardId);
                Cursor c = db.getAllCardsCursor(deckId);
                db.close();
                swapCursor(c);
                toggleEmptyText();
            }
        });
    }

    public void toggleEmptyText() {
        if (isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
        }
    }
}
