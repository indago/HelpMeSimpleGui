package com.android.helpme.demo.manager.interfaces;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;

import com.android.helpme.demo.utils.User;
import com.android.helpme.demo.utils.UserInterface;

public interface UserManagerInterface {

	public  boolean addUser(User position);

	public  ArrayList<User> getUsers();

	public  UserInterface getUserByName(String userName);
	public  UserInterface getUserById(String id);

	public  Runnable readUserFromProperty(Context context);
	
	public UserInterface thisUser();
	
	public UserInterface getThisUser();

}