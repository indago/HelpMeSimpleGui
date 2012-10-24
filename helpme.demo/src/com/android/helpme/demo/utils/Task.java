/**
 * 
 */
package com.android.helpme.demo.utils;

import org.json.simple.JSONObject;

import com.android.helpme.demo.manager.PositionManager;
import com.android.helpme.demo.manager.RabbitMQManager;
import com.android.helpme.demo.manager.UserManager;
import com.android.helpme.demo.manager.interfaces.PositionManagerInterface;
import com.android.helpme.demo.manager.interfaces.RabbitMQManagerInterface;
import com.android.helpme.demo.manager.interfaces.RabbitMQManagerInterface.ExchangeType;
import com.android.helpme.demo.manager.interfaces.UserManagerInterface;
import com.android.helpme.demo.utils.position.Position;

/**
 * @author Andreas Wieland
 *
 */
public class Task {
	private UserInterface user;
	private boolean answered;
	private String exchangeName;
	private Position startPosition;
	UserManagerInterface userManagerInterface;
	RabbitMQManagerInterface rabbitMQManagerInterface;
	PositionManagerInterface positionManagerInterface;

	/**
	 * 
	 */
	public Task() {
		userManagerInterface = UserManager.getInstance();
		rabbitMQManagerInterface = RabbitMQManager.getInstance();
		positionManagerInterface = PositionManager.getInstance();
		answered = false;
		user = null;
	}

	/**
	 * gets called by Helper
	 * @param user
	 */
	public void startTask(UserInterface user) {
		run(positionManagerInterface.startLocationTracking());
		exchangeName = user.getId();
		startPosition = user.getPosition();
		run(rabbitMQManagerInterface.subscribeToChannel(exchangeName, ExchangeType.fanout));
		answered = true;
	}
	
	/**
	 * gets called by Help Seeker
	 */
	public void startTask() {
		run(positionManagerInterface.startLocationTracking());
		exchangeName = userManagerInterface.getThisUser().getId();
		run(rabbitMQManagerInterface.subscribeToChannel(exchangeName, ExchangeType.fanout));
	}
	
	public void sendPosition(Position position){
		JSONObject object = new JSONObject();
		object = UserManager.getInstance().getThisUser().getJsonObject();
		userManagerInterface.thisUser().updatePosition(position);
		object.put(User.POSITION, position.getJSON());
		
		if (answered) {
			run(rabbitMQManagerInterface.sendStringOnChannel(object.toString(), exchangeName));
		}else {
			run(rabbitMQManagerInterface.sendStringOnMain(object.toString()));
		}
	}
	
	public Boolean isAnswered(){
		return answered;
	}
	
	private void run(Runnable runnable){
		ThreadPool.runTask(runnable);
	}
	
	private void setUser(UserInterface user) {
		answered = true;
		this.user = user;
	}
	public double getDistance(){
		Position helperPosition = this.user.getPosition();
		Position ourPosition = userManagerInterface.thisUser().getPosition();
		return helperPosition.calculateSphereDistance(ourPosition);
	}
	
	public void updatePosition(UserInterface userInterface) {
		// if our Task is not answered yet, with this it is now
		if (!answered) {
			setUser(userInterface);
		}else {
			this.user.updatePosition(userInterface.getPosition());
		}
	}
	
	public void stopTask() {
		run(positionManagerInterface.stopLocationTracking());
		run(rabbitMQManagerInterface.endSubscribtionToChannel(exchangeName));
	}
}
