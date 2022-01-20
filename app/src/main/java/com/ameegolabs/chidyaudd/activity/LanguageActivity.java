package com.ameegolabs.chidyaudd.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.ameegolabs.chidyaudd.LocalDB;
import com.ameegolabs.chidyaudd.R;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    Button buttonEnglish;
    Button buttonHindi;

    Context context;
    LocalDB localDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_language);

        context = LanguageActivity.this;
        localDB = new LocalDB(context);

        buttonEnglish = (Button)findViewById(R.id.buttonEnglish);
        buttonHindi = (Button)findViewById(R.id.buttonHindi);

        buttonEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale myLocale = new Locale("en");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);
                Intent refresh = new Intent(LanguageActivity.this, SplashActivity.class);
                refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(refresh);
                localDB.setDefaultLanguage("en");
                finish();
            }
        });

        buttonHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale myLocale = new Locale("hi");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);
                Intent refresh = new Intent(LanguageActivity.this, MenuActivity.class);
                refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(refresh);
                localDB.setDefaultLanguage("hi");
                finish();
            }
        });

    }
}
