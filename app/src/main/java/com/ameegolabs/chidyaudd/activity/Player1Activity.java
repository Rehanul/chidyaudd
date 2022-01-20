package com.ameegolabs.chidyaudd.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.github.hujiaweibujidao.wava.Techniques;
import com.github.hujiaweibujidao.wava.YoYo;

import com.ameegolabs.chidyaudd.Countdown;
import com.ameegolabs.chidyaudd.GameMode;
import com.ameegolabs.chidyaudd.Gameplay;
import com.ameegolabs.chidyaudd.LocalDB;
import com.ameegolabs.chidyaudd.MyMediaPlayer;
import com.ameegolabs.chidyaudd.MyUtils;
import com.ameegolabs.chidyaudd.R;
import com.ameegolabs.chidyaudd.TimestampCallback;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class Player1Activity extends AppCompatActivity {

    //buttons cannot be used to show score because ripple comes above button text

    //UI components
    private TextView[] textViewScore;
    private TextView[] textViewQuestion;
    private TextView textViewHighScore;
    private MaterialRippleLayout[] materialRippleLayout;
    private Button[] buttonPlayer;
    private ImageView[] imageViewGainPoint;
    private ImageView[] imageViewLosePoint;
    private ImageView[] imageViewLives;

    private LinearLayout linearLayoutLives;
    private LinearLayout linearLayoutPauseContainer;
    private LinearLayout linearLayoutPauseButtons;
    private ImageButton imageButtonPlay;
    private ImageButton imageButtonRestart;
    private ImageButton imageButtonMenu;
    private static int NO_OF_PLAYERS = 1;
    private static int NO_OF_QUESTION_FIELDS = 1;
    private int i;

    private Boolean[] playerState;
    private int[] previousScorePlayer;

    private int lives = 5;
    private Gameplay gameplay;
    private Context context = this;
    private GameMode gameMode;
    private Countdown countdown;
    private Boolean firstTime = true;
    private MediaPlayer mPlayer;
    private String stringMode;
    private LocalDB localDB;
    private int highScore;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_1_players);

        localDB = new LocalDB(context);
        Bundle extras = getIntent().getExtras();
        stringMode = extras.getString("mode");
        gameMode = new GameMode(NO_OF_PLAYERS, stringMode);

        bindUIComponents();
        playMusic();
        for (i = 0; i < NO_OF_PLAYERS; i++) {
            setOnClickListenersForCountdown(i);
        }
        initialiseComponents();


    }//onCreate

    private void initialiseComponents() {

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9328538449483757/7164124020");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });


        if (stringMode.equals("deathmatch")) {
            linearLayoutLives.setVisibility(View.GONE);
        }
        highScore = localDB.getHighScore(stringMode);
        textViewHighScore = (TextView) findViewById(R.id.textViewHighScore);
        textViewHighScore.setText(String.valueOf(getString(R.string.high_score) + " " + highScore));


        countdown = new Countdown(context, new TimestampCallback() {
            //onTimestampEnd
            @Override
            public void onTimestampFinish() {
                String question = countdown.getCountdownText();
                for (i = 0; i < NO_OF_QUESTION_FIELDS; i++) {
                    textViewQuestion[i].setText(question);
                }

            }
        }, new TimestampCallback() {
            //onCountdownFinish
            @Override
            public void onTimestampFinish() {
                for (i = 0; i < NO_OF_PLAYERS; i++) {
                    setOnClickListenersForGameplay(i);
                }
                gameMode.setDefaults();
                for (i = 0; i < NO_OF_PLAYERS; i++) {
                    gameMode.actionDown(i + 1);
                }
                gameMode.setDefaults();
                gameplay.start();
            }
        });


        gameplay = new Gameplay(context, new TimestampCallback() {
            //onTimestampEnd
            @Override
            public void onTimestampFinish() {

                //give score, animate score, checkWinner should not work for first time
                if (!firstTime) {

                    //give and check scores
                    gameMode.giveScores();
                    if (gameMode.hasWinner()) {
                        declareWinner();
                        return;
                    }

                    for (i = 0; i < NO_OF_PLAYERS; i++) {

                        MyUtils.logThis("player " + i + " whose score is " + gameMode.getScore(i) +
                                " and prev score is " + previousScorePlayer[i]);
                        //animate scores
                        //don't animate disqualified players in Deathmatch mode
                        //don't animate for players who has previous score rapidfire
                        if (!stringMode.equals("deathmatch") || !(gameMode.getScore(i) == previousScorePlayer[i] || gameMode.getScore(i) == -1)) {
                            if (gameMode.getScore(i) > previousScorePlayer[i]) {
                                Log.d("mylog", "Player " + i + " gets score");
                                animateGainPoints(imageViewGainPoint[i], i);
                            } else {
                                Log.d("mylog", "player " + i + " loose score");
                                animateLosePoints(imageViewLosePoint[i]);
                            }
                        }


                        previousScorePlayer[0] = gameMode.getScore(0);
                        //display score
                        textViewScore[0].setText(String.valueOf(gameMode.getScore(0)));

                    }//end for

                    showLosers();
                }//firstTime
                Log.d("mylog", "---------------------------------------------------------------------");

                //set initial Player inputs i.e. touched down or up
                gameMode.setDefaults();
                for (i = 0; i < NO_OF_PLAYERS; i++) {
                    if (playerState[i]) {
                        gameMode.actionUp(i + 1);
                    } else {
                        gameMode.actionDown(i + 1);
                    }
                }
                gameMode.setDefaults();

                for (i = 0; i < NO_OF_QUESTION_FIELDS; i++) {
                    textViewQuestion[i].setText(gameplay.getQuestion());
                }

                if (gameMode.hasWinner()) {
                    textViewQuestion[0].setText(R.string.gameover);
                    return;
                }

                MyUtils.logThis("Question = " + gameplay.getQuestion() + " that flies " + gameplay.getIsFlyingThing());
                gameMode.setFlyingObject(gameplay.getIsFlyingThing());
                firstTime = false;

                //showLosers();
            }//onTimestampEnd

        }, stringMode);//end gameplay with callback listeners

        setPauseClickListeners();
        for (i = 0; i < NO_OF_QUESTION_FIELDS; i++) {
            setPauseListenersFromQuestion(i);
        }

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("A3FB10294EA81364BE72EDC33F543B47")
                .build();

        mInterstitialAd.loadAd(adRequest);

    }

    private void setPauseClickListeners() {

        imageButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.FadeOut).duration(400)
                        .interpolate(new AccelerateDecelerateInterpolator())
                        .playOn(linearLayoutPauseContainer);
                YoYo.with(Techniques.ZoomOut).duration(400)
                        .interpolate(new AccelerateDecelerateInterpolator())
                        .listen(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                linearLayoutPauseContainer.setVisibility(View.INVISIBLE);
                            }
                        })
                        .playOn(linearLayoutPauseButtons);

                firstTime = true;
                for (i = 0; i < NO_OF_PLAYERS; i++) {
                    setOnClickListenersForCountdown(i);
                }
            }
        });

        imageButtonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.FadeOut).duration(400)
                        .interpolate(new AccelerateDecelerateInterpolator())
                        .playOn(linearLayoutPauseContainer);
                YoYo.with(Techniques.ZoomOut).duration(400)
                        .interpolate(new AccelerateDecelerateInterpolator())
                        .listen(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                linearLayoutPauseContainer.setVisibility(View.INVISIBLE);
                            }
                        })
                        .playOn(linearLayoutPauseButtons);
                resetPlayers();
                gameplay.reset();
                gameMode.reset();
                lives = 5;
                for (i = 0; i < 5; i++) {
                    imageViewLives[i].setVisibility(View.VISIBLE);
                }
                firstTime = true;
                for (i = 0; i < NO_OF_PLAYERS; i++) {
                    setOnClickListenersForCountdown(i);
                    textViewScore[i].setText("0");
                }

            }
        });


        imageButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setPauseListenersFromQuestion(final int x) {
        textViewQuestion[x].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutPauseContainer.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeIn).duration(400)
                        .interpolate(new AccelerateDecelerateInterpolator())
                        .playOn(linearLayoutPauseContainer);
                YoYo.with(Techniques.ZoomIn).duration(400)
                        .interpolate(new AccelerateDecelerateInterpolator())
                        .playOn(linearLayoutPauseButtons);

                gameplay.pause();
                for (i = 0; i < NO_OF_QUESTION_FIELDS; i++) {
                    textViewQuestion[i].setText(getResources().getString(R.string.touch_to_start));
                }
            }
        });
    }

    private void bindUIComponents() {

        playerState = new Boolean[NO_OF_PLAYERS];
        previousScorePlayer = new int[NO_OF_PLAYERS];
        textViewScore = new TextView[NO_OF_PLAYERS];
        materialRippleLayout = new MaterialRippleLayout[NO_OF_PLAYERS];
        buttonPlayer = new Button[NO_OF_PLAYERS];
        imageViewGainPoint = new ImageView[NO_OF_PLAYERS];
        imageViewLosePoint = new ImageView[NO_OF_PLAYERS];
        imageViewLives = new ImageView[5];
        linearLayoutLives = (LinearLayout) findViewById(R.id.linearLayoutLives);

        textViewQuestion = new TextView[NO_OF_QUESTION_FIELDS];

        for (i = 0; i < NO_OF_PLAYERS; i++) {
            playerState[i] = true;
            previousScorePlayer[i] = 0;
        }

        textViewScore[0] = (TextView) findViewById(R.id.textViewScorePlayer1);
        materialRippleLayout[0] = (MaterialRippleLayout) findViewById(R.id.rippleEffect1);
        imageViewGainPoint[0] = (ImageView) findViewById(R.id.imageViewGainPoints1);
        imageViewLosePoint[0] = (ImageView) findViewById(R.id.imageViewLosePoints1);
        buttonPlayer[0] = (Button) findViewById(R.id.buttonPlayer1);

        textViewQuestion[0] = (TextView) findViewById(R.id.textViewQuestion1);

        linearLayoutPauseContainer = (LinearLayout) findViewById(R.id.linearLayoutPauseContainer);
        linearLayoutPauseButtons = (LinearLayout) findViewById(R.id.linearLayoutPauseButtons);
        imageButtonPlay = (ImageButton) findViewById(R.id.imageButtonPlay);
        imageButtonRestart = (ImageButton) findViewById(R.id.imageButtonRestart);
        imageButtonMenu = (ImageButton) findViewById(R.id.imageButtonMenu);

        imageViewLives[0] = (ImageView) findViewById(R.id.imageViewLife1);
        imageViewLives[1] = (ImageView) findViewById(R.id.imageViewLife2);
        imageViewLives[2] = (ImageView) findViewById(R.id.imageViewLife3);
        imageViewLives[3] = (ImageView) findViewById(R.id.imageViewLife4);
        imageViewLives[4] = (ImageView) findViewById(R.id.imageViewLife5);

    }//bindUIComponents

    private void animateGainPoints(final ImageView imageView, int player) {

        //if player is 90 degree i.e. player no 2
        if (player == 2) {
            imageView.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FlipInY).duration(400)
                    .interpolate(new AccelerateDecelerateInterpolator())
                    .listen(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            YoYo.with(Techniques.FadeOut).duration(200)
                                    .interpolate(new AccelerateDecelerateInterpolator())
                                    .playOn(imageView);
                        }
                    })
                    .playOn(imageView);

        } else {

            imageView.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FlipInX).duration(400)
                    .interpolate(new AccelerateDecelerateInterpolator())
                    .listen(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            YoYo.with(Techniques.FadeOut).duration(200)
                                    .interpolate(new AccelerateDecelerateInterpolator())
                                    .playOn(imageView);
                        }
                    })
                    .playOn(imageView);

        }
    }//animateGainPoints

    private void animateLosePoints(final ImageView imageView) {

        if (!stringMode.equals("deathmatch")) {
            lives--;
            imageViewLives[lives].setVisibility(View.INVISIBLE);

            if (lives <= 0) {
                gameMode.setPlayer1HasLost();
                declareWinner();
                return;
            }
        }
        imageView.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.BounceIn).duration(400)
                .interpolate(new AccelerateDecelerateInterpolator())
                .listen(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        YoYo.with(Techniques.FadeOut).duration(200)
                                .interpolate(new AccelerateDecelerateInterpolator())
                                .playOn(imageView);
                    }
                })
                .playOn(imageView);


    }//animateLosePoints


    private void playMusic() {
        mPlayer = MediaPlayer.create(context, R.raw.happyinstrumental);
        mPlayer.setLooping(true);
        mPlayer.start();
    }//playMusic

    private void showLosers() {
        for (int i = 0; i < NO_OF_PLAYERS; i++) {
            if (gameMode.isLoser(i)) {

                MyUtils.logThis("show loser player " + i);
                materialRippleLayout[i].setOnClickListener(null);
                materialRippleLayout[i].setRippleColor(ContextCompat.getColor(context, R.color.colorButtonPlayer2));
                buttonPlayer[i].setBackgroundColor(ContextCompat.getColor(context, R.color.colorDarkerGrey));
                materialRippleLayout[i].setRippleColor(ContextCompat.getColor(context, R.color.colorDarkerGrey));

                //imageViewGainPoint[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.chick));


                imageViewGainPoint[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.chick));

                imageViewGainPoint[i].setVisibility(View.VISIBLE);
                //imageViewGainPoint[i].setImageAlpha(1);
                //imageViewGainPoint[i].setAlpha(1f);
                YoYo.with(Techniques.BounceIn).duration(0)
                        .interpolate(new AccelerateDecelerateInterpolator())
                        .playOn(imageViewGainPoint[i]);

                imageViewLosePoint[i].setVisibility(View.VISIBLE);
                //imageViewLosePoint[i].setImageAlpha(1);
                //imageViewLosePoint[i].setAlpha(1f);

                YoYo.with(Techniques.BounceIn).duration(0)
                        .interpolate(new AccelerateDecelerateInterpolator())
                        .playOn(imageViewLosePoint[i]);

            }
        }
    }//showLosers

    private void declareWinner() {
        gameplay.pause();
        textViewScore[0].setText(String.valueOf(gameMode.getScore(0)));
        textViewQuestion[0].setText(R.string.gameover);


        RelativeLayout relativeLayout = (RelativeLayout) imageButtonPlay.getParent();
        relativeLayout.setVisibility(View.GONE);

        for (i = 0; i < NO_OF_PLAYERS; i++) {
            if (!stringMode.equals("deathmatch")) {
                textViewScore[i].setText(String.valueOf(gameMode.getScore(i)));
            } else {
                textViewScore[i].setText(String.valueOf(previousScorePlayer[0]));
            }
        }
        i = 0;
        //show and save highScore
        if ((gameMode.getScore(0) > highScore && !stringMode.equals("deathmatch"))
                || (previousScorePlayer[0] > highScore && stringMode.equals("deathmatch"))) {


            MyUtils.logThis("score " + gameMode.getScore(0) + " previousScorePlayer " + previousScorePlayer[0] + " high score " + highScore);
            if (!stringMode.equals("deathmatch")) {
                localDB.setHighScore(stringMode, gameMode.getScore(0));
                textViewHighScore.setText(String.valueOf(getString(R.string.new_record) + getString(R.string.high_score) + " " + gameMode.getScore(0)));
                highScore = gameMode.getScore(0);
            } else {
                localDB.setHighScore(stringMode, previousScorePlayer[0]);
                textViewHighScore.setText(String.valueOf(getString(R.string.new_record) + getString(R.string.high_score) + " " + previousScorePlayer[0]));
                highScore = previousScorePlayer[0];
            }
            imageViewGainPoint[i].setVisibility(View.VISIBLE);

            imageViewGainPoint[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.hero));

            YoYo.with(Techniques.BounceIn).duration(400)
                    .interpolate(new AccelerateDecelerateInterpolator())
                    .playOn(imageViewGainPoint[i]);
        } else {
            materialRippleLayout[i].setRippleColor(ContextCompat.getColor(context, R.color.colorDarkerGrey));
            buttonPlayer[i].setBackgroundColor(ContextCompat.getColor(context, R.color.colorDarkerGrey));

            //imageViewGainPoint[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.chick));
            imageViewGainPoint[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.chick));

            imageViewGainPoint[i].setVisibility(View.VISIBLE);
            YoYo.with(Techniques.BounceIn).duration(400)
                    .interpolate(new AccelerateDecelerateInterpolator())
                    .playOn(imageViewGainPoint[i]);

            imageViewLosePoint[i].setVisibility(View.VISIBLE);
            YoYo.with(Techniques.BounceIn).duration(400)
                    .interpolate(new AccelerateDecelerateInterpolator())
                    .playOn(imageViewLosePoint[i]);
        }

        textViewQuestion[0].setText(R.string.gameover);
        gameplay.pause();

        requestNewInterstitial();

    }//declareWinner


    private void resetPlayers() {

        firstTime = true;
        RelativeLayout relativeLayout = (RelativeLayout) imageButtonPlay.getParent();
        relativeLayout.setVisibility(View.VISIBLE);


        for (i = 0; i < NO_OF_PLAYERS; i++) {
            previousScorePlayer[i] = 0;
            imageViewGainPoint[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.eagle));
            materialRippleLayout[i].setRippleColor(ContextCompat.getColor(context, R.color.colorRipplePlayer1));
            buttonPlayer[i].setBackgroundColor(ContextCompat.getColor(context, R.color.colorButtonPlayer1));

            imageViewGainPoint[i].setVisibility(View.INVISIBLE);
            imageViewLosePoint[i].setVisibility(View.INVISIBLE);
        }

    }//resetPlayers

    private void checkReadyPlayers() {

        for (int j = 0; j < NO_OF_QUESTION_FIELDS; j++) {
            textViewQuestion[j].setText(getString(R.string.waiting));
        }

        for (i = 0; i < NO_OF_PLAYERS; i++) {
            if (!gameMode.isLoser(i) && playerState[i]) {
                MyUtils.logThis("Player " + i + " is not ready");
                return;
            }
            MyUtils.logThis("Player " + i + " is ready");
        }
        MyUtils.logThis("Everyone is is ready");

        for (i = 0; i < NO_OF_QUESTION_FIELDS; i++) {
            textViewQuestion[i].setText(getString(R.string.ready));
        }
        countdown.start();

    }//checkReadyPlayers

    private void setOnClickListenersForCountdown(final int x) {
        materialRippleLayout[x].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int action = motionEvent.getAction();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        playerState[x] = false;
                        MyUtils.logThis("Player " + x + " state changed to false");
                        checkReadyPlayers();
                        break;
                    case MotionEvent.ACTION_UP:
                        playerState[x] = true;
                        MyUtils.logThis("Player " + x + " state changed to true");
                        //checkReadyPlayers();
                        for (int j = 0; j < NO_OF_QUESTION_FIELDS; j++) {
                            textViewQuestion[j].setText(getString(R.string.waiting));
                        }
                        countdown.pause();
                        break;
                }
                return false;
            }
        });

    }

    private void setOnClickListenersForGameplay(final int x) {

        materialRippleLayout[x].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        gameMode.actionDown(x + 1);
                        playerState[x] = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        gameMode.actionUp(x + 1);
                        playerState[x] = true;
                        break;
                }
                return false;
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayer.pause();
        gameplay.pause();
        countdown.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        firstTime = true;
        mPlayer.start();
        for (i = 0; i < NO_OF_PLAYERS; i++) {
            setOnClickListenersForCountdown(i);
        }
        for (i = 0; i < NO_OF_QUESTION_FIELDS; i++) {
            textViewQuestion[i].setText(getResources().getString(R.string.touch_to_start));
        }
    }

    @Override
    protected void onStop() {
        MyMediaPlayer.getInstance().mp.seekTo(0);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        //moveTaskToBack(true);
        if (linearLayoutPauseContainer.getVisibility() == View.INVISIBLE) {
            linearLayoutPauseContainer.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeIn).duration(400)
                    .interpolate(new AccelerateDecelerateInterpolator())
                    .playOn(linearLayoutPauseContainer);
            YoYo.with(Techniques.ZoomIn).duration(400)
                    .interpolate(new AccelerateDecelerateInterpolator())
                    .playOn(linearLayoutPauseButtons);

            gameplay.pause();
            for (i = 0; i < NO_OF_QUESTION_FIELDS; i++) {
                textViewQuestion[i].setText(getResources().getString(R.string.touch_to_start));
            }
        } else {
            YoYo.with(Techniques.FadeOut).duration(400)
                    .interpolate(new AccelerateDecelerateInterpolator())
                    .playOn(linearLayoutPauseContainer);
            YoYo.with(Techniques.ZoomOut).duration(400)
                    .interpolate(new AccelerateDecelerateInterpolator())
                    .listen(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            linearLayoutPauseContainer.setVisibility(View.INVISIBLE);
                        }
                    })
                    .playOn(linearLayoutPauseButtons);

            firstTime = true;
            for (i = 0; i < NO_OF_PLAYERS; i++) {
                setOnClickListenersForCountdown(i);
            }
        }
        //Toast.makeText(context, "yeah", Toast.LENGTH_SHORT).show();
    }

}
