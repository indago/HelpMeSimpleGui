package com.android.helpme.demo.utils;

import org.json.simple.JSONObject;

import com.android.helpme.demo.utils.position.PositionInterface;

public interface UserInterface {

	public static final String ACCOUNT = "ACCOUNT";
	public static final String HELFER = "HELFER";

	public String getName();

	public Boolean getHelfer();

	public PositionInterface getPosition();

	public void setPosition(PositionInterface position);

	public JSONObject getJsonObject();

}