package kz.strixit.abuka.fourapp;

import com.backendless.BackendlessUser;

/**
 * Created by abuka on 15.07.2016.
 */
public class GameStats {
    private String objectId;
    private int best_result;
    private int games;
    private int steps;
    private BackendlessUser user;
    private double points;


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getBest_result() {
        return best_result;
    }

    public void setBest_result(int best_result) {
        this.best_result = best_result;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public BackendlessUser getUser() {
        return user;
    }

    public void setUser(BackendlessUser user) {
        this.user = user;
    }

    public double getPoints(){
        return points;
    }

    public void setPoints(double points){
        this.points = points;
    }
}
