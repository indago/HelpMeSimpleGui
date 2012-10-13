package com.android.helpme.demo.utils.position;

import org.json.simple.JSONObject;

public interface PositionInterface {

	public static final String POSITION = "position";
	public static final String PRECISION = "precision";
	public static final String DIRECTION = "direction";
	public static final String SPEED = "speed";
	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";
	public static final String DATE = "date";

	public double getSpeed();

	public double getDirection();

	public double getPrecision();

	public long getMeasureDateTime();

	public double getLongitude();

	public double getLatitude();

	public double calculateSphereDistance(PositionInterface other);

	public JSONObject getJSON();

}