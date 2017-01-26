package com.android.jshuin.notificacionesmovimiento.Servicios;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Jshuin on 19/01/2017.
 */
public class Miservicio extends Service implements SensorEventListener {

    private static final int MOVEMENT_DURATION = 2000;

    private SensorManager miSensorMgr;
    private Sensor sensor;
    private long mLastTime;
    private Context context;

    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private int caso=0;
    private long totaltime;
    private long initialtime;
    private long lastregister;
    private int antirrebote;
    public float gravity[]={0,0,0};
    public float linear_acceleration[]={0,0,0};

    public Miservicio(){
        super();
        this.context=this.getApplicationContext();
        /*miSensorMgr = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensor = miSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        miSensorMgr.registerListener(this,miSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);*/

    }

    public Miservicio(Context c){
        super();
        this.context=c;
        miSensorMgr = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensor = miSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        miSensorMgr.registerListener(this,miSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        mLastTime= System.currentTimeMillis();
        lastregister=mLastTime;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){

            long now = System.currentTimeMillis();
            long delta1 = System.currentTimeMillis() - mLastTime;
            final float alpha = 0.8f;

            gravity[0] = alpha * gravity[0] + (1 - alpha) * sensorEvent.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * sensorEvent.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * sensorEvent.values[2];

            linear_acceleration[0] = sensorEvent.values[0] - gravity[0];
            linear_acceleration[1] = sensorEvent.values[1] - gravity[1];
            linear_acceleration[2] = sensorEvent.values[2] - gravity[2];

            mAccelCurrent = (float) Math.sqrt(linear_acceleration[0]*linear_acceleration[0]+linear_acceleration[1]*linear_acceleration[1]+linear_acceleration[2]*linear_acceleration[2]);
            float distance = mAccelCurrent*(delta1*1.0f/1000.0f)*(delta1*1.0f/1000.0f);
            switch (caso){
                case 0:
                    if(distance>0.05){
                        caso=1;
                        initialtime=now;
                        antirrebote=2;
                        //Toast.makeText(this.context,"Inicio evento",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    if(distance>0.05){
                        antirrebote=3;
                    }else{
                        antirrebote--;
                        if(antirrebote==0){
                            caso=0;
                            totaltime=now-initialtime;
                            initialtime=now;
                            //Toast.makeText(this.context,"Termino evento: "+totaltime,Toast.LENGTH_SHORT).show();
                            if(totaltime>MOVEMENT_DURATION){
                                ///registre el evento
                                Toast.makeText(this.context,"Ocurrio un evento. Tiempo: "+totaltime,Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
            /*float x = (1 - alpha) * sensorEvent.values[0];
            float y = (1 - alpha) * sensorEvent.values[1];
            float z = (1 - alpha) * sensorEvent.values[2];

            mAccelCurrent = (float) Math.sqrt(x*x + y*y + z*z);
            float distance = mAccelCurrent*(delta1*1.0f/1000.0f)*(delta1*1.0f/1000.0f);

            switch (caso){
                case 0:
                    if(distance>0.09){
                        caso=1;
                        initialtime=now;
                        antirrebote=2;
                        //Toast.makeText(this.context,"Inicio evento",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    if(distance>0.09){
                        antirrebote=3;
                    }else{
                        antirrebote--;
                        if(antirrebote<0){
                            caso=0;
                            totaltime=now-initialtime;
                            initialtime=now;
                            //Toast.makeText(this.context,"Termino evento: "+totaltime,Toast.LENGTH_SHORT).show();
                            if(totaltime>MOVEMENT_DURATION){
                                ///registre el evento
                                Toast.makeText(this.context,"Ocurrio un evento. Tiempo: "+totaltime,Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    break;
                default:
                    break;
            }*/
            /*if(!hayEvento){
                if(distance>0.09){
                    hayEvento=true;
                    initialtime=now;
                    antirrebote=2;
                    //Toast.makeText(this.context,"Inicio evento",Toast.LENGTH_SHORT).show();
                }
            }else{
                if(distance>0.09){
                    antirrebote=3;
                }else{
                    antirrebote--;
                    if(antirrebote<0){
                        hayEvento=false;
                        totaltime=now-initialtime;
                        initialtime=now;
                        //Toast.makeText(this.context,"Termino evento: "+totaltime,Toast.LENGTH_SHORT).show();
                        if(totaltime>MOVEMENT_DURATION){
                            ///registre el evento
                            Toast.makeText(this.context,"Ocurrio un evento. Tiempo: "+totaltime+". ultimo registro hace: "+(lastregister-now),Toast.LENGTH_SHORT).show();
                            lastregister=now;
                        }
                    }
                }
            }*/
            mLastTime =now;

       }else{
            return;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
