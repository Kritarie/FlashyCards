package fc.flashycards;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Define custom layout for dialog view
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_new_card, null);
        builder.setView(view);

        deckName = ((CardListActivity) getActivity()).getFileName();

        //Define action if CANCEL is pressed
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        //Define action is NEXT is pressed
        builder.setNeutralButton(R.string.neutral, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditText frontText = (EditText) view.findViewById(R.id.new_card_front);
                EditText backText = (EditText) view.findViewById(R.id.new_card_back);
                front = frontText.getText().toString();
                back = backText.getText().toString();

                //If either field is empty, cancel
                if (front.isEmpty() || back.isEmpty()) {
                    dialog.dismiss();
                } else if (cardExists()) {
                    //Card exists, notify user
                    Context context = getActivity().getApplicationContext();
                    String errorText = "The entered card already exists in this deck";
                    int time = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, errorText, time);
                    toast.show();
                    dialog.dismiss();
                } else {
                    //Card is good, append to file
                    FileOutputStream fos;
                    try {
                        fos = getActivity().getApplicationContext().
                                openFileOutput(deckName, Context.MODE_APPEND);
                        fos.write((front + "~" + back + "\n").getBytes());
                        fos.close();
                    } catch (IOException e) {
                        //TODO Handle this
                        e.printStackTrace();
                    }
                    //Because NEXT is pressed, open dialog again
                    ((CardListActivity) getActivity()).updateCardList();
                    dialog.dismiss();
                }
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

                //If either field is empty, cancel
                if (front.isEmpty() || back.isEmpty()) {
                    dialog.dismiss();
                } else if (cardExists()) {
                    //Card exists, notify user
                    Context context = getActivity().getApplicationContext();
                    String errorText = "The entered card already exists in this deck";
                    int time = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, errorText, time);
                    toast.show();
                    dialog.dismiss();
                } else {
                    //Card is good, append to file
                    FileOutputStream fos;
                    try {
                        fos = getActivity().getApplicationContext().
                                openFileOutput(deckName, Context.MODE_APPEND | Context.MODE_PRIVATE);
                        fos.write((front + "~" + back + "\n").getBytes());
                        fos.close();
                    } catch (IOException e) {
                        //TODO Handle this
                        e.printStackTrace();
                    }
                    ((CardListActivity) getActivity()).updateCardList();
                    dialog.dismiss();
                }
            }
        });

        builder.setTitle(R.string.add_card_title);

        return builder.create();
    }

    private boolean cardExists() {
        File file = new File(getActivity().getFilesDir() + "/" + deckName);
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.equals(front + "~" + back)) {
                    return true;
                }
            }
        } catch (IOException e) {
            //TODO handle this
            e.printStackTrace();
        }
        return false;
    }

    public void setMenuItem(MenuItem item) { this.item = item; }
}
