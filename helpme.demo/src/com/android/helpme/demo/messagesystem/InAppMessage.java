package com.android.helpme.demo.messagesystem;
/**
 * 
 * @author Andreas Wieland
 *
 */
public class InAppMessage {
	public static final String LOGTAG = InAppMessage.class.getSimpleName();
	
	public InAppMessage(Object source, Object object, MESSAGE_TYPE type){
		this.source = source;
		this.object = object;
		this.type = type;
	}
	private Object object;
	private MESSAGE_TYPE type;
	private Object source;
	
	public synchronized MESSAGE_TYPE getType() {
		return type;
	}
	public synchronized void setType(MESSAGE_TYPE type) {
		this.type = type;
	}
	public synchronized Object getObject() {
		return object;
	}
	public synchronized void setObject(Object object) {
		this.object = object;
	}
	@Override
	public String toString() {
		String string = new String("Message form ");
		string += source.getClass().getSimpleName();
		string += "type: ";
		string += type.toString() + "\n";
		string += "Object: " +object.getClass().getSimpleName() +"\n";
		return string;
	}
	public synchronized Object getSource() {
		return source;
	}
	public synchronized void setSource(Object source) {
		source = source;
	}
	
	public synchronized String getSourceName() {
		return source.getClass().getSimpleName();
	}
}
