package neula.sovellus;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class NeedleActivity extends Activity {
    SensorEventListener listener;
    Sensor mSensor;
    SensorManager sensorManager;

    NeedleView needleView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_needle);
        needleView = findViewById(R.id.needle_view);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                needleView.getValues(sensorEvent.values[1],sensorEvent.values[0]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
                //TODO KEK
            }
        };
        sensorManager.registerListener(listener, mSensor,SensorManager.SENSOR_DELAY_NORMAL);
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



