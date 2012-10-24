/**
 * 
 */
package com.android.helpme.demo.manager.interfaces;

import java.util.ArrayList;

import com.android.helpme.demo.utils.Task;
import com.android.helpme.demo.utils.UserInterface;

/**
 * @author Andreas Wieland
 *
 */
public interface HistoryManagerInterface {
	
	public ArrayList<String> getStatistics();
	
	public ArrayList<String> getHistory();

	public Task getTask();
	
	public void startNewTask();
	
	public void startNewTask(UserInterface user);
	
	public void stopTask();
}
