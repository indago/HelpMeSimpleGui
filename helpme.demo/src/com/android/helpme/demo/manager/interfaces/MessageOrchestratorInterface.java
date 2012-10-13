package com.android.helpme.demo.manager.interfaces;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.android.helpme.demo.messagesystem.AbstractMessageSystem;

public interface MessageOrchestratorInterface extends PropertyChangeListener, MessageHandlerInterface{

	public void listenToMessageSystem(AbstractMessageSystem messageSystem);

}