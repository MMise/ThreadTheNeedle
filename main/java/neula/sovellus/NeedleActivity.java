package neula.sovellus;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NeedleActivity extends AppCompatActivity {
    SensorEventListener listener;
    Sensor mGyro;
    SensorManager sensorManager;

    NeedleView needleView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_needle);
        needleView = findViewById(R.id.needle_view);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                needleView.getValues(sensorEvent.values[0],sensorEvent.values[1]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                //TODO KEK
            }
        };
        sensorManager.registerListener(listener,mGyro,SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStart(){
        super.onStart();
        needleView.startThread();
    }

    public void onStop(){
        super.onStop();
        needleView.stopThread();
    }
}



