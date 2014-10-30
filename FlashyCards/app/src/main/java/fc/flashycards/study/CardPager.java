package fc.flashycards.study;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * This class translates fragment layouts into pages
 * for the Study activity to flip between. The pager
 * contains two pages, the front and back of the current
 * flash card.
 */
public class CardPager extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;

    public CardPager(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show the card front
                return CardFront.newInstance(0, "Front");
            case 1: // Fragment # 1 - This will show the card back
                return CardBack.newInstance(1, "Back");
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show the card front
                return "Front";
            case 1: // Fragment # 1 - This will show the card back
                return "Back";
            default:
                return null;
        }
    }

}