package fc.flashycards;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import fc.flashycards.sql.DatabaseHandler;
import fc.flashycards.sql.Deck;

/**
 * Created by Sean on 10/4/2014.
 *
 * This class defines the dialog presented to the user when a new deck
 * is to be created.
 */

public class AddDeckDialog extends DialogFragment {

    private DeckListActivity activity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (DeckListActivity) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //Define custom layout for dialog view
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_new_deck, null);
        builder.setView(view);

        //Define action if CANCEL is pressed
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        //Define action if OK is pressed
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //Get user input
                EditText deckName = (EditText) view.findViewById(R.id.new_deck_name);
                String name = deckName.getText().toString();
                deckName.getText().clear();

                if (!name.equals("")) {
                    DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
                    Deck d = new Deck(name, 0);
                    db.addDeck(d);
                    db.close();

                    activity.onResume();
                }
            }
        });

        builder.setTitle(R.string.add_deck_title);

        return builder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //If a hardware keyboard doesn't exist, open soft keyboard
        if (getResources().getConfiguration().keyboard == Configuration.KEYBOARD_NOKEYS ) {
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }
}
