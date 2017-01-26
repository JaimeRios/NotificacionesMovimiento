package com.android.jshuin.notificacionesmovimiento.Servicios;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Jshuin on 19/01/2017.
 */
public class Movimiento implements SensorEventListener {

    private static final int MOVEMENT_DURATION = 2000;

    private SensorManager miSensorMgr;
    private Sensor sensor;
    private long mLastTime;
    private Context context;


    public Movimiento(){
        miSensorMgr = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensor = miSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        miSensorMgr.registerListener(this,miSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
