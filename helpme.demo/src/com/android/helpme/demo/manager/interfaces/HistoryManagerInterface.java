/**
 * 
 */
package com.android.helpme.demo.manager.interfaces;

import java.util.ArrayList;

import android.content.Context;

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
	
	public void setContext(Context context);
	
	public Runnable saveHistory(Context applicationContext);
	public Runnable loadHistory(Context applicationContext);
}
