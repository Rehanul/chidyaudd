package com.ameegolabs.chidyaudd.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.hujiaweibujidao.wava.Techniques;
import com.github.hujiaweibujidao.wava.YoYo;

import com.ameegolabs.chidyaudd.MyMediaPlayer;
import com.ameegolabs.chidyaudd.R;


public class MenuActivity extends AppCompatActivity {

    //UI Variables
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button buttonAddPlayer;
    private Button buttonSubtractPlayer;
    private TextView textViewNoOfPlayers;
    private TextView textViewChidyaUdd;
    private TextView textViewNoOfPlayersText;
    private FrameLayout frameLayoutCloud;
    private FrameLayout frameLayoutTree;
    private LinearLayout linearLayoutContents;
    private LinearLayout linearLayoutButtonContainer;
    private FloatingActionMenu fabOptions;
    private FloatingActionButton fabCustomWords;
    private FloatingActionButton fabLearn;
    private FloatingActionButton fabTutorial;
    private FloatingActionButton fabLanguage;

    //Class Variables
    private Context context;
    private static int noOfPlayers = 1;
    private MyMediaPlayer player = MyMediaPlayer.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);

        context = MenuActivity.this;
        linkUIVariables();


        animateUIOnCreate();
        if (!player.isPlaying) {
            player.mp.start();
        }
        textViewNoOfPlayers.setText(String.valueOf(noOfPlayers));

        setOnClickListeners();

        //set font
        Typeface fontAdventure = Typeface.createFromAsset(getAssets(), "adventure.ttf");
        button1.setTypeface(fontAdventure);
        button2.setTypeface(fontAdventure);
        button3.setTypeface(fontAdventure);
        button4.setTypeface(fontAdventure);
        textViewChidyaUdd.setTypeface(fontAdventure);
        textViewNoOfPlayersText.setTypeface(fontAdventure);
    }


    private void linkUIVariables() {

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        buttonAddPlayer = (Button) findViewById(R.id.buttonAddPlayer);
        buttonSubtractPlayer = (Button) findViewById(R.id.buttonSubtractPlayer);

        textViewNoOfPlayers = (TextView) findViewById(R.id.textViewNoOfPlayers);
        textViewChidyaUdd = (TextView) findViewById(R.id.textViewChidyaUdd);
        textViewNoOfPlayersText = (TextView) findViewById(R.id.textViewNoOfPlayersText);

        frameLayoutCloud = (FrameLayout) findViewById(R.id.frameLayoutCloud);
        frameLayoutTree = (FrameLayout) findViewById(R.id.frameLayoutTree);
        linearLayoutContents = (LinearLayout) findViewById(R.id.linearLayoutContents);
        linearLayoutButtonContainer = (LinearLayout) findViewById(R.id.linearLayoutButtonContainer);

        fabOptions = (FloatingActionMenu) findViewById(R.id.fabOptions);
        fabCustomWords = (FloatingActionButton) findViewById(R.id.fabCustomWords);
        fabLearn = (FloatingActionButton) findViewById(R.id.fabLearn);
        fabTutorial = (FloatingActionButton) findViewById(R.id.fabTutorial);
        fabLanguage = (FloatingActionButton) findViewById(R.id.fabLanguage);

    }


    private void setOnClickListeners() {

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToGame("classic");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToGame("deathmatch");
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToGame("rapidfire");
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToGame("challenge");
            }
        });

        buttonAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(noOfPlayers <= 4)) {
                    return;
                }
                addPlayer();
            }
        });


        buttonSubtractPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(noOfPlayers > 1)) {
                    return;
                }
                subtractPlayer();
            }
        });

        fabOptions.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened){
                    fabOptions.setClickable(true);
                }
                else {
                    fabOptions.setClickable(false);
                }
            }
        });
        fabCustomWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CustomWordsActivity.class);
                startActivity(intent);
            }
        });
        fabLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LearnActivity.class);
                startActivity(intent);
            }
        });
        fabTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TutorialActivity.class);
                startActivity(intent);
            }
        });
        fabLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LanguageActivity.class);
                startActivity(intent);
            }
        });

    }//setOnClickListener


    private void addPlayer() {

        YoYo.with(Techniques.ZoomIn).duration(500)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(textViewNoOfPlayers);

        noOfPlayers++;
        textViewNoOfPlayers.setText(String.valueOf(noOfPlayers));

        YoYo.with(Techniques.ZoomOutLeft).duration(400)
                .interpolate(new AccelerateDecelerateInterpolator())
                .listen(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        YoYo.with(Techniques.BounceInRight).duration(400)
                                .interpolate(new AccelerateDecelerateInterpolator())
                                .playOn(button1);
                    }
                })
                .playOn(button1);

        YoYo.with(Techniques.ZoomOutLeft).duration(600)
                .interpolate(new AccelerateDecelerateInterpolator())
                .listen(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        YoYo.with(Techniques.BounceInRight).duration(400)
                                .interpolate(new AccelerateDecelerateInterpolator())
                                .playOn(button2);
                    }
                })
                .playOn(button2);

        YoYo.with(Techniques.ZoomOutLeft).duration(800)
                .interpolate(new AccelerateDecelerateInterpolator())
                .listen(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        YoYo.with(Techniques.BounceInRight).duration(400)
                                .interpolate(new AccelerateDecelerateInterpolator())
                                .playOn(button3);
                    }
                })
                .playOn(button3);

        YoYo.with(Techniques.ZoomOutLeft).duration(1000)
                .interpolate(new AccelerateDecelerateInterpolator())
                .listen(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        YoYo.with(Techniques.BounceInRight).duration(400)
                                .interpolate(new AccelerateDecelerateInterpolator())
                                .playOn(button4);
                    }
                })
                .playOn(button4);
    }//addPlayer


    private void subtractPlayer() {
        YoYo.with(Techniques.ZoomIn).duration(500)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(textViewNoOfPlayers);

        noOfPlayers--;

        textViewNoOfPlayers.setText(String.valueOf("" + noOfPlayers));

        YoYo.with(Techniques.ZoomOutRight).duration(400)
                .interpolate(new AccelerateDecelerateInterpolator())
                .listen(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        YoYo.with(Techniques.BounceInLeft).duration(400)
                                .interpolate(new AccelerateDecelerateInterpolator())
                                .playOn(button1);
                    }
                })
                .playOn(button1);

        YoYo.with(Techniques.ZoomOutRight).duration(600)
                .interpolate(new AccelerateDecelerateInterpolator())
                .listen(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        YoYo.with(Techniques.BounceInLeft).duration(400)
                                .interpolate(new AccelerateDecelerateInterpolator())
                                .playOn(button2);
                    }
                })
                .playOn(button2);

        YoYo.with(Techniques.ZoomOutRight).duration(800)
                .interpolate(new AccelerateDecelerateInterpolator())
                .listen(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        YoYo.with(Techniques.BounceInLeft).duration(400)
                                .interpolate(new AccelerateDecelerateInterpolator())
                                .playOn(button3);
                    }
                })
                .playOn(button3);

        YoYo.with(Techniques.ZoomOutRight).duration(1000)
                .interpolate(new AccelerateDecelerateInterpolator())
                .listen(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        YoYo.with(Techniques.BounceInLeft).duration(400)
                                .interpolate(new AccelerateDecelerateInterpolator())
                                .playOn(button4);
                    }
                })
                .playOn(button4);
    }//subtractPlayer


    private void animateUIOnCreate() {
        //initial animation
        linearLayoutButtonContainer.setVisibility(View.INVISIBLE);
        fabOptions.setVisibility(View.INVISIBLE);

        YoYo.with(Techniques.FadeIn).duration(1500)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(frameLayoutCloud);
        YoYo.with(Techniques.FadeIn).duration(1500)
                .interpolate(new AccelerateDecelerateInterpolator())
                .listen(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        linearLayoutButtonContainer.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.FadeInUp).duration(500)
                                .interpolate(new AccelerateDecelerateInterpolator())
                                .playOn(linearLayoutButtonContainer);

                        fabOptions.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.SlideInRight).duration(500)
                                .interpolate(new AccelerateDecelerateInterpolator())
                                .playOn(fabOptions);

                        animateBackground();
                    }
                })
                .playOn(frameLayoutTree);
        YoYo.with(Techniques.FadeIn).duration(1500)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(linearLayoutContents);

    }//animateUIOnCreate


    private void animateBackground() {

        //animate background
        final ImageView backgroundOne = (ImageView) findViewById(R.id.imageViewClouds1);
        final ImageView backgroundTwo = (ImageView) findViewById(R.id.imageViewClouds2);

        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(20000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = backgroundOne.getWidth();
                final float translationX = width * progress;
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX - width);
            }
        });
        animator.start();


        final ImageView background3 = (ImageView) findViewById(R.id.imageViewTreeBackground1);
        final ImageView background4 = (ImageView) findViewById(R.id.imageViewTreeBackground2);

        final ValueAnimator animator2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator2.setRepeatCount(ValueAnimator.INFINITE);
        animator2.setInterpolator(new LinearInterpolator());
        animator2.setDuration(20000L);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation2) {
                final float progress2 = (float) animation2.getAnimatedValue();
                final float width2 = background3.getWidth();
                final float translationX2 = width2 * progress2;
                background3.setTranslationX(translationX2);
                background4.setTranslationX(translationX2 - width2);
            }
        });
        animator2.start();

    }//animateBackground


    private void redirectToGame(String mode) {
        Intent intent;

        switch (noOfPlayers) {
            case 1:
                intent = new Intent(context, Player1Activity.class);
                break;
            case 2:
                intent = new Intent(context, Player2Activity.class);
                break;
            case 3:
                intent = new Intent(context, Player3Activity.class);
                break;
            case 4:
                intent = new Intent(context, Player4Activity.class);
                break;
            case 5:
                intent = new Intent(context, Player5Activity.class);
                break;
            default:
                intent = new Intent(context, Player2Activity.class);
        }
        intent.putExtra("mode", mode);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }//redirectToGame


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
        super.onResume();
        if (!player.isPlaying) {
            player.mp.start();
            player.isPlaying = true;
        }
    }
}
