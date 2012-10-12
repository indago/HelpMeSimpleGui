/*
 * Copyright (C) 2011-2012 AlarmApp.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.helpme.demo.position;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.simple.JSONObject;

import android.R.dimen;
import android.location.Location;
import android.os.Bundle;

public class Position implements Serializable {
	public static final String POSITION = "position";
	 public static final String PRECISION = "precision";
	 public static final String DIRECTION = "direction";
	 public static final String SPEED = "speed";
	 public static final String LONGITUDE = "longitude";
	 public static final String LATITUDE = "latitude";
	 public static final String DATE = "date";

	private double longitude;
	private double latitude;
	private double speed;
	private double direction;
	private double precision;
	private long date;

	// private String operationId;
	
	public Position(Location location){
		this.longitude =  location.getLongitude();
		this.latitude = location.getLatitude();
		this.speed = location.getSpeed();
		this.direction = location.getBearing();
		this.precision = location.getAccuracy();
		this.date = location.getTime();
	}

	public Position(double lon, double lat, float speed, float direction, float precision, long date) {
		// this.operationId = OperationId;
		this.longitude = lon;
		this.latitude = lat;
		this.speed = speed;
		this.direction = direction;
		this.precision = precision;
		this.date = date;
	}

//	public LonLatData getPosition() {
//		return position;
//	}

	public Position(JSONObject object) {
		JSONObject position = (JSONObject) object.get(POSITION);
		this.longitude = (Double)position.get(LONGITUDE);
		this.latitude = (Double)position.get(LATITUDE);
		this.speed = (Double)position.get(SPEED);
		this.direction = (Double)position.get(DIRECTION);
		this.precision = (Double)position.get(PRECISION);
		this.date = (Long)position.get(DATE);
	}

	public double getSpeed() {
		return speed;
	}

	public double getDirection() {
		return direction;
	}

	public double getPrecision() {
		return precision;
	}

	public long getMeasureDateTime() {
		return date;
	}
	
	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}
	
	public double calculateSphereDistance(Position other) {

		double earthRadius = 3958.75;
		double dLat = Math.toRadians(this.latitude - other.getLatitude());
		double dLng = Math.toRadians(this.longitude - other.getLongitude());
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(this.latitude))
				* Math.cos(Math.toRadians(other.getLatitude()))
				* Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		int meterConversion = 1609;

		return  (dist * meterConversion);
	}

	// public static WayPointData create(Bundle bundle) {
	// Ensure.bundleHasKeys(bundle, DATE, OPERATION_ID, LAT, LON, SPEED,
	// DIRECTION, PRECISION, MEASUREMENT_METHOD);
	//
	// LonLat pos = new LonLatData(bundle.getFloat(LON), bundle.getFloat(LAT));
	// PositionMeasurementMethod src =
	// PositionMeasurementMethod.Create(bundle.getString(MEASUREMENT_METHOD));
	// Date date = DateUtil.parse(bundle.getString(DATE));
	// return new WayPointData(bundle.getString(OPERATION_ID), pos,
	// bundle.getFloat(SPEED), bundle.getFloat(DIRECTION),
	// bundle.getFloat(PRECISION), src, date);
	//
	// }

	// public static boolean isWayPointBundle(Bundle bundle) {
	// return BundleUtil.containsAll(bundle, OPERATION_ID, DATE, LAT, LON,
	// SPEED, DIRECTION, PRECISION, MEASUREMENT_METHOD);
	// }

	// public Bundle getBundle() {
	// Bundle b = new Bundle();
	// b.putString(OPERATION_ID, this.operationId);
	// b.putString(DATE, DateUtil.format(this.date));
	// b.putFloat(LAT, position.getLatitude());
	// b.putFloat(LON, position.getLongitude());
	// b.putFloat(SPEED, speed);
	// b.putFloat(DIRECTION, direction);
	// b.putFloat(PRECISION, precision);
	// b.putString(MEASUREMENT_METHOD, measurementMethod.toString());
	// return b;
	// }

	// public String getOperationId() {
	// return this.operationId;
	// }
	
	public JSONObject getJSON(){
		JSONObject object = new JSONObject();
		object.put(LONGITUDE, this.longitude);
		object.put(LATITUDE, this.latitude);
		object.put(SPEED, this.speed);
		object.put(DIRECTION, this.direction);
		object.put(PRECISION, this.precision);
		object.put(DATE, this.date);
		return object;
	}
	
	@Override
	public String toString() {
		String string = new String();
		string += LONGITUDE +" : " + this.longitude + "\n";
		string += LATITUDE +" : "+ this.latitude + "\n";
		string += SPEED +" : "+ this.speed +"\n";
		string += DIRECTION +" : "+ this.direction +"\n";
		string += PRECISION +" : "+ this.precision +"\n";
		string += DATE +" : "+DateFormat.getDateInstance(DateFormat.FULL).format(new Date(date)) +"\n";
		
		return string;
	}
}
