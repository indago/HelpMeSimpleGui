/**
 * 
 */
package com.android.helpme.demo.utils;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Andreas Wieland
 *
 */
public class Message implements MessageInterface {
	public enum HelpRequestType{ REQUEST, ACKNOWLEDGEMENT};
	public static final String ID = "ID";
	private String id;
	private HelpRequestType type;

	/**
	 * 
	 */
	public Message(String id, HelpRequestType type) {
		this.id = id;
		this.type = type;
	}
	
	public static Message generateHelpRequest(){
		String id = new String();
		id += UUID.randomUUID();
		Message request= new Message(id, HelpRequestType.REQUEST);
		return request;
	}
	
	/* (non-Javadoc)
	 * @see com.android.helpme.demo.utils.HelpRequestInterface#toJSONObject()
	 */
	@Override
	public JSONObject toJSONObject() throws JSONException{
		JSONObject object = new JSONObject();
		
		object.put(type.name(), type);
		object.put(ID, id);
		
		return object;
	}

	/* (non-Javadoc)
	 * @see com.android.helpme.demo.utils.HelpRequestInterface#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see com.android.helpme.demo.utils.HelpRequestInterface#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see com.android.helpme.demo.utils.HelpRequestInterface#getType()
	 */
	@Override
	public HelpRequestType getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see com.android.helpme.demo.utils.HelpRequestInterface#setType(com.android.helpme.demo.utils.HelpRequest.HelpRequestType)
	 */
	@Override
	public void setType(HelpRequestType type) {
		this.type = type;
	}

}
