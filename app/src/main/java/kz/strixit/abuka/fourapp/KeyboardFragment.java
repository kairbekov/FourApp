package kz.strixit.abuka.fourapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.strixit.abuka.fourapp.R;

import java.util.ArrayList;
import java.util.Arrays;


public class KeyboardFragment extends Fragment implements FragmentLifecycle{

    private static final String TAG = "Keyboard Fragment";
    private Button[] buttons;
    private ArrayList<String> dictionary, challangeDictionary;
    private String secretWord;
    private int steps = 0;
    TextView letter1, letter2, letter3, letter4;

    public KeyboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_keyboard, container, false);
        dictionary = new ArrayList<String>(Arrays.asList(getResources().getString(R.string.dictionary).split(" ")));
        secretWord = ((GameActivity)getActivity()).getSecretWord();

        buttons = new Button[31];
        for (int i=0; i<31; i++){
            final int b = i;
            buttons[b] = (Button) view.findViewById(R.id.Q+b);
            //Log.d(TAG,(R.id.Q+b)+" "+buttons[b].getText());
            buttons[b].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d(TAG, (String) buttons[b].getText());
                    YoYo.with(Techniques.Pulse).duration(200).playOn(buttons[b]);
                    if (buttons[b].getId() == R.id.backSpaceButton){
                        onBackSpaceClick();
                    }
                    else {
                        keyboardOnClick((String) buttons[b].getText());
                    }
                }
            });
        }
        return view;
    }

    private void keyboardOnClick(String letter) {
        letter1 = (TextView) getActivity().findViewById(R.id.letter1);
        letter2 = (TextView) getActivity().findViewById(R.id.letter2);
        letter3 = (TextView) getActivity().findViewById(R.id.letter3);
        letter4 = (TextView) getActivity().findViewById(R.id.letter4);
        TextView matchingNum = (TextView) getActivity().findViewById(R.id.matchingNum);
        TextView matchingText = (TextView) getActivity().findViewById(R.id.matchingText);
        TextView letterX = null;
        if (letter1.getText().toString().equals("")) {
            letterX = letter1;
            ((GameActivity)getActivity()).setLetterAnimation(R.id.letter1);
        }
        else if (letter2.getText().toString().equals("")) {
            letterX = letter2;
            ((GameActivity)getActivity()).setLetterAnimation(R.id.letter2);
        }
        else if (letter3.getText().toString().equals("")) {
            letterX = letter3;
            ((GameActivity)getActivity()).setLetterAnimation(R.id.letter3);
        }
        else if (letter4.getText().toString().equals("")) {
            letterX = letter4;
            ((GameActivity)getActivity()).setLetterAnimation(R.id.letter4);
        }
        try {
            letterX.setText(letter);
        }
        catch (Exception e){
            Log.e(TAG,e.toString());
        }
        if (!letter1.getText().toString().isEmpty() && !letter2.getText().toString().isEmpty() &&
                !letter3.getText().toString().isEmpty() && !letter4.getText().toString().isEmpty()){
            String userWord = letter1.getText().toString()+letter2.getText().toString()+letter3.getText().toString()+letter4.getText().toString();
            int num = checkWord(userWord);
            if (num!=-1){
                steps++;
                //stepsTextView.setText(Integer.toString(steps) + " " + getString(R.string.steps));
                matchingNum.setTextSize(40);
                matchingNum.setText(Integer.toString(num));
                if (num==0) {
                    matchingText.setText(getResources().getString(R.string.zero_letter_same));
                }
                else if (num==1){
                    matchingText.setText(getResources().getString(R.string.one_letter_same));
                }
                else {
                    matchingText.setText(getResources().getString(R.string.more_letter_same));
                }
                ((GameActivity)getActivity()).addHistoryWords(userWord + "-" + num);
                ((GameActivity)getActivity()).correctWordAnimation();
            }
            else {
                matchingText.setText(getResources().getString(R.string.this_word_i_dont_know));
                ((GameActivity)getActivity()).inccorectWordAnimation();
                //Toast.makeText(getContext(),"Incorrect word",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private int checkWord(String userWord) {
        Log.d(TAG, secretWord);
        Log.d(TAG, userWord);
        boolean inDic = false;
        int cnt = 0;
        if (secretWord.equals(userWord)) { winGame(); inDic = true;}
        else {
            for (String str : dictionary) {
                if (str.equals(userWord)) {
                    inDic = true;
                    break;
                }
            }
        }
        if (inDic){
            cnt = calcMatching(userWord, secretWord);
            Log.d(TAG,Integer.toString(cnt));
        }
        else{
            cnt = -1;
        }
        return cnt;

    }

    private int calcMatching(String userWord, String botWord) {
        int res=0;
        StringBuilder newStr1 = new StringBuilder(userWord);
        StringBuilder newStr2 = new StringBuilder(botWord);
        for (int i=0; i<userWord.length(); i++){
            for (int j=0; j<botWord.length(); j++) {
                if (newStr1.charAt(i) == newStr2.charAt(j) && newStr1.charAt(i)!='@'){
                    newStr1.setCharAt(i, '@');
                    newStr2.setCharAt(j, '@');
                    res++;
                }
            }
        }
        return res;
    }

    private void winGame() {
        ((GameActivity)getActivity()).stopTimer();
        TextView stepsTextView = (TextView) getActivity().findViewById(R.id.stepsTextView);
        Intent intent = new Intent(getActivity(), ResultActivity.class);
        intent.putExtra("steps", steps);
        intent.putExtra("points", Integer.parseInt(stepsTextView.getText()+""));
        intent.putExtra("secretWord",secretWord);
        intent.putExtra("result", 1);
        getActivity().finish();
        startActivity(intent);
    }

    public static KeyboardFragment newInstance(){
        KeyboardFragment fragment = new KeyboardFragment();
        return fragment;
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }

    public void onBackSpaceClick(){
        if (!letter4.getText().toString().isEmpty()){
            YoYo.with(Techniques.RubberBand).duration(300).playOn(letter4);
            letter4.setText("");
        }
        else if (!letter3.getText().toString().isEmpty()){
            YoYo.with(Techniques.RubberBand).duration(300).playOn(letter3);
            letter3.setText("");
        }
        else if (!letter2.getText().toString().isEmpty()){
            YoYo.with(Techniques.RubberBand).duration(300).playOn(letter2);
            letter2.setText("");
        }
        else if (!letter1.getText().toString().isEmpty()){
            YoYo.with(Techniques.RubberBand).duration(300).playOn(letter1);
            letter1.setText("");
        }
    }
}
