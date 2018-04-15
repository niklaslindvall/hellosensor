package se.bitterhet.hellosensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {

    private TextView mHeadingData;
    private ImageView mCompassView;

    private SensorManager mSensorManager;

    private Sensor mAccelerometer;
    private float[] mLastAccData;

    private Sensor mMagnetometer;
    private float[] mLastMagData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mCompassView = findViewById(R.id.compass_view);
        mHeadingData = findViewById(R.id.heading_value_text);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mLastMagData = event.values;
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mLastAccData = event.values;
        } else {
            Log.e("Nic", "Unknown senor: " + event.sensor.getType());
        }

        if(mLastAccData != null && mLastMagData != null) {
            float azimuth = calculateTrueNorth();
            float degrees = (float) Math.toDegrees(azimuth);
            mHeadingData.setText(String.valueOf(azimuth));
            mCompassView.setRotation(degrees);
        }
    }


    private float calculateTrueNorth() {
        float R[] = new float[9];
        float I[] = new float[9];
        boolean success = SensorManager.getRotationMatrix(R, I, mLastAccData, mLastMagData);
        if (success) {
            float orientation[] = new float[3];
            SensorManager.getOrientation(R, orientation);
            // [0] Azimuth, angle of rotation about the -z axis.
            // [1] Pitch, angle of rotation about the x axis
            // [2] Roll, angle of rotation about the y axis
            return orientation[0];
        } else {
            return 0;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
