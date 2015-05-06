package com.group_downstair.main;

import engine.Physical;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MySensor {
	private SensorManager sensorMgr;
	private Sensor aSensor;
	private float gravity[] = new float[3];
	MySensor(Object object) {
		sensorMgr = (SensorManager)object;
		aSensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorMgr.registerListener(listener, aSensor, sensorMgr.SENSOR_DELAY_GAME);
	}
	
	public void onPause() {
		sensorMgr.unregisterListener(listener);
	}
	public float getForceX() {
		return gravity[0]*2;
	}
	private SensorEventListener listener = new SensorEventListener(){

		public void onSensorChanged(SensorEvent event) {
			gravity[0] = event.values[0];
			gravity[1] = event.values[1];
			gravity[2] = event.values[2];
			//Physical.setForceX(gravity[0]);
			
		}
		public void onAccuracyChanged(android.hardware.Sensor sensor,
				int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};
}
