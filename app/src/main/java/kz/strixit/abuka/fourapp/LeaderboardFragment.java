package kz.strixit.abuka.fourapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.strixit.abuka.fourapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderboardFragment extends Fragment {

    private static final String TAG = "LeaderboardFragment";
    private static final String SHARED_PREFES_FILE_NAME = "com.strixit.abuka.fourApp";
    private ListView listView;
    private TextView myPlaceTextView, myRecordTextView;

    public LeaderboardFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        listView = (ListView) view.findViewById(R.id.leaderboardListView);
        myPlaceTextView = (TextView) view.findViewById(R.id.myPlaceTextView);
        myRecordTextView = (TextView) view.findViewById(R.id.myRecordTextView);

        Map<String, String> facebookFieldMapping = new HashMap<String, String>();
        facebookFieldMapping.put("email", "email");
        facebookFieldMapping.put("first_name", "fb_first_name");
        facebookFieldMapping.put("last_name", "fb_last_name");
        List<String> permissions = new ArrayList<String>();
        permissions.add("email");
        permissions.add("public_profile");

        Backendless.UserService.loginWithFacebookSdk(getActivity(), facebookFieldMapping, permissions, ((MainActivity)getActivity()).getCallbackManager(), new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                Log.d(TAG, "Fast User Log in success");
                setListView();
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


        return view;
    }

    private void setListView() {
        //BackendlessUser user = Backendless.UserService.CurrentUser();
        String whereClause = "points > 0";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);

        QueryOptions queryOptions = new QueryOptions();
        queryOptions.addRelated("user");
        queryOptions.addSortByOption("points DESC");
        queryOptions.setPageSize(100);
        dataQuery.setQueryOptions(queryOptions);

        final ArrayList<GameStats> gameStatses = new ArrayList<GameStats>();
        final ArrayList<Integer> position = new ArrayList<>();
        final int[] myPos = new int[1];
        Backendless.Persistence.of(GameStats.class).find(dataQuery, new AsyncCallback<BackendlessCollection<GameStats>>() {
            @Override
            public void handleResponse(BackendlessCollection<GameStats> response) {
                Log.d(TAG, "Users finded");
                ArrayList<GameStats> gs = (ArrayList<GameStats>) response.getData();
                boolean ok = false;
                for (int i = 0; i < gs.size(); i++) {
                    //Log.d(TAG, gs.get(i).getUser().getProperty("fb_first_name") + " " + gs.get(i).getPoints());
                    if (i < 30) {
                        if (Backendless.UserService.CurrentUser().getObjectId().equals(gs.get(i).getUser().getObjectId())) {
                            ok = true;
                            gameStatses.add(gs.get(i));
                            position.add((i + 1) * -1);
                            myPos[0] = i + 1;
                            Log.d(TAG, "My position " + myPos[0] + " Negative " + (i + 1) * -1);
                        } else {
                            gameStatses.add(gs.get(i));
                            position.add(i + 1);
                            //Log.d(TAG, "Curren User objectId=" + Backendless.UserService.CurrentUser().getObjectId());
                            //Log.d(TAG,"Iterated User objectId="+gs.get(i).getUser().getObjectId());
                        }

                    } else if (Backendless.UserService.CurrentUser().getObjectId().equals(gs.get(i).getUser().getObjectId()) && !ok) {
                        gameStatses.add(gs.get(i));
                        position.add((i + 1) * -1);
                        myPos[0] = i + 1;
                        Log.d(TAG, "else " + (i + 1) * -1);
                    }
                }
                LeaderboardAdapter adapter = new LeaderboardAdapter((MainActivity) getActivity(), gameStatses, position);
                listView.setAdapter(adapter);
                setBottomStats(myPos[0]);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d(TAG, "error " + fault.toString());
            }
        });

    }


    public void setBottomStats(int pos) {
        Log.d(TAG, "MyPosition in func "+pos);
        myPlaceTextView.setText(getString(R.string.my_place)+" "+pos);
        myRecordTextView.setText(getString(R.string.my_record) + " " + ((MainActivity) getActivity()).getBestResult());
    }

    public static LeaderboardFragment newInstance(){
        LeaderboardFragment fragment = new LeaderboardFragment();
        return fragment;
    }
}
