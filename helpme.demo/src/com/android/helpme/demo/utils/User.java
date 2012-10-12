package com.android.helpme.demo.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.android.helpme.demo.position.Position;


/**
 * 
 * @author Andreas Wieland
 *
 */
public class User {
	public static final String ACCOUNT = "ACCOUNT";
	public static final String HELFER = "HELFER";
	private String name;
	private Boolean helfer;
	private Position position; 

	public User(String name, Boolean helfer) {
		this.name = name;
		this.helfer = helfer;
	}

	public User(JSONObject object) {
		this.name = (String) object.get(ACCOUNT);
		this.helfer = (Boolean) object.get(HELFER);
		if (object.get(Position.POSITION) != null) {
			this.position = new Position(object);
		}
	}

	public String getName() {
		return name;
	}

	public Boolean getHelfer() {
		return helfer;
	}
	
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
	
	@Override
	public String toString() {
		String string = new String();
		if (helfer) {
			string += "Helfer";
		}else {
			string += "Hilfe suchender";
		}
		string += (" : "+ name);
		return string;
	}

	public JSONObject getJsonObject() {
		JSONObject object = new JSONObject();
		object.put(ACCOUNT, name);
		object.put(HELFER, helfer);
		object.put(Position.POSITION, position);
		return object;
	}
	
}
