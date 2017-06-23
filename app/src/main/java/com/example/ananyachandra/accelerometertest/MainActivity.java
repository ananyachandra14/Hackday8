package com.example.ananyachandra.accelerometertest;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements AccelerometerListener {

    Handler handler = new Handler();
    boolean isHandlerRunning = false;
    float ax;
    float ay;
    float az;

    Position3D position3D;

    Button render;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accelerometer_example_main);

        position3D = new Position3D();

        render = (Button) findViewById(R.id.renderButton);
        render.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position3D.send();
            }
        });

        // Check onResume Method to start accelerometer listener
    }

    public void onAccelerationChanged(float x, float y, float z) {
        // TODO Auto-generated method stub

        ax = x;
        ay = y;
        az = z;

        if(!isHandlerRunning) {
            isHandlerRunning = true;
            handler.postDelayed(runnable, 10);
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            System.out.println("Ax: " + ax + "\t Ay: " + ay + "\t Az: " + az);
            position3D.run(ax, ay, az, 0.1f);
            handler.postDelayed(this, 10);
        }
    };

    public void onShake(float force) {

        // Do your stuff here

        // Called when Motion Detected
        Toast.makeText(getBaseContext(), "Motion detected",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getBaseContext(), "onResume Accelerometer Started",
                Toast.LENGTH_SHORT).show();

        //Check device supported Accelerometer senssor or not
        if (SensorManager.isSupported(this)) {

            //Start Accelerometer Listening
            SensorManager.startListening(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        //Check device supported Accelerometer senssor or not
        if (SensorManager.isListening()) {

            //Start Accelerometer Listening
            SensorManager.stopListening();

            Toast.makeText(getBaseContext(), "onStop Accelerometer Stoped",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Sensor", "Service  distroy");

        //Check device supported Accelerometer senssor or not
        if (SensorManager.isListening()) {

            //Start Accelerometer Listening
            SensorManager.stopListening();

            Toast.makeText(getBaseContext(), "onDestroy Accelerometer Stoped",
                    Toast.LENGTH_SHORT).show();
        }

    }

}
