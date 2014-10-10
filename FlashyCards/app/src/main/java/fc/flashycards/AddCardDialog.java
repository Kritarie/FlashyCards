package fc.flashycards;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Sean on 10/5/2014.
 *
 * This class defines the dialog presented to the user
 * when a new card is to be added to an existing deck.
 */
public class AddCardDialog extends DialogFragment {

    private String deckName;
    private String front;
    private String back;
    private MenuItem item;
    private CardListActivity activity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Define custom layout for dialog view
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_new_card, null);
        builder.setView(view);

        activity = (CardListActivity) getActivity();
        deckName = activity.fileName;

        //Define action if CANCEL is pressed
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        //TODO Redo add card for better performance (add to list, write to file on activity stop)
        //Define action is NEXT is pressed
        builder.setNeutralButton(R.string.neutral, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditText frontText = (EditText) view.findViewById(R.id.new_card_front);
                EditText backText = (EditText) view.findViewById(R.id.new_card_back);
                front = frontText.getText().toString();
                back = backText.getText().toString();
                tryAddToDeck(front, back);

                ((CardListActivity) getActivity()).Add(item);
            }
        });

        //Define action if OK is pressed
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditText frontText = (EditText) view.findViewById(R.id.new_card_front);
                EditText backText = (EditText) view.findViewById(R.id.new_card_back);
                front = frontText.getText().toString();
                back = backText.getText().toString();
                tryAddToDeck(front, back);
            }
        });

        builder.setTitle(R.string.add_card_title);

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

    private void tryAddToDeck(String f, String b) {
        if (!f.isEmpty() && !b.isEmpty()) {
            Card card = new Card(front, back);
            if (!activity.cards.contains(card)) {
                //Good to create
                activity.cards.add(card);
                activity.cardListAdapter.notifyDataSetChanged();
                activity.toggleEmptyText();
            } else {
                //Card exists, notify user
                String errorText = "The card already exists";
                int time = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(activity, errorText, time);
                toast.show();
            }
        }
    }

    public void setMenuItem(MenuItem item) { this.item = item; }
}
