package com.android.helpme.demo.utils;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.helpme.demo.utils.Message.HelpRequestType;

public interface MessageInterface {

	public JSONObject toJSONObject() throws JSONException;

	public String getId();

	public void setId(String id);

	public HelpRequestType getType();

	public void setType(HelpRequestType type);
	
}