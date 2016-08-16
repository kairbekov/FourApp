package kz.strixit.abuka.fourapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.backendless.BackendlessUser;
import com.strixit.abuka.fourapp.R;

import java.util.ArrayList;

/**
 * Created by abuka on 17.07.2016.
 */
public class LeaderboardAdapter extends BaseAdapter {

    Context context;
    private ArrayList<GameStats> gameStats;
    private ArrayList<Integer> positions;
    private LayoutInflater inflater;

    public LeaderboardAdapter(Context context, ArrayList<GameStats> gameStats, ArrayList<Integer> positions){
        this.context = context;
        this.gameStats = gameStats;
        this.positions = positions;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return gameStats.size();
    }

    @Override
    public Object getItem(int position) {
        return gameStats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null){
            convertView = inflater.inflate(R.layout.raw_leaderboard_list_view_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BackendlessUser user = gameStats.get(position).getUser();

        viewHolder.numberTextView.setText(Integer.toString(positions.get(position)));
        viewHolder.nameTextView.setText(user.getProperty("fb_first_name")+" "+user.getProperty("fb_last_name"));
        viewHolder.stepsTextView.setText(Math.round(gameStats.get(position).getPoints()) + "");

        if (positions.get(position)< 0){
            viewHolder.numberTextView.setText(Integer.toString(positions.get(position)*-1));
            viewHolder.numberTextView.setTextColor(context.getResources().getColor(R.color.history_item));
            viewHolder.nameTextView.setTextColor(context.getResources().getColor(R.color.history_item));
            viewHolder.stepsTextView.setTextColor(context.getResources().getColor(R.color.history_item));
            //Log.d("LeaderboardAdapter", "-position");
        }
        else{
            viewHolder.numberTextView.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.nameTextView.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.stepsTextView.setTextColor(context.getResources().getColor(R.color.white));
        }

        return convertView;
    }

    private class ViewHolder{
        TextView numberTextView, nameTextView, stepsTextView;

        public ViewHolder(View v){
            numberTextView = (TextView) v.findViewById(R.id.numberTextView);
            nameTextView = (TextView) v.findViewById(R.id.nameTextView);
            stepsTextView = (TextView) v.findViewById(R.id.stepsTextView);
        }
    }
}

