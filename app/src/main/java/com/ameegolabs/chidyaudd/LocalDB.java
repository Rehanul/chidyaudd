package com.ameegolabs.chidyaudd;

import android.content.Context;
import android.content.SharedPreferences;


public class LocalDB {

    private static final String SP_NAME = "data";
    private SharedPreferences sp;

    public LocalDB(Context context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public void setWordsAdded() {
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putBoolean("customWords", true);
        spEditor.apply();
    }

    public boolean isWordsAdded() {
        return sp.getBoolean("customWords", false);
    }


    public void setTutorialCompleted() {
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putBoolean("tutorial", true);
        spEditor.apply();
    }


    public String getDefaultLanguage() {
        return sp.getString("language", "en");
    }

    public void setDefaultLanguage(String lang) {
        MyUtils.logThis("default language changed to " + lang);
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putString("language", lang);
        spEditor.apply();
    }

    public int getHighScore(String mode) {
        return sp.getInt(mode, 0);
    }

    public void setHighScore(String mode, int score) {
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.putInt(mode, score);
        spEditor.apply();
    }


    public Boolean isTutorialCompleted() {
        return sp.getBoolean("tutorial", false);
    }


    public void clear() {
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.clear();
        spEditor.apply();
    }


}
