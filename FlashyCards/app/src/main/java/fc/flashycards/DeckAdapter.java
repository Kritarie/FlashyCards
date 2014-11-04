package fc.flashycards;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.test.suitebuilder.annotation.Suppress;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import fc.flashycards.sql.DatabaseHandler;

/**
 * Created by Sean on 11/2/2014.
 *
 */
public class DeckAdapter extends ResourceCursorAdapter {

    private Context context;
    private TextView emptyText;

    public DeckAdapter(Context context, Cursor cursor, TextView emptyText){
        super(context,R.layout.deck_list_row, cursor);
        this.context = context;
        this.emptyText = emptyText;
    }

    @Override
    public void bindView(View view, Context ctx, Cursor cursor){
        final int deckId = cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_ID));

        TextView name = (TextView) view.findViewById(R.id.deck_name);
        name.setText(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_NAME)));

        TextView size = (TextView) view.findViewById(R.id.deck_size);
        size.setText(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SIZE)));

        //Delete deck dialog button
        ImageButton delete = (ImageButton) view.findViewById(R.id.btn_deck_delete);
        delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                LayoutInflater inflater = LayoutInflater.from(context);
                final View dialogView = inflater.inflate(R.layout.dialog_delete_deck, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Deck")
                        .setView(dialogView)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                DatabaseHandler db = new DatabaseHandler(context);
                                db.deleteDeck(deckId);
                                Cursor c = db.getAllDecksCursor();
                                db.close();
                                swapCursor(c);
                                toggleEmptyText();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog add = builder.create();
                add.show();
            }
        });

        //Edit deck button
        ImageButton edit = (ImageButton) view.findViewById(R.id.btn_deck_edit);
        edit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent intent = new Intent(context, CardListActivity.class);
                intent.putExtra("deck", deckId);
                context.startActivity(intent);
            }
        });
    }

    //If there are no decks, display textview
    public void toggleEmptyText() {
        if (isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
        }
    }
}
