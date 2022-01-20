package com.ameegolabs.chidyaudd.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.hujiaweibujidao.wava.Techniques;
import com.github.hujiaweibujidao.wava.YoYo;

import com.ameegolabs.chidyaudd.DBHelper;
import com.ameegolabs.chidyaudd.MyMediaPlayer;
import com.ameegolabs.chidyaudd.MyRecyclerAdapter;
import com.ameegolabs.chidyaudd.R;
import com.ameegolabs.chidyaudd.SwipeHelper;

import java.util.ArrayList;

public class CustomWordsActivity extends AppCompatActivity {

    //UI Variables
    private RecyclerView recyclerViewFlying;
    private RecyclerView recyclerViewNonFlying;
    private TextView textViewFlying;
    private TextView textViewNonFlying;
    private FloatingActionButton fabAddFlying;
    private FloatingActionButton fabAddNonFlying;

    //Class variables
    private MyRecyclerAdapter adapterFlying;
    private MyRecyclerAdapter adapterNonFlying;
    private ArrayList<String> arrayListFlying;
    private ArrayList<String> arrayListNonFlying;
    private MyMediaPlayer player = MyMediaPlayer.getInstance();
    private Context context;
    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_custom_words);

        context = CustomWordsActivity.this;
        dbHelper = new DBHelper(context);
        linkUIVariables();


        arrayListFlying = dbHelper.getAllFlyingObjects();
        if (arrayListFlying==null){
            arrayListFlying = new ArrayList<>();
        }

        arrayListNonFlying = dbHelper.getAllNonFlyingObjects();
        if (arrayListNonFlying==null){
            arrayListNonFlying = new ArrayList<>();
        }

        //adapter
        adapterFlying = new MyRecyclerAdapter(CustomWordsActivity.this, arrayListFlying, true);
        recyclerViewFlying.setAdapter(adapterFlying);

        adapterNonFlying = new MyRecyclerAdapter(CustomWordsActivity.this, arrayListNonFlying, false);
        recyclerViewNonFlying.setAdapter(adapterNonFlying);

        //swipe
        ItemTouchHelper.Callback callback = new SwipeHelper(adapterFlying);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerViewFlying);

        ItemTouchHelper.Callback callback2 = new SwipeHelper(adapterNonFlying);
        ItemTouchHelper helper2 = new ItemTouchHelper(callback2);
        helper2.attachToRecyclerView(recyclerViewNonFlying);

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

    }

    private void linkUIVariables() {
        textViewFlying = (TextView) findViewById(R.id.textViewFlying);
        textViewNonFlying = (TextView) findViewById(R.id.textViewNonFlying);
        fabAddFlying = (FloatingActionButton) findViewById(R.id.fabAddFlying);
        fabAddNonFlying = (FloatingActionButton) findViewById(R.id.fabAddNonFlying);


        //recycler
        recyclerViewFlying = (RecyclerView) findViewById(R.id.recyclerViewFlying);
        recyclerViewFlying.setLayoutManager(new LinearLayoutManager(CustomWordsActivity.this));

        recyclerViewNonFlying = (RecyclerView) findViewById(R.id.recyclerViewNonFlying);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CustomWordsActivity.this);
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

        fabAddFlying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputAlert(true);
            }
        });

        fabAddNonFlying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputAlert(false);
            }
        });

    }

    private void showInputAlert(final Boolean isFlying) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String temp;
        if (isFlying)
            temp = "Flying Thing";
        else
            temp = "Non Flying Thing";

        builder.setTitle("Add " + temp);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        builder.setView(input);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String dialogMessage = input.getText().toString();
                if (TextUtils.isEmpty(dialogMessage)) {
                    Toast.makeText(context, getResources().getString(R.string.error_field_required), Toast.LENGTH_LONG).show();
                } else {
                    //sendQuery();
                    //Toast.makeText(context, "do something", Toast.LENGTH_LONG).show();
                    if (isFlying) {
                        dbHelper.addFlyingObject(dialogMessage);
                        arrayListFlying.add(dialogMessage);
                        adapterFlying.notifyDataSetChanged();
                    } else {
                        dbHelper.addNonFlyingObject(dialogMessage);
                        arrayListNonFlying.add(dialogMessage);
                        adapterNonFlying.notifyDataSetChanged();
                    }
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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
