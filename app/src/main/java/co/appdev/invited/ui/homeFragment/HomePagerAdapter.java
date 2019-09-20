package co.appdev.invited.ui.homeFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import co.appdev.invited.ui.contactList.ContactListFragment;
import co.appdev.invited.ui.createEvent.CreateEventFragment;
import co.appdev.invited.ui.eventStatus.EventStatusFragment;

public class HomePagerAdapter extends FragmentStatePagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"My Lists", "Create Message", "Messages Status"};
    private Context context;

    public HomePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return ContactListFragment.newInstance();
        } else if (position == 1){
            return CreateEventFragment.newInstance();
        }
        return EventStatusFragment.newInstance();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}