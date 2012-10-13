package com.android.helpme.demo.manager.interfaces;

import java.util.ArrayList;

import android.app.Activity;

import com.android.helpme.demo.utils.User;
import com.android.helpme.demo.utils.UserInterface;

public interface UserManagerInterface {

	public  void addUser(User position);

	public  ArrayList<User> getUsers();

	public  UserInterface getUser(String userName);

	public  Runnable readUserFromProperty(Activity activity);

}