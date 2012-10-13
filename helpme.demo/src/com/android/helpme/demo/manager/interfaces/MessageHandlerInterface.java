package com.android.helpme.demo.manager.interfaces;

import java.util.HashMap;

import com.android.helpme.demo.DrawManager;
import com.android.helpme.demo.DrawManager.DRAWMANAGER_TYPE;

public interface MessageHandlerInterface {

	public HashMap<DrawManager.DRAWMANAGER_TYPE, DrawManager> getDrawManagers();

	public DrawManager getDrawManager(DRAWMANAGER_TYPE type);

	public void setDrawManager(DrawManager.DRAWMANAGER_TYPE type, DrawManager drawManager);

}