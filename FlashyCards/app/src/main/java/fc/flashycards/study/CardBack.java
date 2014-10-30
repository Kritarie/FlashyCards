package fc.flashycards.study;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import fc.flashycards.R;

/**
 * This fragment is page two of the ViewPager for the
 * Study activity. It represents the back of the current
 * flash card. It also contains two buttons for the user
 * to press which loads up the next card and modifies the
 * weight of the current card.
 */
public class CardBack extends Fragment {

    // Store instance variables
    private String title;
    private int page;
    private StudyActivity activity;

    // newInstance constructor for creating fragment with arguments
    public static CardBack newInstance(int page, String title) {
        CardBack instance = new CardBack();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        instance.setArguments(args);
        return instance;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
        activity = (StudyActivity) getActivity();
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_back, container, false);

        //Set button listeners
        ImageButton pos = (ImageButton) view.findViewById(R.id.btnPositive);
        pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //On positive click
                activity.vp.setCurrentItem(0, true);
            }
        });

        ImageButton neg = (ImageButton) view.findViewById(R.id.btnNegative);
        neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //On negative click
                activity.vp.setCurrentItem(0, true);
            }
        });

        return view;
    }
}
