package com.ameegolabs.chidyaudd.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import com.ameegolabs.chidyaudd.R;

import java.util.ArrayList;


public class TestActivity extends AppCompatActivity {

    //UI References
    ArrayList<String> feeList;
    RecyclerView recyclerView;
    Context context;
    String[] values =
            {
                    "ANDROID",
                    "PHP",
                    "BLOGGER",
                    "WORDPRESS",
                    "JOOMLA",
                    "ASP.NET",
                    "JAVA",
                    "C++",
                    "MATHS",
                    "HINDI",
                    "ENGLISH"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_test);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        context = TestActivity.this;


        feeList = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            feeList.add(values[i]);
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }
}