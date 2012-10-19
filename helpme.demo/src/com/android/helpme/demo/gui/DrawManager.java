package com.android.helpme.demo.gui;

public interface DrawManager {
	public enum DRAWMANAGER_TYPE {
		SEEKER, LIST, LOGIN, MAP;
	}
	public void drawThis(Object object);

}
