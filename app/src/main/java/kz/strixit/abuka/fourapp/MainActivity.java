package kz.strixit.abuka.fourapp;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.strixit.abuka.fourapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String SHARED_PREFES_FILE_NAME = "com.strixit.abuka.fourApp";
    private ImageView progressBar;
    private Button playButton;
    private View statusBar;
    private FbAuthFragment fbAuthFragment;
    private MainPageFragment mainPageFragment;
    private FragmentTransaction fragmentTransaction;
    private RankingFragment rankingFragment;
    private CallbackManager callbackManager;
    private SharedPreferences sharedPreferences;
    private boolean isUserAuth, isFirstLogin;
    private int bestResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Animation a = AnimationUtils.loadAnimation(this, R.anim.progress_anime);
        progressBar = (ImageView) findViewById(R.id.progressBar);
        playButton = (Button) findViewById(R.id.playButton);
        //facebookButton = (Button) findViewById(R.id.facebookButton);
        statusBar = (View) findViewById(R.id.statusBarBackground);

        setStatusBarColor(statusBar, getResources().getColor(R.color.colorBackgraoundMainMenu));

        fbAuthFragment = new FbAuthFragment();
        mainPageFragment = new MainPageFragment();
        rankingFragment = new RankingFragment();

        getFragmentManager().beginTransaction().add(R.id.fragmentLayout, mainPageFragment).commit();

        //a.setDuration(500);
        //progressBar.startAnimation(a);

        Backendless.initApp(this, Const.APP_ID, Const.ANDROID_KEY, Const.VERSION);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        sharedPreferences = getSharedPreferences(SHARED_PREFES_FILE_NAME, Context.MODE_PRIVATE);
        isUserAuth = sharedPreferences.getBoolean("isUserAuth",false);
        bestResult = sharedPreferences.getInt("bestResult", 0);
        isFirstLogin = sharedPreferences.getBoolean("isFirstLogin", true);
        if (isFirstLogin){
            startTutorial();
        }
        setFragment();

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayButtonClick();
            }
        });

    }

    public void setStatusBarColor(View statusBar,int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //status bar height
            //action bar height
            statusBar.setBackgroundColor(color);
        }
    }

    public void setFragment() {
        fragmentTransaction = getFragmentManager().beginTransaction();
        Backendless.UserService.CurrentUser();
        if (haveNetworkConnection()){
            //Backendless.UserService.setCurrentUser();
            if (Backendless.UserService.CurrentUser() != null) {
                Log.d(TAG, "Valid Login");
                fragmentTransaction.replace(R.id.fragmentLayout, rankingFragment);
                fragmentTransaction.commit();
            }
            else if (Backendless.UserService.CurrentUser() == null && isUserAuth){
                Log.d(TAG,"Invalid login but UserAuth");
                fastUserAuth();
            }
            else{
                Log.d(TAG, "Invalid login");
                fragmentTransaction.replace(R.id.fragmentLayout, fbAuthFragment);
                fragmentTransaction.commit();
            }
        }
        else {
            Log.d(TAG, "NO internet");
            fragmentTransaction.replace(R.id.fragmentLayout, mainPageFragment);
            fragmentTransaction.commit();
        }

    }


    private void onPlayButtonClick() {

        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        MainActivity.this.startActivity(intent);
    }

    private boolean haveNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = cm.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
    }

    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"onActivityResult");
    }

    public CallbackManager getCallbackManager(){
        return callbackManager;
    }

    public boolean isLoggedIn() {
        //AccessToken accessToken = AccessToken.getCurrentAccessToken();
        //return accessToken != null;
        return AccessToken.getCurrentAccessToken() != null;
    }

    public RankingFragment getLeaderboardFragment(){
        return rankingFragment;
    }

    public void fastUserAuth(){


        Map<String, String> facebookFieldMapping = new HashMap<String, String>();
        facebookFieldMapping.put("email", "email");
        facebookFieldMapping.put("first_name", "fb_first_name");
        facebookFieldMapping.put("last_name", "fb_last_name");
        List<String> permissions = new ArrayList<String>();
        permissions.add("email");
        permissions.add("public_profile");

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLayout, getLeaderboardFragment());
        fragmentTransaction.commit();

        // Progress bar visible

        Backendless.UserService.loginWithFacebookSdk(this, facebookFieldMapping, permissions, callbackManager, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                Log.d(TAG,"Fast User Log in success");
                //usernameTextView.setText(response.getProperty("fb_first_name") + " " + response.getProperty("fb_last_name"));
                //facebookButton.setVisibility(View.INVISIBLE);

//                sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFES_FILE_NAME, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.clear();
//                editor.putBoolean("isUserAuth", true);

                //saveNewUserData(response);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "Fast User Auth log in failed");
            }
        }, true);
    }

    public int getBestResult(){
        return bestResult;
    }

    public void startTutorial(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.clear();
        editor.putBoolean("isFirstLogin",false);
        editor.commit();
        Intent intent = new Intent(MainActivity.this,TutorialActivity.class);
        MainActivity.this.startActivity(intent);
    }



//    public void onBackPressed(){
//
//    }
}
