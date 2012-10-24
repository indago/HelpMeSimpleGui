package com.android.helpme.demo;

import java.util.HashMap;

import com.android.helpme.demo.gui.HelperActivity;
import com.android.helpme.demo.gui.DrawManager.DRAWMANAGER_TYPE;
import com.android.helpme.demo.manager.HistoryManager;
import com.android.helpme.demo.manager.MessageOrchestrator;
import com.android.helpme.demo.manager.PositionManager;
import com.android.helpme.demo.manager.RabbitMQManager;
import com.android.helpme.demo.manager.UserManager;
import com.android.helpme.demo.manager.interfaces.RabbitMQManagerInterface;
import com.android.helpme.demo.utils.ThreadPool;
import com.android.helpme.demo.utils.UserInterface;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service implements RabbitMQManagerInterface{
	private static final String LOGTAG = MyService.class.getSimpleName();
	private static String URL = "ec2-54-247-61-12.eu-west-1.compute.amazonaws.com";
	private NotificationManager mNM;
	private ConnectionFactory factory;
	private Connection connection;
	private HashMap<String,Channel> subscribedChannels;
	private Boolean connected = false;

	// Used to receive messages from the Activity
	final Messenger inMessenger = new Messenger(new IncomingHandler());
	// Use to send message to the Activity
	private Messenger outMessenger;

	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Log.e("MESSAGE", "Got message");
		}
	}
	// Unique Identification Number for the Notification.
	// We use it on Notification start, and to cancel it.
	private int NOTIFICATION = R.string.local_service_started;

	@Override
	public void onCreate() {
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return outMessenger.getBinder();
	}

	public class LocalBinder extends Binder {
		public MyService getService() {
			return MyService.this;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(LOGTAG, getString(R.string.local_service_started));
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		Toast.makeText(this, R.string.local_service_started, Toast.LENGTH_SHORT).show();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.i(LOGTAG, getString(R.string.local_service_stopped));
		// Cancel the persistent notification.
		mNM.cancel(NOTIFICATION);

		// Tell the user we stopped.
		Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Show a notification while this service is running.
	 */
	public void showNotification(UserInterface userInterface) {
		// In this sample, we'll use the same text for the ticker and the expanded notification
		CharSequence text = getText(R.string.this_person);
		CharSequence title = getText(R.string.new_help_request);

		// The PendingIntent to launch our activity if the user selects this notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, HelperActivity.class), 0);


		Notification notification = new Notification.Builder(this)
		.setContentTitle(title)
		.setContentText(text + userInterface.getName())
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentIntent(contentIntent)
		.build();


		// Send the notification.
		mNM.notify(NOTIFICATION, notification);
	}

	@Override
	public Runnable connect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Runnable sendStringOnMain(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Runnable sendStringOnChannel(String string, String exchangeName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Runnable sendStringToSubscribedChannels(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Runnable subscribeToMainChannel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Runnable subscribeToChannel(String exchangeName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Runnable subscribeToChannel(String exchangeName, ExchangeType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Runnable endSubscribtionToChannel(String exchangeName) {
		// TODO Auto-generated method stub
		return null;
	}
}
