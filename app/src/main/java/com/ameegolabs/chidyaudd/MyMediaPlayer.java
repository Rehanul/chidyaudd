package com.ameegolabs.chidyaudd;

import android.media.MediaPlayer;

public class MyMediaPlayer {

    public MediaPlayer mp;
    public Boolean isPlaying = false;

    private static volatile MyMediaPlayer instance = null;
    private MyMediaPlayer() { }

    public static MyMediaPlayer getInstance() {
        if (instance == null) {
            synchronized (MyMediaPlayer.class) {
                if (instance == null) {
                    instance = new MyMediaPlayer();
                }
            }
        }

        return instance;
    }

}