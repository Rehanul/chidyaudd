package com.ameegolabs.chidyaudd.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.github.hujiaweibujidao.wava.Techniques;
import com.github.hujiaweibujidao.wava.YoYo;

import com.ameegolabs.chidyaudd.DBHelper;
import com.ameegolabs.chidyaudd.MyMediaPlayer;
import com.ameegolabs.chidyaudd.MyRecyclerAdapter;
import com.ameegolabs.chidyaudd.R;

import java.util.ArrayList;
import java.util.Arrays;

public class LearnActivity extends AppCompatActivity {

    //UI Variables
    RecyclerView recyclerViewFlying;
    RecyclerView recyclerViewNonFlying;
    TextView textViewFlying;
    TextView textViewNonFlying;
    FloatingActionButton fabAddFlying;
    FloatingActionButton fabAddNonFlying;


    //Class Variables
    MyRecyclerAdapter adapterFlying;
    MyRecyclerAdapter adapterNonFlying;
    ArrayList<String> arrayListFlying;
    ArrayList<String> arrayListNonFlying;
    private MyMediaPlayer player = MyMediaPlayer.getInstance();
    Context context;
    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_custom_words);

        context = LearnActivity.this;
        dbHelper = new DBHelper(context);
        linkUIVariables();

        //data
        //addData();
        String[] flyingThings = context.getResources().getStringArray(R.array.flying);
        String[] nonFlyingThings = context.getResources().getStringArray(R.array.nonFlying);
        arrayListFlying = new ArrayList<>(Arrays.asList(flyingThings));
        arrayListNonFlying = new ArrayList<>(Arrays.asList(nonFlyingThings));


        //adapter
        adapterFlying = new MyRecyclerAdapter(LearnActivity.this, arrayListFlying, true);
        recyclerViewFlying.setAdapter(adapterFlying);

        adapterNonFlying = new MyRecyclerAdapter(LearnActivity.this, arrayListNonFlying, false);
        recyclerViewNonFlying.setAdapter(adapterNonFlying);


        setOnClickListeners();

        YoYo.with(Techniques.SlideInDown).duration(400)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(recyclerViewFlying);

        YoYo.with(Techniques.SlideOutDown).duration(400)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(recyclerViewNonFlying);

        YoYo.with(Techniques.RollIn).duration(400)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(fabAddFlying);

        YoYo.with(Techniques.RollOut).duration(400)
                .interpolate(new AccelerateDecelerateInterpolator())
                .playOn(fabAddNonFlying);

        fabAddFlying.setVisibility(View.INVISIBLE);
        fabAddNonFlying.setVisibility(View.INVISIBLE);


    }

    private void linkUIVariables() {
        textViewFlying = (TextView) findViewById(R.id.textViewFlying);
        textViewNonFlying = (TextView) findViewById(R.id.textViewNonFlying);
        fabAddFlying = (FloatingActionButton) findViewById(R.id.fabAddFlying);
        fabAddNonFlying = (FloatingActionButton) findViewById(R.id.fabAddNonFlying);


        //recycler
        recyclerViewFlying = (RecyclerView) findViewById(R.id.recyclerViewFlying);
        recyclerViewFlying.setLayoutManager(new LinearLayoutManager(LearnActivity.this));

        recyclerViewNonFlying = (RecyclerView) findViewById(R.id.recyclerViewNonFlying);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LearnActivity.this);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewNonFlying.setLayoutManager(linearLayoutManager);
    }

    private void setOnClickListeners() {

        textViewFlying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (recyclerViewFlying.getAlpha() != 1) {
                    YoYo.with(Techniques.SlideInDown).duration(400)
                            .interpolate(new AccelerateDecelerateInterpolator())
                            .playOn(recyclerViewFlying);

                    YoYo.with(Techniques.SlideOutDown).duration(400)
                            .interpolate(new AccelerateDecelerateInterpolator())
                            .playOn(recyclerViewNonFlying);

                    //recyclerViewFlying.setVisibility(View.VISIBLE);
                    //recyclerViewNonFlying.setVisibility(View.INVISIBLE);

                    YoYo.with(Techniques.RollIn).duration(400)
                            .interpolate(new AccelerateDecelerateInterpolator())
                            .playOn(fabAddFlying);

                    YoYo.with(Techniques.RollOut).duration(400)
                            .interpolate(new AccelerateDecelerateInterpolator())
                            .playOn(fabAddNonFlying);
                }
            }
        });
        textViewNonFlying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerViewNonFlying.getAlpha() != 1) {
                    YoYo.with(Techniques.SlideInUp).duration(400)
                            .interpolate(new AccelerateDecelerateInterpolator())
                            .playOn(recyclerViewNonFlying);

                    YoYo.with(Techniques.SlideOutUp).duration(400)
                            .interpolate(new AccelerateDecelerateInterpolator())
                            .playOn(recyclerViewFlying);

                    //recyclerViewNonFlying.setVisibility(View.VISIBLE);
                    //recyclerViewFlying.setVisibility(View.INVISIBLE);

                    YoYo.with(Techniques.RollOut).duration(400)
                            .interpolate(new AccelerateDecelerateInterpolator())
                            .playOn(fabAddFlying);

                    YoYo.with(Techniques.RollIn).duration(400)
                            .interpolate(new AccelerateDecelerateInterpolator())
                            .playOn(fabAddNonFlying);
                }
            }
        });

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
