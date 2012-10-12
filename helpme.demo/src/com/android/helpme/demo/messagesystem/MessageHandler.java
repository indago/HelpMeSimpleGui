package com.android.helpme.demo.messagesystem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Element;

import android.location.Location;
import android.util.Log;

import com.android.helpme.demo.DrawManager;
import com.android.helpme.demo.DrawManager.DRAWMANAGER_TYPE;
import com.android.helpme.demo.MainActivity;
import com.android.helpme.demo.exceptions.UnkownMessageType;
import com.android.helpme.demo.exceptions.WrongObjectType;
import com.android.helpme.demo.manager.MessageOrchestrator;
import com.android.helpme.demo.manager.PositionManager;
import com.android.helpme.demo.manager.RabbitMQManager;
import com.android.helpme.demo.position.Position;
import com.android.helpme.demo.utils.ThreadPool;
import com.android.helpme.demo.utils.User;

/**
 * 
 * @author Andreas Wieland
 * 
 */
public abstract class MessageHandler extends AbstractMessageSystem {

	abstract public DrawManager getDrawManager(DrawManager.DRAWMANAGER_TYPE type);

	abstract public HashMap<DrawManager.DRAWMANAGER_TYPE, DrawManager> getDrawManagers();

	abstract public void setDrawManager(DrawManager.DRAWMANAGER_TYPE type, DrawManager drawManager);

	abstract protected boolean reloadDatabase();

	protected void handleLocationMessage(InAppMessage message) {
		switch (message.getType()) {
		case LOCATION:
			if (!(message.getObject() instanceof Position)) {
				fireError(new WrongObjectType(message.getObject(), Position.class));
				return;
			}
			Position position = (Position) message.getObject();
			JSONObject object = new JSONObject();
			object = MainActivity.user.getJsonObject();
			object.put(Position.POSITION, position.getJSON());

			ThreadPool.runTask(PositionManager.getInstance().stopLocationTracking());
			ThreadPool.runTask(RabbitMQManager.getInstance().sendString(object.toString()));
			break;

		default:
			fireError(new UnkownMessageType());
			break;
		}
	}

	protected void handleRabbitMQMessages(InAppMessage message) {
		switch (message.getType()) {
		case CONNECTED:
			if (!(message.getObject() instanceof RabbitMQManager)) {
				fireError(new WrongObjectType(message.getObject(), RabbitMQManager.class));
				return;
			}
			ThreadPool.runTask(RabbitMQManager.getInstance().getString());
			break;
		case SEND:
			// TODO
			break;
		case STRING:
			if (!(message.getObject() instanceof String)) {
				fireError(new WrongObjectType(message.getObject(), String.class));
				return;
			}

			try {
				JSONParser parser = new JSONParser();
				JSONObject object = (JSONObject) parser.parse((String) message.getObject());
				User user = new User(object);
				if (!user.getHelfer()) {
					ThreadPool.runTask(PositionManager.getInstance().startLocationTracking());
				}
					PositionManager.getInstance().addPosition(user);
					if (getDrawManager(DRAWMANAGER_TYPE.LIST) == null) {
						getDrawManager(DRAWMANAGER_TYPE.MAIN).drawThis(object);
					}else {
						getDrawManager(DRAWMANAGER_TYPE.LIST).drawThis(object);
					}
					
//				}

			} catch (ParseException e) {
				fireError(e);
				return;
			}
			break;

		default:
			fireError(new UnkownMessageType());
			break;
		}
	}


	/**
	 * handles messages from Stomp Connection
	 * 
	 * @param message
	 */
	// protected void handleStompMessages(Message message) {
	// Element element = (Element) message.getObject();
	// Long id;
	// switch (message.getType()) {
	// case APPOINTMENT:
	// id = new Long(element.getAttribute("id"));
	// Appointment appointment = AppointmentDB.getDB().getAppointment(id);
	// appointment.setStatus(Status.valueOf(element.getAttribute("status")));
	// ThreadPool.runTask(AppointmentDB.getDB().show(id));
	// break;
	// case PREPTASK:
	// id = new Long(element.getAttribute("id"));
	// Subtask preptask = SubTaskFromAppointmentDB.getDB().getPrepTask(id);
	// preptask.setStatus(Status.valueOf(element.getAttribute("status")));
	// ThreadPool.runTask(SubTaskFromAppointmentDB.getDB().show(id));
	// break;
	// case SUBTASK:
	// id = new Long(element.getAttribute("id"));
	// Subtask subtask = SubTaskFromTaskDB.getDB().getSubtask(id);
	// subtask.setStatus(Status.valueOf(element.getAttribute("status")));
	// ThreadPool.runTask(SubTaskFromTaskDB.getDB().show(id));
	// break;
	// case TASK:
	// id = new Long(element.getAttribute("id"));
	// Task task = TaskDB.getDB().getTask(id);
	// task.setStatus(Status.valueOf(element.getAttribute("status")));
	// ThreadPool.runTask(TaskDB.getDB().show(id));
	// break;
	// case ITEM:
	// id = new Long(element.getAttribute("id"));
	// Item item = ItemDB.getDB().getItem(id);
	// item.setStatus(Status.valueOf(element.getAttribute("status")));
	// ThreadPool.runTask(ItemDB.getDB().show(id));
	// break;
	// case JOURNEY:
	// case APPOINTMENT_REMINDER:
	// case PREPTASK_REMINDER:
	// id = new Long(element.getAttribute("id"));
	// ArrayList<Status> buttons = new ArrayList<Status>();
	// if (new Boolean(element.getAttribute("startable")).booleanValue() ) {
	// buttons.add(Status.STARTABLE);
	// }
	// if (new Boolean(element.getAttribute("confirmable")).booleanValue()) {
	// buttons.add(Status.CONFIRMABLE);
	// }
	// if (new Boolean(element.getAttribute("cancelable")).booleanValue()) {
	// buttons.add(Status.CANCELABLE);
	// }
	// if (new Boolean(element.getAttribute("snoozable")).booleanValue()) {
	// buttons.add(Status.SNOOZEABLE);
	// }
	//
	// ThreadPool.runTask(SQLConnection.getConnection().getReminder(id,buttons,message.getType()));
	// break;
	// case CONFIG_CHANGE:
	// Long timestamp = new Long(element.getAttribute("timestamp"));
	// timestamp *= 1000;
	// Calendar calendar = Calendar.getInstance();
	// calendar.setTime(getDay());
	// calendar.add(Calendar.DAY_OF_YEAR, 1);
	// long day = getDay().getTime();
	// long dayafter = calendar.getTime().getTime();
	// if (timestamp >= day && timestamp <= dayafter) {
	// ThreadPool.runTask(new Runnable() {
	//
	// public void run() {
	// reloadDatabase();
	// }
	// });
	// } else {
	// int blub = 1;
	// blub++;
	// }
	// break;
	// case LOGIN:
	// // TODO login handle
	// break;
	// case LOGOUT:
	// // TODO logout handle
	// break;
	//
	// default:
	// fireError(new UnkownMessageType(STOMPConnection.class));
	// break;
	// }
	// }

}
