package com.android.helpme.demo.manager.interfaces;

import java.util.HashMap;

import com.android.helpme.demo.gui.DrawManager;
import com.android.helpme.demo.gui.DrawManager.DRAWMANAGER_TYPE;

public interface MessageHandlerInterface {

	/**
	 * Gets all {@link DrawManager} which are associated to the Message Handler
	 * @return
	 */
	public HashMap<DrawManager.DRAWMANAGER_TYPE, DrawManager> getDrawManagers();

	/**
	 * 
	 * @param type
	 * @return
	 */
	public DrawManager getDrawManager(DRAWMANAGER_TYPE type);

	public void addDrawManager(DrawManager.DRAWMANAGER_TYPE type, DrawManager drawManager);

}