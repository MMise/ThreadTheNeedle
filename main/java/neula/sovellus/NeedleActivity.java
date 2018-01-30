package neula.sovellus;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class NeedleActivity extends AppCompatActivity {
    SensorEventListener listener;
    Sensor mGyro;
    SensorManager sensorManager;

    TextView textViewX;
    TextView textViewY;
    TextView textViewZ;

    ArrayList<Float> xList = new ArrayList<>();
    ArrayList<Float> yList = new ArrayList<>();
    ArrayList<Float> zList = new ArrayList<>();

    NeedleView needleView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_needle);

        textViewX = findViewById(R.id.textViewX);
        textViewY = findViewById(R.id.textViewY);
        textViewZ = findViewById(R.id.textViewZ);

        textViewX.setTextSize(46);
        textViewY.setTextSize(46);
        textViewZ.setTextSize(46);

        needleView = (NeedleView) findViewById(R.id.needle_view);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                xList.add(sensorEvent.values[0]);
                yList.add(sensorEvent.values[1]);
                zList.add(sensorEvent.values[2]);
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

