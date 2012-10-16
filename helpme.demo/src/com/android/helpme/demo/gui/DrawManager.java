package com.android.helpme.demo.gui;

public interface DrawManager {
	public enum DRAWMANAGER_TYPE {
		MAIN, LIST, LOGIN;
	}
	public void drawThis(Object object);

}
