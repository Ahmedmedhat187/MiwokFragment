package com.example.android.miwokfragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class CategoryAdapter extends FragmentPagerAdapter {

    Context context;

    public CategoryAdapter(Context context ,FragmentManager fm) {
        super(fm);
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new FragmentNumbers();
        }
        else if(position == 1){
            return new FragmentFamily();
        }
        else if(position == 2){
            return new FragmentColors();
        }
        else{
            return new FragmentPhrases();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return context.getString(R.string.category_numbers);
        }
        else if(position == 1){
            return context.getString(R.string.category_family);
        }
        else if(position == 2){
            return context.getString(R.string.category_colors);
        }
        else{
            return context.getString(R.string.category_phrases);
        }
    }
}





