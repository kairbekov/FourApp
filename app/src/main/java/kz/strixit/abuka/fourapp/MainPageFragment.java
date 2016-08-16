package kz.strixit.abuka.fourapp;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.strixit.abuka.fourapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainPageFragment extends Fragment {


    private TextView bestResult;

    public MainPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
        bestResult = (TextView) view.findViewById(R.id.bestResult);
        bestResult.setText(convertToTime(((MainActivity)getActivity()).getBestResult()));

        return view;
    }

    String convertToTime(int sec){
        String text = sec+" сек.";
        if (sec>=60){
            text = sec/60+"."+sec%60+" мин.";
        }
        return text;
    }

}
