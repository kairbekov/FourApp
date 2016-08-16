package kz.strixit.abuka.fourapp;


import android.graphics.Typeface;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.strixit.abuka.fourapp.R;


public class TutorialPageFragment extends Fragment {

    private int pageNum;
    private Button button;

    public TutorialPageFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutorial_page, container, false);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        Button button = (Button) view.findViewById(R.id.button);
        if (pageNum==1){
            Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "helvetica_neue_thin.ttf");
            textView.setTypeface(face);
            textView.setText(R.string.tutorial_page1);
            button.setText(R.string.next_button);
        }
        else{
            Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "helvetica_neue_thin.ttf");
            textView.setTypeface(face);
            textView.setText(R.string.tutorial_page2);
            button.setText(R.string.start_button);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageNum==1){
                    page1ButtonClick();
                }
                else{
                    page2ButtonClick();
                }
            }
        });
        return view;
    }

    private void page1ButtonClick() {
        ((TutorialActivity)getActivity()).setCurrentItem(2, true);
    }

    private void page2ButtonClick() {
        ((TutorialActivity)getActivity()).finish();
    }


    public static TutorialPageFragment newInstance(int pageNumber){
        TutorialPageFragment f = new TutorialPageFragment();
        Bundle args = new Bundle();
        args.putInt("pageNum", pageNumber);
        f.setArguments(args);
        return f;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        pageNum = getArguments() != null ? getArguments().getInt("pageNum") : -1;
    }


}
