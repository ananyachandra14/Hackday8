package com.example.ananyachandra.accelerometertest;

/**
 * Created by ananya.chandra on 22/06/17.
 */

public interface AccelerometerListener {

    public void onAccelerationChanged(float x, float y, float z);

    public void onShake(float force);

}
