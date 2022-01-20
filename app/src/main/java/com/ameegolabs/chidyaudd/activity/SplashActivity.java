package com.ameegolabs.chidyaudd.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.ameegolabs.chidyaudd.DBHelper;
import com.ameegolabs.chidyaudd.LocalDB;
import com.ameegolabs.chidyaudd.MyMediaPlayer;
import com.ameegolabs.chidyaudd.MyUtils;
import com.ameegolabs.chidyaudd.R;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 1000 * 4;

    //UI Variables
    private ImageView imageViewRunway;
    private ScrollView scrollView;
    private LinearLayout linearLayoutSlidingPanel;

    //Class variables
    private MyMediaPlayer player = MyMediaPlayer.getInstance();
    private Handler handler;
    private Runnable runnable;
    private Context context;
    private int height;
    private LocalDB localDB;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        linkUIVariables();
        context = SplashActivity.this;
        localDB = new LocalDB(context);
        dbHelper = new DBHelper(context);
        setLanguage();
        addWordsToDatabase();

        //adjust the runway size to match full height of the device
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;//782
        imageViewRunway.setMinimumHeight(height);
        player.mp = MediaPlayer.create(getApplicationContext(), R.raw.theme);
        player.mp.start();
        player.isPlaying = true;
        /*
            int height2 = displaymetrics.densityDpi;
            int width = displaymetrics.widthPixels;
            Log.d("mylog", "height=" + height2);
        */

        //disable scroll so as to stop user in moving the pane
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        animatePlane();
        splashAndGoNext();
    }

    private void setLanguage() {
        String language = localDB.getDefaultLanguage();
        MyUtils.logThis("default language is " + language);
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);
    }

    private void addWordsToDatabase() {
        if (!localDB.isWordsAdded()) {

            Intent i = new Intent(SplashActivity.this, LanguageActivity.class);
            startActivity(i);

            dbHelper.addFlyingObject(getString(R.string.rajnikant));
            dbHelper.addFlyingObject(getString(R.string.chuck_norris));
            dbHelper.addFlyingObject(getString(R.string.wonder_women));
            dbHelper.addFlyingObject(getString(R.string.ironman));
            dbHelper.addFlyingObject(getString(R.string.goku));
            dbHelper.addFlyingObject(getString(R.string.superman));
            dbHelper.addFlyingObject(getString(R.string.vision));
            dbHelper.addFlyingObject(getString(R.string.magneto));
            dbHelper.addFlyingObject(getString(R.string.buzz_lightyear));
            dbHelper.addFlyingObject(getString(R.string.green_lantern));
            dbHelper.addFlyingObject(getString(R.string.hancock));
            dbHelper.addFlyingObject(getString(R.string.peter_pan));

            dbHelper.addNonFlyingObject(getString(R.string.hulk));
            dbHelper.addNonFlyingObject(getString(R.string.captan_america));
            dbHelper.addNonFlyingObject(getString(R.string.thor));
            dbHelper.addNonFlyingObject(getString(R.string.flash));
            dbHelper.addNonFlyingObject(getString(R.string.batman));
            dbHelper.addNonFlyingObject(getString(R.string.spiderman));
            dbHelper.addNonFlyingObject(getString(R.string.wolverine));
            dbHelper.addNonFlyingObject(getString(R.string.deadpool));
            dbHelper.addNonFlyingObject(getString(R.string.nightcrawler));
            dbHelper.addNonFlyingObject(getString(R.string.silversurfer));
            dbHelper.addNonFlyingObject(getString(R.string.antman));
            dbHelper.addNonFlyingObject(getString(R.string.harry_potter));

            localDB.setWordsAdded();
        }
    }

    private Boolean checkTutorial() {
        if (!localDB.isTutorialCompleted()) {
            Intent i = new Intent(SplashActivity.this, TutorialActivity.class);
            startActivity(i);
            overridePendingTransition(0, R.anim.fadein);
            return false;
        }
        return true;
    }

    private void linkUIVariables() {
        imageViewRunway = (ImageView) findViewById(R.id.imageViewRunway);
        linearLayoutSlidingPanel = (LinearLayout) findViewById(R.id.linearLayoutSlidingPanel);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
    }


    public void splashAndGoNext() {

        runnable = new Runnable() {

            @Override
            public void run() {
                if (checkTutorial()) {
                    Intent i;
                    i = new Intent(SplashActivity.this, MenuActivity.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(0, R.anim.fadein);
                }
            }
        };
        handler = new Handler();
        handler.postDelayed(runnable, SPLASH_TIME_OUT);
    }


    private void animatePlane() {
        Thread thread = new Thread();
        Animation slideUpAnimation = new TranslateAnimation(0, 0, height, -height / 2);
        slideUpAnimation.setDuration(5000);
        slideUpAnimation.setFillAfter(true);
        slideUpAnimation.setRepeatCount(Animation.ABSOLUTE);  // animation repeat count
        slideUpAnimation.setRepeatMode(2);
        linearLayoutSlidingPanel.startAnimation(slideUpAnimation);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (player.isPlaying) {
            player.mp.pause();
            player.isPlaying = false;
        }
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!player.isPlaying) {
            player.mp = MediaPlayer.create(getApplicationContext(), R.raw.theme);
            player.mp.start();
            player.isPlaying = true;
        }
        //reset plane position
        animatePlane();
        handler.postDelayed(runnable, SPLASH_TIME_OUT);
    }

}
