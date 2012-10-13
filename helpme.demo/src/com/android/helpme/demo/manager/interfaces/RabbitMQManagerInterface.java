package com.android.helpme.demo.manager.interfaces;

public interface RabbitMQManagerInterface {

	public  Runnable connect();

	public  Runnable sendString(String string);

	public  Runnable getString();

}