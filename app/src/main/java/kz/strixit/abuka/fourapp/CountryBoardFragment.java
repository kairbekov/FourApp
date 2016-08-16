package kz.strixit.abuka.fourapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.strixit.abuka.fourapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CountryBoardFragment extends Fragment {

    private ListView listView;

    public CountryBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_country_board, container, false);
        listView = (ListView) view.findViewById(R.id.leaderboardListView);

        return view;
    }

    public static CountryBoardFragment newInstance(){
        CountryBoardFragment fragment = new CountryBoardFragment();
        return fragment;
    }


}
