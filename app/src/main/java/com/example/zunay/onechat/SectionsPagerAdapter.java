package com.example.zunay.onechat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by zunay on 11/19/2017.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                RequestsFragment requestsFragment= new RequestsFragment();
                return requestsFragment;
            case 1:
                ChatsFragment chatsFragment= new ChatsFragment();
                return chatsFragment;
            case 2:
                FriendsFragment friendsFragment= new FriendsFragment();
                return friendsFragment;
                default:
                    return null;
        }
    }
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Request";

            case 1:
                return "Chats";

            case 2:
                return "Friends";

                default: return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
