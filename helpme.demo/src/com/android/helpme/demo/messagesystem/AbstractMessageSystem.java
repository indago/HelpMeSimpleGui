package com.android.helpme.demo.messagesystem;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
/**
 * 
 * @author Andreas Wieland
 *
 */
public abstract class AbstractMessageSystem extends PropertyChangeSupport{
	protected static InAppMessage message = new InAppMessage(null, null, MESSAGE_TYPE.NULL);
	
	public AbstractMessageSystem() {
		super(message);
	}
	
	/**
	 * sets the LogTag for each class its for debuging on android
	 * @return
	 */
	public abstract String getLogTag();
	/**
	 * gets the {@link InAppMessage} from the implementing class
	 * @return
	 */
	protected abstract InAppMessage getMessage();
	/**
	 * sets the {@link InAppMessage} for the implementing class
	 * @param message
	 */
	protected abstract void setMessage(InAppMessage inAppMessage);

	/**
	 * standard method which will fire a Error {@link InAppMessage} via {@link PropertyChangeEvent},which will invoke the Method of the {@link PropertyChangeListener}
	 * @param exception
	 */
	protected void fireError(Exception exception){
		InAppMessage newMessage = new InAppMessage(getManager(), exception, MESSAGE_TYPE.ERROR);
		InAppMessage oldMessage = getMessage();
		if (oldMessage == null) {
			oldMessage = new InAppMessage(null, null, MESSAGE_TYPE.NULL);
		}
		firePropertyChange("Message", oldMessage, newMessage);
		setMessage(newMessage);
	}
	
	public abstract AbstractMessageSystem getManager();
	
	/**
	 * standard method which will fire a new Message from the current {@link AbstractMessageSystem} via {@link PropertyChangeEvent}
	 * @param object
	 * @param type
	 */
	protected void fireMessageFromManager(Object object, MESSAGE_TYPE type) {
		InAppMessage message = new InAppMessage(getManager(), object, type);
		fireMessage(message);
	}
	
	/**
	 * standard method which will fire a normal {@link InAppMessage} via {@link PropertyChangeEvent},which will invoke the Method of the {@link PropertyChangeListener}
	 * @param newMessage
	 */
	protected void fireMessage(InAppMessage inAppMessage){
		InAppMessage oldMessage = getMessage();
		if (oldMessage == null) {
			oldMessage = new InAppMessage(null, null, MESSAGE_TYPE.NULL);
		}
		firePropertyChange("Message", oldMessage, inAppMessage);
		setMessage(inAppMessage);
	}
}
