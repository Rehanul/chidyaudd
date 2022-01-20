package com.ameegolabs.chidyaudd;

import android.content.Context;
import android.os.Handler;

import java.util.Random;


public class Gameplay {

    private String[] flyingThings;
    private String[] nonFlyingThings;
    private int EASY = 24;
    private int MEDIUM = 48;
    private int HARD = 72;

    private String question;
    private Boolean isFlyingThing;
    private int timestamp;
    private int level;
    //when level increases, timestamp reduces by (level * timeTrimRate)
    private int levelUpRate;//defines no of questions required to level up
    private int hardnessRate;//defines no of levels required to change question difficulty
    //so question becomes more difficult after levelUpRate * hardnessRate = 5 * 5 = after 25 questions
    private int timeTrimRate;
    private int count;
    private Handler handler;
    private Runnable runnable;
    private TimestampCallback timestampCallback;
    private boolean running = true;

    public Gameplay(Context context, TimestampCallback onTimestampEnd, String mode) {

        flyingThings = context.getResources().getStringArray(R.array.flying);
        nonFlyingThings = context.getResources().getStringArray(R.array.nonFlying);

        if (mode.equals("challenge")) {
            DBHelper db = new DBHelper(context);
            flyingThings = db.getAllFlyingObjects().toArray(new String[0]);
            nonFlyingThings = db.getAllNonFlyingObjects().toArray(new String[0]);

            int smallerSize = flyingThings.length < nonFlyingThings.length ? flyingThings.length : nonFlyingThings.length;
            HARD = EASY = MEDIUM = smallerSize;
        }
        question = "";
        isFlyingThing = false;
        timestamp = 1000 * 2;
        level = 0;
        levelUpRate = 3;
        hardnessRate = 5;
        timeTrimRate = 35;
        count = 0;
        handler = new Handler();
        this.timestampCallback = onTimestampEnd;

        runnable = new Runnable() {
            @Override
            public void run() {
                if (running)
                    next();
            }
        };

    }

    /*----------private functions----------*/

    private void next() {
        MyUtils.logThis("Gameplay - next");
        setQuestion();
        count++;
        if (count >= levelUpRate) {
            level++;
            count = 0;
        }
        //MyUtils.logThis("timestamp = " + (timestamp - (level * timeTrimRate)));
        timestampCallback.onTimestampFinish();
        handler.postDelayed(runnable, timestamp - (level * timeTrimRate));
    }//next

    private void setQuestion() {

        Random random = new Random();
        isFlyingThing = random.nextBoolean();
        int index;
        String[] useArray;

        if (isFlyingThing) {
            useArray = flyingThings;
        } else {
            useArray = nonFlyingThings;
        }//else

        if (level < hardnessRate) {
            index = random.nextInt(EASY);
        } else if (level < hardnessRate * 2) {
            index = random.nextInt(MEDIUM - EASY) + EASY;
        } else if (level < hardnessRate * 3) {
            index = random.nextInt(HARD - MEDIUM) + MEDIUM;
        } else {
            index = random.nextInt(HARD);
        }

        String tempQuestion = useArray[index];
        if (question.equals(tempQuestion)) {
            setQuestion();
        } else {
            question = useArray[index];
        }
    }//setQuestion

    /*----------end private functions----------*/

    public void start() {
        running = true;
        MyUtils.logThis("Gameplay - start");
        handler.post(runnable);
    }//start

    public void resume() {
        MyUtils.logThis("Gameplay - resume");
        handler.post(runnable);
    }//resume


    public void pause() {
        MyUtils.logThis("Gameplay - paused");
        running = false;
        handler.removeCallbacks(runnable);
    }//pause

    public void reset() {
        running = true;
        MyUtils.logThis("Gameplay - reset");
        question = "";
        isFlyingThing = false;
        timestamp = 1000 * 2;
        level = 0;
        levelUpRate = 5;
        hardnessRate = 5;
        timeTrimRate = 25;
        count = 0;
        handler = new Handler();
    }

    public Boolean getIsFlyingThing() {
        return isFlyingThing;
    }//getIsFlyingThing

    public String getQuestion() {
        return question;
    }//getQuestion

}
