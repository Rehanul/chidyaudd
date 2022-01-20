package com.ameegolabs.chidyaudd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.github.hujiaweibujidao.wava.Techniques;
import com.github.hujiaweibujidao.wava.YoYo;

import com.ameegolabs.chidyaudd.LocalDB;
import com.ameegolabs.chidyaudd.MyMediaPlayer;
import com.ameegolabs.chidyaudd.R;

public class TutorialActivity extends AppCompatActivity {

    //UI Variables
    private TextView textViewQuestion;
    private TextView textViewDescription;
    private ImageView imageView;
    private ImageButton imageButtonSkip;
    private ImageButton imageButtonRestart;
    private MaterialRippleLayout materialRippleLayout;
    private Button buttonBegin;

    //Class Variables
    private Context context;
    private Handler handler;
    private Runnable runnable;
    private int step = 0;
    private int prevStep = 0;

    private MyMediaPlayer player = MyMediaPlayer.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tutorial);

        linkUIComponents();
        context = TutorialActivity.this;
        handler = new Handler();


        step1();
    }

    private void linkUIComponents() {
        textViewQuestion = (TextView) findViewById(R.id.textViewQuestion);
        textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageButtonRestart = (ImageButton) findViewById(R.id.imageButtonRestart);
        imageButtonSkip = (ImageButton) findViewById(R.id.imageButtonSkip);
        materialRippleLayout = (MaterialRippleLayout) findViewById(R.id.materialRippleLayout);
        buttonBegin = (Button) findViewById(R.id.buttonBegin);

        buttonBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalDB localDB = new LocalDB(context);
                localDB.setTutorialCompleted();
                Intent i = new Intent(TutorialActivity.this, MenuActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        imageButtonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                step1();
                step = 0;
                prevStep = 0;
                Log.d("mylog", "prev step changed = " + prevStep);
                buttonBegin.setVisibility(View.INVISIBLE);
            }
        });

        imageButtonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDB localDB = new LocalDB(context);
                localDB.setTutorialCompleted();
                Intent i = new Intent(context, MenuActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }

    public void step1() {
        step = 1;
        buttonBegin.setVisibility(View.INVISIBLE);
        handler.removeCallbacks(runnable);
        materialRippleLayout.setOnTouchListener(null);
        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.touchdown4));
        imageView.setVisibility(View.INVISIBLE);
        textViewQuestion.setText(getString(R.string.tutorial));
        textViewQuestion.setVisibility(View.VISIBLE);
        textViewDescription.setText(getString(R.string.step1));

        Log.d("mylog", "prev step = " + prevStep);
        if (prevStep != 0) {
            imageView.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeIn).duration(400)
                    .interpolate(new AccelerateDecelerateInterpolator())
                    .playOn(imageView);
            materialRippleLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    final int action = motionEvent.getAction();

                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            step2();
                            break;
                        case MotionEvent.ACTION_UP:
                            if (step != 1) {
                                Toast.makeText(context, R.string.hold_there, Toast.LENGTH_SHORT).show();
                                step1();
                            }
                            break;
                    }
                    return false;
                }
            });
        } else {
            runnable = new Runnable() {
                @Override
                public void run() {
                    imageView.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeIn).duration(400)
                            .interpolate(new AccelerateDecelerateInterpolator())
                            .playOn(imageView);

                    materialRippleLayout.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            final int action = motionEvent.getAction();

                            switch (action) {
                                case MotionEvent.ACTION_DOWN:
                                    step2();
                                    break;
                                case MotionEvent.ACTION_UP:
                                    if (step != 1) {
                                        Toast.makeText(context, R.string.hold_there, Toast.LENGTH_SHORT).show();
                                        step1();
                                    }
                                    break;
                            }
                            return false;
                        }
                    });
                }
            };
        }
        handler.postDelayed(runnable, 3500);

    }

    private void step2() {
        prevStep = step;
        step = 2;

        Log.d("mylog", "prev step changed = " + prevStep);
        handler.removeCallbacks(runnable);
        imageView.setVisibility(View.INVISIBLE);
        textViewQuestion.setText(R.string.eagle);
        textViewDescription.setText(R.string.step2);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.touchup4));

        runnable = new Runnable() {
            @Override
            public void run() {
                imageView.setVisibility(View.VISIBLE);
                materialRippleLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        final int action = motionEvent.getAction();

                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                                //step2();
                                break;
                            case MotionEvent.ACTION_UP:
                                //Toast.makeText(context, "Woahh, hold your finger there", Toast.LENGTH_SHORT).show();
                                step3();
                                break;
                        }
                        return false;
                    }
                });

            }
        };
        handler.postDelayed(runnable, 5000);
    }

    private void step3() {
        handler.removeCallbacks(runnable);
        materialRippleLayout.setOnTouchListener(null);
        imageView.setVisibility(View.INVISIBLE);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.touchdown4));
        textViewQuestion.setText(R.string.mountain);
        textViewDescription.setText(R.string.step3);

        runnable = new Runnable() {
            @Override
            public void run() {
                imageView.setVisibility(View.VISIBLE);
                materialRippleLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        final int action = motionEvent.getAction();

                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                                step4();
                                break;
                            case MotionEvent.ACTION_UP:
                                //Toast.makeText(context, "Woahh, hold your finger there", Toast.LENGTH_SHORT).show();
                                //step3();
                                break;
                        }
                        return false;
                    }
                });

            }
        };
        handler.postDelayed(runnable, 5000);

    }

    private void step4() {
        step = 4;
        materialRippleLayout.setOnTouchListener(null);
        handler.removeCallbacks(runnable);
        imageView.setVisibility(View.INVISIBLE);
        textViewQuestion.setText("");
        textViewQuestion.setVisibility(View.GONE);
        textViewDescription.setText(R.string.step4);

        buttonBegin.setVisibility(View.VISIBLE);

        /*textViewDescription.setText("CLASSIC MODE\nCompete to achieve the highest score. you get 1 point for a correct answer and lose 1 point for a wrong answer" +
                "\n\nDEATHMATCH\nLast man standing will be the winner anyone who gives a wrong answer will be disqualified" +
                "\n\nRAPID FIRE\nPerson who gives the quickest response will only get the point. So, you have to be fast and careful" +
                "\n\nCHALLENGE\nIf you have other words in your mind or you have a group of amigos which have their unique jargon of words, then this mode is specially for you.\n" +
                "     You can add you own custom word from option > word list provided in the game menu");
          */

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
        if (player.isPlaying) {
            player.mp.pause();
            player.isPlaying = false;
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (!player.isPlaying) {
            player.mp.start();
            player.isPlaying = true;
        }
        super.onResume();
    }

}
