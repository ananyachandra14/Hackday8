package com.example.ananyachandra.accelerometertest;

import java.util.ArrayList;

/**
 * Created by ananya.chandra on 23/06/17.
 */

public class Coordinate {
    private float value;

    private float currV;
    private float prevV;

    int direction = 0;
    boolean isFollowUp = false;

    ArrayList<Float> last10msAccList = new ArrayList<>(); //a of last ten 0.01s iterations
    ArrayList<Float> accsOfLast5Iterations = new ArrayList<>(); //last 5 avg_a of each 0.1s iteration

    public Coordinate() {
        value = 0;
        currV = 0;
        prevV = 0;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isFollowUp() {
        return isFollowUp;
    }

    public void setFollowUp(boolean followUp) {
        isFollowUp = followUp;
    }

    public ArrayList<Float> getLast10msAccList() {
        return last10msAccList;
    }

    public void setLast10msAccList(ArrayList<Float> last10msAccList) {
        this.last10msAccList = last10msAccList;
    }

    public ArrayList<Float> getAccsOfLast5Iterations() {
        return accsOfLast5Iterations;
    }

    public void setAccsOfLast5Iterations(ArrayList<Float> accsOfLast5Iterations) {
        this.accsOfLast5Iterations = accsOfLast5Iterations;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getCurrV() {
        return currV;
    }

    public void setCurrV(float currV) {
        this.currV = currV;
    }

    public float getPrevV() {
        return prevV;
    }

    public void setPrevV(float prevV) {
        this.prevV = prevV;
    }
}
