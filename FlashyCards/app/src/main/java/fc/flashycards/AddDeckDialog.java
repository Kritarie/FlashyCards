package fc.flashycards;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Sean on 10/4/2014.
 *
 * This class defines the dialog presented to the user when a new deck
 * is to be created.
 */

public class AddDeckDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Define custom layout for dialog view
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_new_deck, null);
        builder.setView(view);

        //Define action if CANCEL is pressed
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        //Define action if OK is pressed
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //Get user input
                EditText deckName = (EditText) view.findViewById(R.id.new_deck_name);
                String name = deckName.getText().toString();

                //If no text entered, close
                if (name.equals("")) {
                    dialog.dismiss();
                } else if (fileExists(name)) {
                    //File exists, notify user
                    Context context = getActivity().getApplicationContext();
                    String errorText = "The deck \"" + name + "\" already exists";
                    int time = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, errorText, time);
                    toast.show();
                } else {
                    //File doesn't exist, ok to create
                    FileOutputStream fos;
                    try {
                        fos = getActivity().getApplicationContext().openFileOutput(name, Context.MODE_PRIVATE);
                        fos.write(name.getBytes());
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ((DeckListActivity) getActivity()).updateDeckList();
                    ((DeckListActivity) getActivity()).editDeck(name);
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

    //Check if file exists in internal storage
    private boolean fileExists(String name) {
        File file = getActivity().getBaseContext().getFileStreamPath(name);
        return file.exists();
    }
}
