package com.android.helpme.demo.manager.interfaces;

import java.util.HashMap;

import com.android.helpme.demo.gui.DrawManager;
import com.android.helpme.demo.gui.DrawManager.DRAWMANAGER_TYPE;
import com.android.helpme.demo.manager.MessageHandler;

public interface MessageHandlerInterface {

	/**
	 * Gets all {@link DrawManager} which are associated to the Message Handler
	 * @return
	 */
	public HashMap<DrawManager.DRAWMANAGER_TYPE, DrawManager> getDrawManagers();

	/**
	 * returns associated {@link DrawManager} with {@link DRAWMANAGER_TYPE}
	 * @param type
	 * @return
	 */
	public DrawManager getDrawManager(DRAWMANAGER_TYPE type);

	/**
	 * adds a new {@link DrawManager} with {@link DRAWMANAGER_TYPE} to {@link MessageHandler}
	 * @param type
	 * @param drawManager
	 */
	public void addDrawManager(DrawManager.DRAWMANAGER_TYPE type, DrawManager drawManager);

}