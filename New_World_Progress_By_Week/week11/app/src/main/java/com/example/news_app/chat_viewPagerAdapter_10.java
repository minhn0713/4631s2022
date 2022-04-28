package com.example.news_app;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class chat_viewPagerAdapter_10 extends FragmentStateAdapter {

    //2.2) create array to display tap
    private String[] titles = new String[]{"Messages"};

    public chat_viewPagerAdapter_10(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new chat_chatFragment();
        }
        return new chat_chatFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
