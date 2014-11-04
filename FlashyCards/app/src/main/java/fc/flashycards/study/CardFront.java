package fc.flashycards.study;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import fc.flashycards.R;
import fc.flashycards.sql.Card;
import fc.flashycards.sql.DatabaseHandler;

/**
 * This fragment is page one of the ViewPager for the
 * Study activity. It represents the front of the current
 * flash card.
 */
public class CardFront extends Fragment {

    // Store instance variables
    private String title;
    private int page;
    private Context context;
    private StudyActivity activity;
    private Random rand;

    private DatabaseHandler db;
    private ArrayList<Card> cards;

    private TextView tv;
    private Animation fadeIn;

    // newInstance constructor for creating fragment with arguments
    public static CardFront newInstance(int page, String title) {
        CardFront instance = new CardFront();
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
        context = activity.getApplicationContext();
        rand = new Random();

        //Get all cards in this deck
        db = new DatabaseHandler(context);
        cards = db.getAllCards(activity.deck.getId());
        db.close();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isResumed()) {
            if (isVisibleToUser) {
                onResume();
            } else {
                tv.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) return;
        if (cards.size() > 1) {
            activity.currentCard = cards.get(randInt(1, cards.size()));
        } else {
            activity.currentCard = cards.get(0);
        }
        tv.setText(activity.currentCard.getFront());
        tv.startAnimation(fadeIn);
        tv.setVisibility(View.VISIBLE);
    }

    private int randInt(int min, int max) {
        return rand.nextInt(max - min) + min;
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_front, container, false);

        tv = (TextView) view.findViewById(R.id.study_card_front);

        fadeIn = AnimationUtils.loadAnimation(context,
                R.anim.fadein);

        return view;
    }

}
