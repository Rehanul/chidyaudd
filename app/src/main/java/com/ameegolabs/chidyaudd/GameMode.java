package com.ameegolabs.chidyaudd;

import android.util.Log;
import java.util.Date;

public class GameMode {

    private int[] score;
    private Boolean[] readInput;
    private Boolean[] isInputGiven;
    private int noOfPlayers;
    private Boolean isFlyingObject;
    private int maxScore = 50;
    private Date startTime;
    private Date[] playerTime;
    private int fastest;
    private String mode;
    //only for single player v
    private Boolean player1HasLost = false;

    public GameMode(int noOfPlayers, String mode) {
        this.noOfPlayers = noOfPlayers;
        score = new int[noOfPlayers];
        readInput = new Boolean[noOfPlayers];
        isInputGiven = new Boolean[noOfPlayers];
        playerTime = new Date[noOfPlayers];
        this.mode = mode;

        for (int i = 0; i < noOfPlayers; i++) {
            readInput[i] = true;
            isInputGiven[i] = false;
            score[i] = 0;
            playerTime[i] = new Date();
        }
    }//ClassicMode

    public void actionDown(int i) {
        i--;
        if (!isInputGiven[i]) {
            readInput[i] = false;
            playerTime[i] = new Date();
            Log.d("mylog", "GameMode - player " + i + " state changed to false");
        }
        isInputGiven[i] = true;
    }//actionDown

    public void actionUp(int i) {
        i--;
        if (!isInputGiven[i]) {
            readInput[i] = true;
            playerTime[i] = new Date();
            Log.d("mylog", "GameMode - player " + i + " state changed to true");
        }
        isInputGiven[i] = true;
    }//actionUp

    public void setDefaults() {
        startTime = new Date();
        for (int i = 0; i < noOfPlayers; i++) {
            isInputGiven[i] = false;
            playerTime[i].setTime(startTime.getTime());
        }
    }//setDefaults

    public void giveScores() {

        fastest = 0;

        switch (mode) {
            case "classic":
            case "challenge":
                for (int i = 0; i < noOfPlayers; i++) {
                    if (readInput[i] == isFlyingObject) {
                        score[i]++;
                    } else {
                        if (score[i] > 0)
                            score[i]--;
                    }
                    Log.d("mylog", "ClassicMode - player " + i + " input state is " + readInput[i] + " and score is " + score[i]);
                }
                break;
            case "deathmatch":
                for (int i = 0; i < noOfPlayers; i++) {
                    if (readInput[i] == isFlyingObject) {
                        if (score[i] != -1)
                            score[i]++;
                    } else {
                        score[i] = -1;
                    }
                    Log.d("mylog", "DeathMatch - player " + i + " input state is " + readInput[i] + " and score is " + score[i]);
                }
                break;
            case "rapidfire":
                for (int i = 0; i < noOfPlayers; i++) {
                    MyUtils.logThis("player " + i + " time = " + (playerTime[i].getTime() - startTime.getTime()));
                    if (playerTime[i].getTime() == startTime.getTime()) {
                        playerTime[i].setTime(playerTime[i].getTime() + 10000);
                    }
                    //who is has minimal time taken
                    if (readInput[i] == isFlyingObject) {
                        if (playerTime[i].getTime() < playerTime[fastest].getTime()) {
                            fastest = i;
                        }
                    }
                }//end for

                //now we know player fastest has taken minimal time
                //fastest is just for comparing other player values it does not mean he is the only 1 fast
                MyUtils.logThis("fastest player is " + fastest);
                for (int i = 0; i < noOfPlayers; i++) {
                    Log.d("mylog", "Rapidfire - player " + i + " input state is " + readInput[i] + " and score is " + score[i]);
                    if (isFlyingObject) {
                        //correct input
                        if (readInput[i]) {
                            //if player time and time taken by fastest is equal then give score else take scores
                            if (playerTime[i].getTime() == playerTime[fastest].getTime()) {
                                score[i]++;
                                MyUtils.logThis("giving score to player " + i);
                            }
                        } else {
                            if (score[i] > 0)
                                score[i]--;
                        }
                    } else {
                        if (!readInput[i]) {
                            score[i]++;
                            MyUtils.logThis("giving score to player " + i);
                        } else {
                            if (score[i] > 0)
                                score[i]--;
                        }
                    }
                }
                break;

        }
    }//giveScores

    public int getScore(int playerNumber) {
        return score[playerNumber];
    }//getScore

    public void setFlyingObject(Boolean isFlyingObject) {
        this.isFlyingObject = isFlyingObject;
    }//setFlyingObject

    public boolean hasWinner() {
        int count = 0;
        switch (mode) {
            case "classic":
            case "challenge":
            case "rapidfire":
                if (noOfPlayers==1){
                    return player1HasLost;
                }
                for (int i = 0; i < noOfPlayers; i++) {
                    if (score[i] >= maxScore) {
                        return true;
                    }
                }
                return false;

            case "deathmatch":
                count = 0;
                for (int i = 0; i < noOfPlayers; i++) {
                    if (score[i] == -1) {
                        count++;
                    }
                }
                if (noOfPlayers == 1) {
                    if (score[0] == -1) {
                        return true;
                    }
                    return false;
                } else if (count > noOfPlayers - 2) {
                    Log.d("mylog", "Deathmatch - hasWinner true ; count = " + count);
                    return true;
                }
                return false;


        }
        return false;
    }//getWinner

    public boolean isWinner(int player) {

        switch (mode) {
            case "classic":
            case "challenge":
            case "rapidfire":
                MyUtils.logThis(" player " + player + " whose score = " + score[player] + " is winner ? " + (score[player] == maxScore));
                return score[player] == maxScore;

            case "deathmatch":
                return score[player] != -1;

        }

        return false;
    }//isWinner

    //only for player1
    public void setPlayer1HasLost(){
        player1HasLost = true;
    }

    public boolean isLoser(int player) {
        switch (mode) {
            case "classic":
            case "challenge":
            case "rapidfire":
                break;
            case "deathmatch":
                return score[player] == -1;
        }
        return false;
    }//isLoser

    public void reset() {
        player1HasLost = false;
        for (int i = 0; i < noOfPlayers; i++) {
            readInput[i] = true;
            isInputGiven[i] = false;
            score[i] = 0;
        }
    }

}
