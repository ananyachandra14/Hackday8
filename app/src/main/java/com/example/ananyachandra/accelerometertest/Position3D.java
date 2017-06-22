package com.example.ananyachandra.accelerometertest;

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

    float prevV;

    ArrayList<Float> last10msAccList = new ArrayList<>(); //a of last ten 0.01s iterations
    int counter = 0;
    boolean updateAllowed = false;

    ArrayList<Float> accsOfLast5Iterations = new ArrayList<>(); //last 5 avg_a of each 0.1s iteration

    float THRESHOLD = 0.3f;

    public Position3D() {
        x = 0;
        y = 0;
        z = 0;
        prevV = 0;
    }

    public void updateParameters(float a, float t) {
        //every 0.01s
        last10msAccList.add(a);

//        System.out.println("a: " + avg_a + "\t v: " + prevV);

        if(counter == 10) {
            updateAllowed = true;
            counter = 0;
        }
        counter++;

        if (updateAllowed) {
            //every 0.1s
            updateAllowed = false;
            float thisIterationA = getMedianAcceleration(last10msAccList); //avg a of last 10 0.01 iterations

            prevV = 0.1f;

            accsOfLast5Iterations.add(thisIterationA);

            // thisIterationA = smoothening algo
            thisIterationA = getGuassianAcceleration(accsOfLast5Iterations);
            //System.out.println("a: " + thisIterationA + "\t\t v: " + prevV);

            if (!(fallInThreshold(thisIterationA))) {
                //System.out.println("a: " + thisIterationA + "\t\t v: " + prevV);
                float s = 0;
//                s = prevV * t + 0.5f * thisIterationA * t * t;

                if (thisIterationA > 0) {
                    s = prevV * t;
                } else if (thisIterationA < 0){
                    s = -1*prevV * t;
                }

                x += s;

//                prevV = prevV + thisIterationA * t;

            } else if (allAccFallingInThreshold(accsOfLast5Iterations)) {
                prevV = 0;
            }

            System.out.println("X: " + x * 100);

            if(accsOfLast5Iterations.size() == 5) {
                accsOfLast5Iterations.remove(0);
            }
        }

        if (last10msAccList.size() == 10) {
            last10msAccList.remove(0);
        }
    }


    private boolean allAccFallingInThreshold (ArrayList<Float> arrayList) {
        boolean flag = true;
        for (float a : arrayList) {
            if (!fallInThreshold(a)) {
                flag = false;
            }
        }
        return flag;
    }

    private boolean fallInThreshold (float a) {
        return (-1*THRESHOLD < a && a < THRESHOLD);
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

    public float getPrevV() {
        return prevV;
    }
}
