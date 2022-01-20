package com.ameegolabs.chidyaudd;

import android.content.Context;
import android.os.Handler;


public class Countdown {

    private static final int TIMESTAMP = 1000; //1 second
    private int count;
    private String countdownText;
    private Handler handler;
    private Runnable runnable;
    private TimestampCallback onTimestampEnd;
    private TimestampCallback onCountdownFinish;
    private Context context;

    public Countdown(Context context, TimestampCallback onTimestampEnd, TimestampCallback onCountdownFinish) {
        handler = new Handler();
        this.context = context;
        this.onTimestampEnd = onTimestampEnd;
        this.onCountdownFinish = onCountdownFinish;
        countdownText = context.getString(R.string.ready);
        count = 0;

        runnable = new Runnable() {
            @Override
            public void run() {
                next();
            }
        };
    }

    private void next() {
        count++;
        //count (1 is Ready) (2 is Steady) (3 is Go)
        if (count == 1) {
            countdownText = context.getString(R.string.steady);
            onTimestampEnd.onTimestampFinish();
            handler.postDelayed(runnable, TIMESTAMP);
        } else if (count == 2) {
            countdownText = context.getString(R.string.go);
            onTimestampEnd.onTimestampFinish();
            handler.postDelayed(runnable, TIMESTAMP);
        } else {
            onCountdownFinish.onTimestampFinish();
            handler.removeCallbacks(runnable);
        }
    }

    public void pause() {
        MyUtils.logThis("Countdown - paused");
        count = 0;
        handler.removeCallbacks(runnable);
    }

    //start restart both have same meaning for countdown
    public void start() {
        MyUtils.logThis("Countdown - start");
        handler.postDelayed(runnable, TIMESTAMP);
    }

    public String getCountdownText() {
        return countdownText;
    }

}
