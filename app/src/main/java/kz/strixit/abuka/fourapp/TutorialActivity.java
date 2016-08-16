package kz.strixit.abuka.fourapp;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.strixit.abuka.fourapp.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

public class TutorialActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private ArrayList<Fragment> fragments;
    private CirclePageIndicator circlePageIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        fragments = new ArrayList<>();
        fragments.add(TutorialPageFragment.newInstance(1));
        fragments.add(TutorialPageFragment.newInstance(2));

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);

        pagerAdapter = new TutorialAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);
        circlePageIndicator.setViewPager(viewPager);

    }

    public void setCurrentItem (int item, boolean smoothScroll) {
        viewPager.setCurrentItem(item, smoothScroll);
    }


}
