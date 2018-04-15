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
    private float[] mLastAccData;

    private Sensor mMagnetometer;
    private float[] mLastMagData;


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


        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mLastMagData = event.values;
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mLastAccData = event.values;
        } else {
            Log.e("Nic", "Unknown senor: " + event.sensor.getType());
        }

        if (mLastAccData != null) {
            updateAccelerometerViews();

        }
        if (mLastAccData != null && mLastMagData != null) {
            float roll = calculatePitch();
            updatePitchView(roll);
        }
    }

    private float calculatePitch() {
        float R[] = new float[9];
        float I[] = new float[9];
        boolean success = SensorManager.getRotationMatrix(R, I, mLastAccData, mLastMagData);
        if (success) {
            float orientation[] = new float[3];
            SensorManager.getOrientation(R, orientation);
            // [0] Azimuth, angle of rotation about the -z axis.
            // [1] Pitch, angle of rotation about the x axis
            // [2] Roll, angle of rotation about the y axis
            return orientation[2];
        } else {
            return 0;
        }
    }

    private void updatePitchView(float roll) {
        if (roll < 0) {
            mDirectionTextView.setText("Left");
        } else {
            mDirectionTextView.setText("Right");
        }
    }

    private void updateAccelerometerViews() {
        float x = mLastAccData[0];
        float y = mLastAccData[1];
        float z = mLastAccData[2];
        mXTextView.setText("X: " + x);
        mYTextView.setText("Y: " + y);
        mZTextView.setText("Z: " + z);
    }
}
