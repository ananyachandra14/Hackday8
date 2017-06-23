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
//    float x;
//    float y;
//    float z;
//
//    float currV;
//    float prevV;

    Coordinate X;
    Coordinate Y;
    Coordinate Z;
    int counter = 0;
    boolean updateAllowed = false;

    final float ACC_THRESHOLD = 0.5f;

    public Position3D() {
        X = new Coordinate();
        Y = new Coordinate();
        Z = new Coordinate();
    }

    public void run (float ax, float ay, float az, float t) {

        updateParameters(ax, t, X);
//        updateParameters(ay, t, Y);
//        updateParameters(az, t, Z);

        if(counter == 10) {
            updateAllowed = true;
            counter = 0;
//            System.out.println("X: " + X.getValue() * 100 + "\t\t Y: " + Y.getValue() * 100 + "\t\t Z: " + Z.getValue() * 100);
        }
        counter++;
    }

    private void updateParameters(float a, float t, Coordinate C) {
        //every 0.01s
        C.getLast10msAccList().add(a);

//        System.out.println("a: " + avg_a + "\t v: " + currV);

        if (updateAllowed) {
            //every 0.1s
            float thisIterationA = getMedianAcceleration(C.getLast10msAccList()); //avg a of last 10 0.01 iterations

//            currV = 0.3f;

            C.getAccsOfLast5Iterations().add(thisIterationA);

            // thisIterationA = smoothening algo
            thisIterationA = getGuassianAcceleration(C.getAccsOfLast5Iterations());

            if (!(fallInThreshold(thisIterationA, ACC_THRESHOLD))) {
//                System.out.println("a: " + thisIterationA + "\t\t v: " + C.getCurrV() + "\t\t dir: " + direction + "\t\t follup: " + isFollowUp);
                float s = 0;
//                s = currV * t + 0.5f * thisIterationA * t * t;

                if (thisIterationA > 0) {
                    if (C.getDirection() == -1) {
                        C.setFollowUp(true);
                    }
                    if(!C.isFollowUp()) {
                        C.setDirection(1);
//                        System.out.println("a: " + thisIterationA + "\t\t +ve");
                        s = C.getCurrV() * t;
//                        s = currV * t + 0.5f * thisIterationA * t * t;
                    }
                } else if (thisIterationA < 0){
                    if (C.getDirection() == 1) {
                        C.setFollowUp(true);
                    }
                    if(!C.isFollowUp()) {
                        C.setDirection(-1);
//                        System.out.println("a: " + thisIterationA + "\t\t -ve");
//                        System.out.println("s: " + s + "\t\t -ve");
                        s = C.getCurrV() * t;
//                        s = currV * t + 0.5f * thisIterationA * t * t;
                    }
                }

                C.setValue(C.getValue() + s);
                System.out.println("X: " + C.getValue() * 100);

                C.setPrevV(C.getCurrV());
                C.setCurrV(C.getCurrV() + thisIterationA*t);
//                System.out.println("prevV: " + C.getPrevV() + "\t\t currV: " + C.getCurrV() + "\t\t bool: " + velocityChangedDirection(C.getPrevV(), C.getCurrV()));
                if (velocityChangedDirection(C.getPrevV(), C.getCurrV())) {
                    C.setDirection(0);
                    C.setFollowUp(false);
                }

            } else if (allAccFallingInThreshold(C.getAccsOfLast5Iterations())) {
                C.setCurrV(0);
                C.setDirection(0);
                C.setFollowUp(false);
            }

            if(C.getAccsOfLast5Iterations().size() == 5) {
                C.getAccsOfLast5Iterations().remove(0);
            }
        }

        if (C.getLast10msAccList().size() == 10) {
            C.getLast10msAccList().remove(0);
        }
    }

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
}
