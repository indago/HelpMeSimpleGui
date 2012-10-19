package com.android.helpme.demo.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.util.Log;

import com.android.helpme.demo.manager.interfaces.RabbitMQManagerInterface;
import com.android.helpme.demo.messagesystem.AbstractMessageSystem;
import com.android.helpme.demo.messagesystem.AbstractMessageSystemInterface;
import com.android.helpme.demo.messagesystem.InAppMessage;
import com.android.helpme.demo.messagesystem.inAppMessageType;
import com.android.helpme.demo.utils.ThreadPool;
import com.android.helpme.demo.utils.User;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.AMQP.BasicProperties;

public class RabbitMQManager extends AbstractMessageSystem implements RabbitMQManagerInterface, ShutdownListener {
	public static final String LOGTAG = RabbitMQManager.class.getSimpleName();
	private static String URL = "ec2-54-247-61-12.eu-west-1.compute.amazonaws.com";
	private static RabbitMQManager rabbitMQManager;

	private InAppMessage message;
	private ConnectionFactory factory;
	private Connection connection;
	private HashMap<String,Channel> subscribedChannels;
	private Boolean connected = false;



	public static RabbitMQManager getInstance() {
		if (rabbitMQManager == null) {
			rabbitMQManager = new RabbitMQManager();
		}
		return rabbitMQManager;
	}

	/* (non-Javadoc)
	 * @see com.android.helpme.demo.manager.RabbitMQManagerInterface#connect()
	 */
	@Override
	public Runnable connect(){
		return new Runnable() {

			@Override
			public void run() {
				if (connected) {
					return;
				}
				try {
					connection = factory.newConnection();
					connected = true;
					Log.i(LOGTAG, "connected to rabbitMQ");
					fireMessageFromManager(rabbitMQManager, inAppMessageType.CONNECTED);
				} catch (IOException e) {
					fireError(e);
				}

			}
		};

	}

	private RabbitMQManager() {
		factory  = new ConnectionFactory();
		factory.setHost(URL);
		subscribedChannels = new HashMap<String, Channel>();
	}

	/* (non-Javadoc)
	 * @see com.android.helpme.demo.manager.RabbitMQManagerInterface#sendString(java.lang.String)
	 */
	@Override
	public Runnable sendStringOnMain(final String string) {
		return sendStringOnChannel(string, "main");
	}

	/* (non-Javadoc)
	 * @see com.android.helpme.demo.manager.RabbitMQManagerInterface#getString()
	 */
	@Override
	public Runnable subscribeToMainChannel() {
		return subscribeToChannel("main");
	}

	@Override
	public String getLogTag() {
		return LOGTAG;
	}

	@Override
	protected InAppMessage getMessage() {
		return message;
	}

	@Override
	protected void setMessage(InAppMessage inAppMessage) {
		message = inAppMessage;

	}

	@Override
	public AbstractMessageSystemInterface getManager() {
		return rabbitMQManager;
	}

	@Override
	public Runnable subscribeToChannel(String exchangeName){
		return subscribeToChannel(exchangeName, ExchangeType.fanout);
	}

	@Override
	public Runnable subscribeToChannel(final String exchangeName, final ExchangeType type){
		return new Runnable() {

			@Override
			public void run() {
				try {
					// we create a new channel 
					Channel channel = connection.createChannel();
					channel.exchangeDeclare(exchangeName, type.name());
					String queueName = channel.queueDeclare().getQueue();
					channel.queueBind(queueName,exchangeName , "");
					channel.addShutdownListener(rabbitMQManager);
					
					// we define what happens if we recieve a new Message
					channel.basicConsume(queueName,new DefaultConsumer(channel){
						@Override
						public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
							String string = new String(body);
							JSONParser parser = new JSONParser();
							JSONObject object;
							try {
								object = (JSONObject) parser.parse(string);
								User user = new User(object);
								fireMessageFromManager(user, inAppMessageType.USER);
							} catch (ParseException e) {
								fireError(e);
							}
						}
					});

					subscribedChannels.put(exchangeName, channel);
				} catch (IOException e) {
					fireError(e);
				}
			}
		};
	}

	@Override
	public Runnable sendStringOnChannel(final String string, final String exchangeName) {
		return new Runnable() {

			@Override
			public void run() {
				try {
					Log.i(LOGTAG, "sending");
					Channel channel = subscribedChannels.get(exchangeName);
					if (channel != null) {
						channel.basicPublish(exchangeName, "", null, string.getBytes());
					}
				} catch (IOException e) {
					fireError(e);
				}

			}
		};
	}
	
	@Override
	public Runnable sendStringToSubscribedChannels(final String string) {
		return new Runnable() {
			
			@Override
			public void run() {
				Set<String> exchangeNames = subscribedChannels.keySet();
				for (String exchangeName : exchangeNames) {
					/**
					 * we dont send the message on the Main channel
					 */
					if (!exchangeName.equalsIgnoreCase("main")) {
						ThreadPool.runTask(sendStringOnChannel(string, exchangeName));
					}
				}
						
			}
		};
	}

	@Override
	public Runnable endSubscribtionToChannel(final String exchangeName) {
		return new Runnable() {

			@Override
			public void run() {
				Channel channel = subscribedChannels.get(exchangeName);
				if (channel != null) {
					try {
						channel.close(0,exchangeName);
					} catch (IOException e) {
						fireError(e);
					}
				}
			}
		};
	}

	@Override
	public void shutdownCompleted(ShutdownSignalException arg0) {
		String string = arg0.getMessage();
		
		/*
		 * if by chance the Message was not set than we have to search each channel for the closed one
		 */
		if (string == null) {
			Set<String> keys = subscribedChannels.keySet();
			for (String key : keys) {
				if (!subscribedChannels.get(key).isOpen()) {
					string = key;
					break;
				}
			}

		}
		synchronized (subscribedChannels) {
			subscribedChannels.remove(string);
		}
	}

}
