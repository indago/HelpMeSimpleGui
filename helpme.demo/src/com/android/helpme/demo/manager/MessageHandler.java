package com.android.helpme.demo.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Element;

import android.content.res.Resources.Theme;
import android.graphics.Path.Direction;
import android.location.Location;
import android.util.Log;

import com.android.helpme.demo.exceptions.UnkownMessageType;
import com.android.helpme.demo.exceptions.WrongObjectType;
import com.android.helpme.demo.gui.DrawManager;
import com.android.helpme.demo.gui.SeekerActivity;
import com.android.helpme.demo.gui.DrawManager.DRAWMANAGER_TYPE;
import com.android.helpme.demo.manager.interfaces.MessageHandlerInterface;
import com.android.helpme.demo.manager.interfaces.MessageOrchestratorInterface;
import com.android.helpme.demo.manager.interfaces.RabbitMQManagerInterface;
import com.android.helpme.demo.manager.interfaces.RabbitMQManagerInterface.ExchangeType;
import com.android.helpme.demo.messagesystem.AbstractMessageSystem;
import com.android.helpme.demo.messagesystem.InAppMessage;
import com.android.helpme.demo.utils.ThreadPool;
import com.android.helpme.demo.utils.User;
import com.android.helpme.demo.utils.position.Position;
import com.android.helpme.demo.utils.position.PositionInterface;

/**
 * 
 * @author Andreas Wieland
 * 
 */
public abstract class MessageHandler extends AbstractMessageSystem implements MessageHandlerInterface{

	abstract protected boolean reloadDatabase();

	/**
	 * Handels the Messages form the {@link PositionManager}
	 * @param message
	 */
	protected void handlePositionMessage(InAppMessage message) {
		switch (message.getType()) {
		case LOCATION:
			if (!(message.getObject() instanceof Position)) {
				fireError(new WrongObjectType(message.getObject(), Position.class));
				return;
			}
			Position position = (Position) message.getObject();
			JSONObject object = new JSONObject();
			object = UserManager.getInstance().getThisUser().getJsonObject();
			UserManager.getInstance().getThisUser().updatePosition(position);
			object.put(User.POSITION, position.getJSON());

			//ThreadPool.runTask(PositionManager.getInstance().stopLocationTracking());
			if (UserManager.getInstance().thisUser().getHelfer()) {
				ThreadPool.runTask(RabbitMQManager.getInstance().sendStringToSubscribedChannels(object.toString()));
			}else {
				ThreadPool.runTask(RabbitMQManager.getInstance().sendStringToSubscribedChannels(object.toString()));
				ThreadPool.runTask(RabbitMQManager.getInstance().sendStringOnMain(object.toString()));
			}
			
			break;

		default:
			fireError(new UnkownMessageType());
			break;
		}
	}

	/**
	 * Handels the Messages form the {@link RabbitMQManager} 
	 * @param message
	 */
	protected void handleRabbitMQMessages(InAppMessage message) {
		switch (message.getType()) {
		case CONNECTED:
			if (!(message.getObject() instanceof RabbitMQManagerInterface)) {
				fireError(new WrongObjectType(message.getObject(), RabbitMQManager.class));
				return;
			}
			ThreadPool.runTask(RabbitMQManager.getInstance().subscribeToMainChannel());
			break;

		case USER:
			if (!(message.getObject() instanceof User)) {
				fireError(new WrongObjectType(message.getObject(), User.class));
				return;
			}
			User user = (User) message.getObject();

			if (UserManager.getInstance().getThisUser().getHelfer()) {
				handleIncomingUserAsHelper(user);
			}else {
				handleIncomingUserAsHelperSeeker(user);
			}

			break;

		default:
			fireError(new UnkownMessageType());
			break;
		}
	}

	/**
	 * if this user is a Helper this Method will be called and starts the List {@link DrawManager}
	 * @param incomingUser
	 */
	private void handleIncomingUserAsHelper(User incomingUser){
		ThreadPool.runTask(PositionManager.getInstance().startLocationTracking());
		ThreadPool.runTask(RabbitMQManager.getInstance().subscribeToChannel(incomingUser.getId(), ExchangeType.driect));

		UserManager.getInstance().addUser(incomingUser);
		if (getDrawManager(DRAWMANAGER_TYPE.LIST) == null) {
			getDrawManager(DRAWMANAGER_TYPE.SEEKER).drawThis(incomingUser);
		} else {
			getDrawManager(DRAWMANAGER_TYPE.LIST).drawThis(incomingUser);
		}
	}

	/**
	 * if this user is a Help Seeker this Method will be called and starts the Map {@link DrawManager}
	 * @param incomingUser
	 */
	private void handleIncomingUserAsHelperSeeker(User incomingUser){
		UserManager.getInstance().addUser(incomingUser);
		if (getDrawManager(DRAWMANAGER_TYPE.MAP) == null) {
			getDrawManager(DRAWMANAGER_TYPE.SEEKER).drawThis(incomingUser);
		} else {
			getDrawManager(DRAWMANAGER_TYPE.MAP).drawThis(incomingUser);
		}
	}

	/**
	 * Handles Messages from the {@link UserManager}
	 * @param message
	 */
	protected void handleUserMessages(InAppMessage message) {
		switch (message.getType()) {
		case USER:
			if (!(message.getObject() instanceof ArrayList<?>)) {
				fireError(new WrongObjectType(message.getObject(), ArrayList.class));
				return;
			}

			getDrawManager(DRAWMANAGER_TYPE.LOGIN).drawThis(message.getObject());
			break;

		default:
			fireError(new UnkownMessageType());
			break;
		}
	}
}
