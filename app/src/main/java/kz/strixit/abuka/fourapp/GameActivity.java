package kz.strixit.abuka.fourapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.strixit.abuka.fourapp.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "Game Activity";
    private ArrayList<Fragment> fragments;
    private ViewPager viewPager;
    private TextView letter1, letter2, letter3, letter4, matchingNum;
    private CirclePageIndicator circlePageIndicator;
    private ArrayList<String> historyWords;
    private Button backArrowButton, tutorialButton;
    private String secretWord;
    private ArrayList<String> challangeDictionary;
    private static CountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        challangeDictionary = new ArrayList<String>(Arrays.asList(getResources().getString(R.string.challangeDictionary).split(" ")));
        Random r = new Random();
        secretWord = challangeDictionary.get(r.nextInt(challangeDictionary.size()));

        backArrowButton = (Button) findViewById(R.id.backArrowButton);
        tutorialButton = (Button) findViewById(R.id.tutorialButton);

        matchingNum = (TextView) findViewById(R.id.matchingNum);
        //stepsTextView = (TextView) findViewById(R.id.stepsTextView);
        letter1 = (TextView) findViewById(R.id.letter1);
        letter2 = (TextView) findViewById(R.id.letter2);
        letter3 = (TextView) findViewById(R.id.letter3);
        letter4 = (TextView) findViewById(R.id.letter4);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);

        historyWords = new ArrayList<String>();

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fragments = new ArrayList<>();
        fragments.add(KeyboardFragment.newInstance());
        fragments.add(HistoryFragment.newInstace());

        final KeyboardAdapter adapter = new KeyboardAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        circlePageIndicator.setViewPager(viewPager);
        startTime();

        circlePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                FragmentLifecycle fragmentToShow = (FragmentLifecycle) adapter.getItem(position);
                fragmentToShow.onResumeFragment();

                FragmentLifecycle fragmentToHide = (FragmentLifecycle) adapter.getItem(currentPosition);
                fragmentToHide.onPauseFragment();

                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        letter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                letter1.setText("");
                YoYo.with(Techniques.RubberBand).duration(300).playOn(findViewById(R.id.letter1));
            }
        });
        letter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                letter2.setText("");
                YoYo.with(Techniques.RubberBand).duration(300).playOn(findViewById(R.id.letter2));

            }
        });
        letter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                letter3.setText("");
                YoYo.with(Techniques.RubberBand).duration(300).playOn(findViewById(R.id.letter3));
            }
        });
        letter4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                letter4.setText("");
                YoYo.with(Techniques.RubberBand).duration(300).playOn(findViewById(R.id.letter4));
            }
        });

        backArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveUp();
            }
        });

        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTutorial();
            }
        });



    }

    public void addHistoryWords(String s){
        historyWords.add(s);
    }
    public ArrayList<String> getHistoryWords(){
        return historyWords;
    }

    public void startTutorial(){

        YoYo.with(Techniques.Pulse).duration(200).playOn(tutorialButton);
        Intent intent = new Intent(GameActivity.this,TutorialActivity.class);
        startActivityForResult(intent, 1);
    }

    public void inccorectWordAnimation(){
        YoYo.with(Techniques.TakingOff).duration(3000).playOn(findViewById(R.id.matchingText));
        YoYo.with(Techniques.Shake).playOn(findViewById(R.id.letter1));
        YoYo.with(Techniques.Shake).playOn(findViewById(R.id.letter2));
        YoYo.with(Techniques.Shake).playOn(findViewById(R.id.letter3));
        YoYo.with(Techniques.Shake).playOn(findViewById(R.id.letter4));
    }

    public void setLetterAnimation(int letterId){
        YoYo.with(Techniques.Pulse).duration(300).playOn(findViewById(letterId));
    }

    public void correctWordAnimation(){
        matchingNum.setVisibility(View.VISIBLE);
        //YoYo.with(Techniques.TakingOff).playOn(findViewById(R.id.letter1));
        //YoYo.with(Techniques.TakingOff).playOn(findViewById(R.id.letter2));
        //YoYo.with(Techniques.TakingOff).playOn(findViewById(R.id.letter3));
        //YoYo.with(Techniques.TakingOff).playOn(findViewById(R.id.letter4));
        //YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.matchingNum));
        YoYo.with(Techniques.TakingOff).duration(3000).playOn(findViewById(R.id.matchingNum));
        YoYo.with(Techniques.TakingOff).duration(3000).playOn(findViewById(R.id.matchingText));
        letter1.setText("");
        letter2.setText("");
        letter3.setText("");
        letter4.setText("");
//        YoYo.with(Techniques.FadeIn).playOn(findViewById(R.id.letter1));
//        YoYo.with(Techniques.FadeIn).playOn(findViewById(R.id.letter2));
//        YoYo.with(Techniques.FadeIn).playOn(findViewById(R.id.letter3));
//        YoYo.with(Techniques.FadeIn).playOn(findViewById(R.id.letter4));
    }

    public void onBackPressed(){
        giveUp();
        //YoYo.with(Techniques.Pulse).duration(200).playOn(backArrowButton);
    }

    private void giveUp() {
        new AlertDialog.Builder(this)
                //.setTitle("Title")
                .setMessage("Хотите сдаться?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameEnd();
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                //.setIcon(R.drawable.)
                .show();
    }

    public String getSecretWord(){
        return secretWord;
    }

    public void gameEnd(){
        timer.cancel();
        Intent intent = new Intent(GameActivity.this, ResultActivity.class);
        intent.putExtra("steps", 0);
        intent.putExtra("secretWord",secretWord);
        intent.putExtra("result", 0);
        finish();
        startActivity(intent);
    }

    public void startTime(){
        final int[] sec = {180};
        final TextView stepsTextView = (TextView) findViewById(R.id.stepsTextView);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(sec[0]);
        timer = new CountDownTimer((sec[0]+2)*1000,1000) {
            int t=0;
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(sec[0]);
                stepsTextView.setText(Integer.toString(sec[0]));
                sec[0] -= 1;
                t++;
            }
            @Override
            public void onFinish() {
                gameEnd();
            }
        };
        timer.start();
    }

    public void stopTimer(){
        timer.cancel();
    }
}
