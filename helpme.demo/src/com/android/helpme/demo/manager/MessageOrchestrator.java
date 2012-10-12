package com.android.helpme.demo.manager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.android.helpme.demo.DrawManager;
import com.android.helpme.demo.DrawManager.DRAWMANAGER_TYPE;
import com.android.helpme.demo.exceptions.WrongObjectType;
import com.android.helpme.demo.messagesystem.AbstractMessageSystem;
import com.android.helpme.demo.messagesystem.InAppMessage;
import com.android.helpme.demo.messagesystem.MESSAGE_TYPE;
import com.android.helpme.demo.messagesystem.MessageHandler;

import android.location.Location;
import android.util.Log;


public class MessageOrchestrator extends MessageHandler implements PropertyChangeListener {
	private static final String LOGTAG = MessageOrchestrator.class.getSimpleName();
	private static MessageOrchestrator messageOrchestrator;
	private InAppMessage message;
	private ArrayList<String> arrayList;
	private HashMap<DrawManager.DRAWMANAGER_TYPE, DrawManager> drawManagerMap;
	

	public static MessageOrchestrator getInstance(){
		if (messageOrchestrator == null) {
			messageOrchestrator = new MessageOrchestrator();
		}
		return messageOrchestrator;
	}

	private MessageOrchestrator() {
		arrayList = new ArrayList<String>();
		drawManagerMap = new HashMap<DrawManager.DRAWMANAGER_TYPE, DrawManager>();
	}
	
	public static void listenToMessageSystem(AbstractMessageSystem messageSystem) {
		if (!(getInstance().arrayList.contains(messageSystem.getLogTag()))) {
			messageSystem.addPropertyChangeListener(getInstance());
			getInstance().arrayList.add(messageSystem.getLogTag());
		};
	}

	@Override
	protected boolean reloadDatabase() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public String getLogTag() {
		return LOGTAG;
	}

	@Override
	protected InAppMessage getMessage() {
		return message;
	}

	@Override
	protected void setMessage(InAppMessage message) {
		this.message = message;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (!(event.getNewValue() instanceof InAppMessage)) {
			fireError(new WrongObjectType(event, InAppMessage.class));
		}
		InAppMessage message = (InAppMessage) event.getNewValue();
		if (message.getType() == MESSAGE_TYPE.ERROR) {
			Log.e(((AbstractMessageSystem)message.getSource()).getLogTag(), ((Exception)message.getObject()).toString());
//		}
//		else if (message.getSource() instanceof AmazonClientManager) {
//			handleAmazonClientMenagerMessages(message);
//		}else if (message.getSource() instanceof SQSManager) {
//			handleSQSMessages(message);
//		}else if (message.getSource() instanceof SNSManager) {
//			handleSNSMessages(message);
		}else if (message.getSource() instanceof RabbitMQManager) {
			handleRabbitMQMessages(message);
		}else if (message.getSource() instanceof PositionManager) {
			handleLocationMessage(message);
		}
	}

	@Override
	public AbstractMessageSystem getManager() {
		return messageOrchestrator;
	}

	@Override
	public HashMap<DrawManager.DRAWMANAGER_TYPE, DrawManager> getDrawManagers() {
		return drawManagerMap;
	}
	
	@Override
	public DrawManager getDrawManager(DRAWMANAGER_TYPE type) {
		return drawManagerMap.get(type);
	}

	@Override
	public void setDrawManager(DrawManager.DRAWMANAGER_TYPE type,DrawManager drawManager) {
		this.drawManagerMap.put(type, drawManager);	
	}
	
}
