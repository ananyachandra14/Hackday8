package com.example.ananyachandra.accelerometertest;

import android.os.Handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by ananya.chandra on 22/06/17.
 */

//Positions in m in the real 3D world
public class Position3D {
    float x;
    float y;
    float z;

    float currV;
    float prevV;

    int direction = 0;
    boolean isFollowUp = false;

    Handler directionChangeHandler = new Handler();

    ArrayList<Float> last10msAccList = new ArrayList<>(); //a of last ten 0.01s iterations
    int counter = 0;
    boolean updateAllowed = false;

    ArrayList<Float> accsOfLast5Iterations = new ArrayList<>(); //last 5 avg_a of each 0.1s iteration

    float ACC_THRESHOLD = 0.5f;

    public Position3D() {
        x = 0;
        y = 0;
        z = 0;
        currV = 0;
    }

    public void updateParameters(float a, float t) {
        //every 0.01s
        last10msAccList.add(a);

//        System.out.println("a: " + avg_a + "\t v: " + currV);

        if(counter == 10) {
            updateAllowed = true;
            counter = 0;
        }
        counter++;

        if (updateAllowed) {
            //every 0.1s
            updateAllowed = false;
            float thisIterationA = getMedianAcceleration(last10msAccList); //avg a of last 10 0.01 iterations

//            currV = 0.3f;

            accsOfLast5Iterations.add(thisIterationA);

            // thisIterationA = smoothening algo
            thisIterationA = getGuassianAcceleration(accsOfLast5Iterations);
            //System.out.println("a: " + thisIterationA + "\t\t v: " + currV);

            if (!(fallInThreshold(thisIterationA, ACC_THRESHOLD))) {
//                System.out.println("a: " + thisIterationA + "\t\t v: " + currV);
                float s = 0;
//                s = currV * t + 0.5f * thisIterationA * t * t;

                if (thisIterationA > 0) {
                    if (direction == -1) {
                        isFollowUp = true;
                    }
                    if(!isFollowUp) {
                        direction = 1;
//                        System.out.println("a: " + thisIterationA + "\t\t +ve");
                        s = currV * t;
//                        s = currV * t + 0.5f * thisIterationA * t * t;
                    }
                } else if (thisIterationA < 0){
                    if (direction == 1) {
                        isFollowUp = true;
                    }
                    if(!isFollowUp) {
                        direction = -1;
//                        System.out.println("a: " + thisIterationA + "\t\t -ve");
                        s = currV * t;
//                        s = currV * t + 0.5f * thisIterationA * t * t;
                    }
                }

                x += s;
                System.out.println("X: " + x * 100);

                prevV = currV;
                currV = currV + thisIterationA * t;
//                System.out.println("prevV: " + prevV + "\t\t currV: " + currV + "\t\t bool: " + velocityChangedDirection(prevV, currV));
                if (velocityChangedDirection(prevV, currV)) {
                    direction = 0;
                    isFollowUp = false;
                }

            } else if (allAccFallingInThreshold(accsOfLast5Iterations)) {
                currV = 0;
                direction = 0;
                isFollowUp = false;
            }

            if(accsOfLast5Iterations.size() == 5) {
                accsOfLast5Iterations.remove(0);
            }
        }

        if (last10msAccList.size() == 10) {
            last10msAccList.remove(0);
        }
    }

    Runnable directionChangeRunnable = new Runnable() {
        @Override
        public void run() {

        }
    };

    private boolean allAccFallingInThreshold (ArrayList<Float> arrayList) {
        boolean flag = true;
        for (float a : arrayList) {
            if (!fallInThreshold(a, ACC_THRESHOLD)) {
                flag = false;
            }
        }
        return flag;
    }

    private boolean fallInThreshold (float a, float threshold) {
        return (-1*threshold < a && a < threshold);
    }

    private boolean velocityChangedDirection(float prevV, float currV) {
        if (prevV <= 0 && currV >= 0)
            return true;
        else if (prevV >= 0 && currV <= 0)
            return true;
        else
            return false;
    }

    private float getAverageAcceleration(ArrayList<Float> arrayList) {
        float sum_a = 0;
        for (float a : arrayList) {
            sum_a += a;
        }
        return sum_a/arrayList.size();
    }

    private float getMedianAcceleration(ArrayList<Float> arrayList) {
        ArrayList<Float> list = new ArrayList<>(arrayList);
        Collections.sort(list);

        return list.get(list.size()/2);

    }

    private float getGuassianAcceleration(ArrayList<Float> arrayList) {
        List<Float> weights = Arrays.asList(0f, 0.1f, 0.2f, 0.3f, 0.4f);
        float gaussian = 0;
        for (int i=0; i<arrayList.size(); i++) {
            gaussian += arrayList.get(i) * weights.get(i);
        }

        return gaussian;
    }


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getCurrV() {
        return currV;
    }
}
