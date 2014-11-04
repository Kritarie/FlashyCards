package fc.flashycards.study;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private StudyActivity activity;
    private ViewPager vp;

    private TextView tv;
    private LinearLayout hiddenLayout;
    private ImageButton pos;
    private ImageButton neg;

    private Animation bottomUp;
    private Animation bottomDown;
    private Animation fadeIn;

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
        activity = (StudyActivity) getActivity();
        vp = (ViewPager) activity.findViewById(R.id.pager);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isResumed()) {
            if (isVisibleToUser) {
                //If this fragment is visible,
                //update text and fade in
                tv.setText(activity.currentCard.getBack());
                tv.startAnimation(fadeIn);
                tv.setVisibility(View.VISIBLE);

                //Slide up buttons
                hiddenLayout.startAnimation(bottomUp);
                hiddenLayout.setVisibility(View.VISIBLE);

                //Set the buttons clickable
                pos.setClickable(true);
                neg.setClickable(true);

                //Reset the background
                pos.getBackground().setAlpha(150);
                neg.getBackground().setAlpha(150);
            } else {
                tv.setVisibility(View.INVISIBLE);
                hiddenLayout.setVisibility(View.GONE);
            }
        }
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_back, container, false);

        tv = (TextView) view.findViewById(R.id.study_card_back);
        hiddenLayout = (LinearLayout) view.findViewById(R.id.buttonArea);

        pos = (ImageButton) view.findViewById(R.id.btnPositive);
        neg = (ImageButton) view.findViewById(R.id.btnNegative);

        bottomUp = AnimationUtils.loadAnimation(context,
                R.anim.bottom_up);
        bottomDown = AnimationUtils.loadAnimation(context,
                R.anim.bottom_down);
        fadeIn = AnimationUtils.loadAnimation(context,
                R.anim.fadein);

        //Set button listeners
        pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //On positive click
                pos.setClickable(false);
                pos.getBackground().setAlpha(200);
                neg.getBackground().setAlpha(100);
                hiddenLayout.startAnimation(bottomDown);
                hiddenLayout.setVisibility(View.GONE);

                vp.setCurrentItem(0, true);
            }
        });

        neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //On negative click
                neg.setClickable(false);
                neg.getBackground().setAlpha(200);
                pos.getBackground().setAlpha(100);
                hiddenLayout.startAnimation(bottomDown);
                hiddenLayout.setVisibility(View.GONE);

                vp.setCurrentItem(0, true);
            }
        });

        return view;
    }
}
