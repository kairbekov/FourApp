package kz.strixit.abuka.fourapp;


import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.strixit.abuka.fourapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FbAuthFragment extends Fragment {


    private static final String TAG = "FbAuthFragment";
    private static final String SHARED_PREFES_FILE_NAME = "com.strixit.abuka.fourApp";
    private Button facebookButton;
    private SharedPreferences sharedPreferences;
    private TextView bestResult;

    public FbAuthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fb_auth, container, false);
        facebookButton = (Button) view.findViewById(R.id.facebookButton);
        bestResult = (TextView) view.findViewById(R.id.bestResult);
        bestResult.setText(convertToTime(((MainActivity)getActivity()).getBestResult()));

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFacebookButtonClick();
            }
        });

        return view;
    }

    private void onFacebookButtonClick() {
        Map<String, String> facebookFieldMapping = new HashMap<String, String>();
        facebookFieldMapping.put("email", "email");
        facebookFieldMapping.put("first_name", "fb_first_name");
        facebookFieldMapping.put("last_name", "fb_last_name");
        //facebookFieldMapping.put("location.name", "country");
        List<String> permissions = new ArrayList<String>();
        permissions.add("email");
        permissions.add("public_profile");
        //permissions.add("user_location");

        Backendless.UserService.loginWithFacebookSdk(getActivity(), facebookFieldMapping, permissions, ((MainActivity) getActivity()).getCallbackManager(), new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                //Log.d(TAG,"Loged in success "+response.toString());
                //usernameTextView.setText(response.getProperty("fb_first_name") + " " + response.getProperty("fb_last_name"));
                //facebookButton.setVisibility(View.INVISIBLE);

                saveNewUserData(response);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentLayout, ((MainActivity) getActivity()).getLeaderboardFragment());
                fragmentTransaction.commit();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "facebook login failed");
            }
        }, true);
    }

    private void saveNewUserData(BackendlessUser response) {
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putBoolean("isUserAuth", true);
        editor.putInt("bestResult",0);
        editor.commit();

        GameStats gameStats = new GameStats();
        gameStats.setUser(response);
        gameStats.setBest_result(0);
        gameStats.setGames(0);
        gameStats.setSteps(0);
        gameStats.setPoints(0);
        Backendless.Persistence.save(gameStats, new AsyncCallback<GameStats>() {
            @Override
            public void handleResponse(GameStats response) {
                Log.d(TAG, "Saved success");
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d(TAG, "save failed");
            }
        });
    }

    String convertToTime(int sec){
        String text = sec+" сек.";
        if (sec>=60){
            text = sec/60+"."+sec%60+" мин";
        }
        return text;
    }


}
