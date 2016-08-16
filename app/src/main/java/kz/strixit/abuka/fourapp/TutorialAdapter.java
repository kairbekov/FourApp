package kz.strixit.abuka.fourapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by abuka on 20.07.2016.
 */
public class TutorialAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> fragmentArrayList;

    public TutorialAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragments){
        super(fragmentManager);
        this.fragmentArrayList = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }
}
