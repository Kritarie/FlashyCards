package fc.flashycards.study;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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
    private Context context;

    private LinearLayout hiddenLayout;
    private ImageButton pos;
    private ImageButton neg;

    private Animation bottomUp;
    private Animation bottomDown;

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
        context = getActivity().getApplicationContext();
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_back, container, false);

        hiddenLayout = (LinearLayout) view.findViewById(R.id.buttonArea);

        pos = (ImageButton) view.findViewById(R.id.btnPositive);
        neg = (ImageButton) view.findViewById(R.id.btnNegative);
        pos.getBackground().setAlpha(150);
        neg.getBackground().setAlpha(150);

        //Set button listeners
        pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //On positive click
                pos.getBackground().setAlpha(200);
                neg.getBackground().setAlpha(100);
            }
        });

        neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //On negative click
                neg.getBackground().setAlpha(200);
                pos.getBackground().setAlpha(100);
            }
        });

        bottomUp = AnimationUtils.loadAnimation(context,
                R.anim.bottom_up);
        bottomDown = AnimationUtils.loadAnimation(context,
                R.anim.bottom_down);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        hiddenLayout.startAnimation(bottomUp);
        hiddenLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        hiddenLayout.startAnimation(bottomDown);
        hiddenLayout.setVisibility(View.GONE);
    }
}