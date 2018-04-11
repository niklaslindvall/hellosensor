package se.bitterhet.hellosensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;


    private TextView mXTextView;
    private TextView mYTextView;
    private TextView mZTextView;
    private TextView mDirectionTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mXTextView = findViewById(R.id.text_x);
        mYTextView = findViewById(R.id.text_y);
        mZTextView = findViewById(R.id.text_z);
        mDirectionTextView = findViewById(R.id.text_direction);


        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onSensorChanged(SensorEvent event) {
      if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];
            mXTextView.setText("X: " + x);
            mYTextView.setText("Y: " + y);
            mZTextView.setText("Z: " + z);
        } else {
            Log.e("Nic","Unknown senor: " + event.sensor.getType());
        }
    }
}
