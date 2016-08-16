package kz.strixit.abuka.fourapp;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.strixit.abuka.fourapp.R;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RankingFragment extends Fragment {

    private ViewPager viewPager;
    private ArrayList<android.support.v4.app.Fragment> fragments;
    private Button globalButton, countryButton;

    public RankingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
//        globalButton = (Button) view.findViewById(R.id.globalButton);
//        countryButton = (Button) view.findViewById(R.id.countryButton);

        fragments = new ArrayList<>();
        fragments.add(LeaderboardFragment.newInstance());
//        fragments.add(CountryBoardFragment.newInstance());

        RankingAdapter adapter = new RankingAdapter(((MainActivity)getActivity()).getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

//        globalButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                globalButtonClick();
//            }
//        });
//        countryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                countryButtonClick();
//            }
//        });

        return view;

    }

    private void globalButtonClick() {
        viewPager.setCurrentItem(0, true);
    }

    private void countryButtonClick() {
        viewPager.setCurrentItem(1, true);
    }

}
