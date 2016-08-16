package kz.strixit.abuka.fourapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.strixit.abuka.fourapp.R;

import java.io.File;
import java.io.FileOutputStream;

public class ResultActivity extends AppCompatActivity {

    private static final String TAG = "Result Activity";
    private static final String SHARED_PREFES_FILE_NAME = "com.strixit.abuka.fourApp";
    private TextView letter1, letter2, letter3, letter4, stepsTextView, status_text, stepText;
    private ImageButton shareButton, newGameButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        Intent intent = getIntent();
        final int steps = intent.getIntExtra("steps", 0);
        final int points = intent.getIntExtra("points", 0);
        String secretWord = intent.getStringExtra("secretWord");
        int result = intent.getIntExtra("result", 0);
        final int res = result;


        letter1 = (TextView) findViewById(R.id.letter1);
        letter2 = (TextView) findViewById(R.id.letter2);
        letter3 = (TextView) findViewById(R.id.letter3);
        letter4 = (TextView) findViewById(R.id.letter4);
        stepsTextView = (TextView) findViewById(R.id.stepsTextView);
        status_text = (TextView) findViewById(R.id.status_text);
        stepText = (TextView) findViewById(R.id.stepText);
        shareButton = (ImageButton) findViewById(R.id.shareButton);
        newGameButton = (ImageButton) findViewById(R.id.newGameButton);

        letter1.setText(secretWord.charAt(0)+"");
        letter2.setText(secretWord.charAt(1)+"");
        letter3.setText(secretWord.charAt(2)+"");
        letter4.setText(secretWord.charAt(3)+"");

        stepText.setText(getConverText(180-points));

        sharedPreferences = getSharedPreferences(SHARED_PREFES_FILE_NAME, Context.MODE_PRIVATE);

        if(result == 0) {
            stepsTextView.setText(Integer.toString(180-points));
            stepsTextView.setVisibility(View.INVISIBLE);
            stepText.setVisibility(View.INVISIBLE);
            status_text.setText(getString(R.string.you_lose));
        }
        else {
            int best = sharedPreferences.getInt("bestResult",0);

            if(best>steps || best==0){
                stepsTextView.setText(Integer.toString(180-points));
                stepsTextView.setVisibility(View.VISIBLE);
                stepText.setVisibility(View.VISIBLE);
                status_text.setText(getString(R.string.your_result));
            }
            else{
                stepsTextView.setText(Integer.toString(180-points));
                stepsTextView.setVisibility(View.VISIBLE);
                stepText.setVisibility(View.VISIBLE);
                status_text.setText(getString(R.string.your_record));
            }
            try {
                updateUserData(steps, points);
            }
            catch (Exception e){
                Log.e(TAG,e.toString());
            }
        }

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShareButtonClick(points, res);
            }
        });

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewGameButtonlick();
            }
        });

        letter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.RubberBand).duration(300).playOn(findViewById(R.id.letter1));
            }
        });
        letter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.RubberBand).duration(300).playOn(findViewById(R.id.letter2));

            }
        });
        letter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.RubberBand).duration(300).playOn(findViewById(R.id.letter3));
            }
        });
        letter4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.RubberBand).duration(300).playOn(findViewById(R.id.letter4));
            }
        });
    }

    private void updateUserData(final int steps, final int points) {
        bestResultUpdate(180-points);
        BackendlessUser user = Backendless.UserService.CurrentUser();
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        Log.e(TAG,user+" ");
        String whereClause = "user.objectId = '" + user.getObjectId() + "'";
        dataQuery.setWhereClause(whereClause);
        Log.d(TAG, user.getObjectId() + "");
        Backendless.Data.of(GameStats.class).find(dataQuery, new AsyncCallback<BackendlessCollection<GameStats>>() {
            @Override
            public void handleResponse(BackendlessCollection<GameStats> response) {
                GameStats gameStats = (GameStats) response.getCurrentPage().get(0);
                gameStats.setSteps(gameStats.getSteps()+steps);
                gameStats.setGames(gameStats.getGames() + 1);
                //gameStats.setPoints((gameStats.getSteps()*1.0/gameStats.getGames())*1.0);
                gameStats.setPoints(gameStats.getPoints()+points);
                gameStats.setBest_result((gameStats.getBest_result()>(180-points) || gameStats.getBest_result()==0)?(180-points):gameStats.getBest_result());
                bestResultUpdate(gameStats.getBest_result());
                Backendless.Persistence.save(gameStats, new AsyncCallback<GameStats>() {
                    @Override
                    public void handleResponse(GameStats response) {
                        Log.d(TAG, "updated success");
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.e(TAG, "failed " + fault.toString());
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "failed " + fault.toString());
            }
        });
    }


    private void onShareButtonClick(int steps, int res) {
        //Date now = new Date();
        //android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {

            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + "share.jpg";

            //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.logo_final);
            Bitmap bm;
            if (res==0){
                bm = BitmapFactory.decodeResource(getResources(), R.drawable.share_image_lose);
            }
            else {
                String text = "сек.";
                bm = drawTextToBitmap(this, R.drawable.share_image, convertToTime(180-steps));
            }
            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bm.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/*");
            Uri uri = Uri.fromFile(imageFile);
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.putExtra(Intent.EXTRA_TEXT, "bit.ly/get-four");
            //share.setPackage("com.instagram.android");
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //startActivity(share);
            ResultActivity.this.startActivity(share);

        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            //Log.e(TAG,e.toString());
            e.printStackTrace();
        }
    }

    private void onNewGameButtonlick() {
        Log.d(TAG, "Share pressed");
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        //finish();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    public void bestResultUpdate(int bestResult){
        //sharedPreferences = getSharedPreferences(SHARED_PREFES_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.clear();
        int currentBest = sharedPreferences.getInt("bestResult",0);
        if (currentBest==0 || currentBest>bestResult){
            currentBest = bestResult;
        }
        editor.putInt("bestResult", currentBest);
        editor.commit();
    }

    public Bitmap drawTextToBitmap(Context gContext, int gResId, String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);

        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(255, 255, 255));
        // text size in pixels
        paint.setTextSize((int) (150 * scale));
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
        // set font
        Typeface font = Typeface.createFromAsset(getAssets(),"helvetica_neue_thin.ttf");
        paint.setTypeface(font);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width())/2;
        int y = (bitmap.getHeight() + bounds.height())/2;

        canvas.drawText(gText, x, y, paint);

        return bitmap;
    }

    String getConverText(int steps){
        String text = "0";
        //Log.d(TAG,steps+"");
        if (steps>4 && steps<20) text = getResources().getString(R.string.more_steps);
        else if (steps%10==1) text = getResources().getString(R.string.one_steps);
        else if (steps%10==2 || steps%10==3 || steps%10==4) text = getResources().getString(R.string.two__three_four_steps);
        else text = getResources().getString(R.string.more_steps);
        return text;
    }

    String convertToTime(int sec){
        String text = sec+" сек.";
        if (sec>=60){
            text = sec/60+" мин. "+sec%60+" сек. ";
        }
        return text;
    }
}