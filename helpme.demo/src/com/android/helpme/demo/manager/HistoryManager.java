/**
 * 
 */
package com.android.helpme.demo.manager;

import java.util.ArrayList;

import com.android.helpme.demo.manager.interfaces.HistoryManagerInterface;
import com.android.helpme.demo.messagesystem.AbstractMessageSystem;
import com.android.helpme.demo.messagesystem.AbstractMessageSystemInterface;
import com.android.helpme.demo.messagesystem.InAppMessage;
import com.android.helpme.demo.utils.Task;
import com.android.helpme.demo.utils.UserInterface;

/**
 * @author Andreas Wieland
 *
 */
public class HistoryManager extends AbstractMessageSystem implements HistoryManagerInterface {
	private static final String LOGTAG = HistoryManager.class.getSimpleName();
	private static HistoryManager manager;
	private static Task currentTask;
	private InAppMessage message;
	
	public static HistoryManager getInstance(){
		if (manager == null) {
			manager = new HistoryManager();
		}
		return manager;
	}

	/**
	 * 
	 */
	private HistoryManager() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.android.helpme.demo.messagesystem.AbstractMessageSystemInterface#getLogTag()
	 */
	@Override
	public String getLogTag() {
		return LOGTAG;
	}

	/* (non-Javadoc)
	 * @see com.android.helpme.demo.messagesystem.AbstractMessageSystemInterface#getManager()
	 */
	@Override
	public AbstractMessageSystemInterface getManager() {
		return manager;
	}

	/* (non-Javadoc)
	 * @see com.android.helpme.demo.manager.interfaces.HistoryManagerInterface#getStatistics()
	 */
	@Override
	public ArrayList<String> getStatistics() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.android.helpme.demo.manager.interfaces.HistoryManagerInterface#getHistory()
	 */
	@Override
	public ArrayList<String> getHistory() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.android.helpme.demo.messagesystem.AbstractMessageSystem#getMessage()
	 */
	@Override
	protected InAppMessage getMessage() {
		return message;
	}

	/* (non-Javadoc)
	 * @see com.android.helpme.demo.messagesystem.AbstractMessageSystem#setMessage(com.android.helpme.demo.messagesystem.InAppMessage)
	 */
	@Override
	protected void setMessage(InAppMessage inAppMessage) {
		this.message = inAppMessage;
	}

	@Override
	public Task getTask() {
		return currentTask;
	}

	@Override
	public void startNewTask() {
		currentTask = new Task();
		currentTask.startTask();
	}
	
	@Override
	public void startNewTask(UserInterface user) {
		currentTask = new Task();
		currentTask.startTask(user);
	}
	
	@Override
	public void stopTask() {
		if (currentTask != null) {
			currentTask.stopTask();
			currentTask = null;
		}
		
	}
}
