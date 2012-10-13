package com.android.helpme.demo.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.android.helpme.demo.utils.position.Position;
import com.android.helpme.demo.utils.position.PositionInterface;



/**
 * 
 * @author Andreas Wieland
 *
 */
public class User implements UserInterface {
	private String name;
	private Boolean helfer;
	private PositionInterface position; 

	public User(String name, Boolean helfer) {
		this.name = name;
		this.helfer = helfer;
	}

	public User(JSONObject object) {
		this.name = (String) object.get(ACCOUNT);
		this.helfer = (Boolean) object.get(HELFER);
		if (object.get(PositionInterface.POSITION) != null) {
			this.position = new Position(object);
		}
	}

	/* (non-Javadoc)
	 * @see com.android.helpme.demo.utils.UserInterface#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see com.android.helpme.demo.utils.UserInterface#getHelfer()
	 */
	@Override
	public Boolean getHelfer() {
		return helfer;
	}
	
	/* (non-Javadoc)
	 * @see com.android.helpme.demo.utils.UserInterface#getPosition()
	 */
	@Override
	public PositionInterface getPosition() {
		return position;
	}

	/* (non-Javadoc)
	 * @see com.android.helpme.demo.utils.UserInterface#setPosition(com.android.helpme.demo.utils.position.Position)
	 */
	@Override
	public void setPosition(PositionInterface position) {
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

	/* (non-Javadoc)
	 * @see com.android.helpme.demo.utils.UserInterface#getJsonObject()
	 */
	@Override
	public JSONObject getJsonObject() {
		JSONObject object = new JSONObject();
		object.put(ACCOUNT, name);
		object.put(HELFER, helfer);
		object.put(PositionInterface.POSITION, position);
		return object;
	}
	
}
