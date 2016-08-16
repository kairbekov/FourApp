package kz.strixit.abuka.fourapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.strixit.abuka.fourapp.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements FragmentLifecycle{

    private static final String TAG = "History Fragment";
    private GridView gridView;

    private String[] historyWords = {};
    private ArrayList<String> data;
    private ArrayAdapter<String> historyAdapter;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        gridView = (GridView) view.findViewById(R.id.gridView);
        adjustGridView();

        return view;
    }

    private void adjustGridView() {
        gridView.setNumColumns(3);
        gridView.setVerticalSpacing(10);
        //gridView.setHorizontalSpacing(20);
    }

    public static HistoryFragment newInstace(){
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }


    @Override
    public void onPauseFragment() {
        Log.d(TAG, "onPause");
    }

    @Override
    public void onResumeFragment() {
        Log.d(TAG, "onResume");
        data = ((GameActivity)getActivity()).getHistoryWords();
        Log.d(TAG, data.toString());
        historyWords = new String[data.size()];
        for(int i=0; i<data.size(); i++){
            historyWords[i]=data.get(i);
        }
        historyWords = data.toArray(historyWords);
        historyAdapter = new ArrayAdapter<String>(getActivity(), R.layout.grid_view_item, R.id.historyTextView, historyWords);
        gridView.setAdapter(historyAdapter);
    }
}
