package com.android.helpme.demo.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import com.android.helpme.demo.manager.interfaces.UserManagerInterface;
import com.android.helpme.demo.messagesystem.AbstractMessageSystem;
import com.android.helpme.demo.messagesystem.AbstractMessageSystemInterface;
import com.android.helpme.demo.messagesystem.InAppMessage;
import com.android.helpme.demo.utils.User;
import com.android.helpme.demo.utils.UserInterface;

/**
 * 
 * @author Andreas Wieland
 * 
 */
public class UserManager extends AbstractMessageSystem implements UserManagerInterface {
	private static final String LOGTAG = UserManager.class.getSimpleName();
	private static final String USER_PROPERTIES = "user.properties";
	private static UserManager manager;
	private InAppMessage message;
	private ArrayList<User> arrayList;

	public static UserManager getInstance() {
		if (manager == null) {
			manager = new UserManager();
		}
		return manager;
	}

	private UserManager() {
		arrayList = new ArrayList<User>();
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
		this.message = inAppMessage;
	}

	@Override
	public AbstractMessageSystemInterface getManager() {
		return manager;
	}

	/* (non-Javadoc)
	 * @see com.android.helpme.demo.manager.UserManagerInterface#addUser(com.android.helpme.demo.utils.User)
	 */
	@Override
	public void addUser(User position) {
		synchronized (arrayList) {
			arrayList.add(position);
		}
	}

	/* (non-Javadoc)
	 * @see com.android.helpme.demo.manager.UserManagerInterface#getUsers()
	 */
	@Override
	public ArrayList<User> getUsers() {
		return arrayList;
	}

	/* (non-Javadoc)
	 * @see com.android.helpme.demo.manager.UserManagerInterface#getUser(java.lang.String)
	 */
	@Override
	public UserInterface getUser(String userName) {
		userName = userName.trim();
		for (UserInterface user : arrayList) {
			if (user.getName().compareToIgnoreCase(userName) == 0) {
				return user;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.android.helpme.demo.manager.UserManagerInterface#readUserFromProperty(android.app.Activity)
	 */
	@Override
	public Runnable readUserFromProperty(final Activity activity) {
		return new Runnable() {

			@Override
			public void run() {
				Resources resources = activity.getResources();
				AssetManager assetManager = resources.getAssets();

				// Read from the /assets directory
				try {
					InputStream inputStream = assetManager.open(USER_PROPERTIES);
					Reader reader = new InputStreamReader(inputStream);
					JSONParser parser = new JSONParser();
					Properties properties = new Properties();
					properties.load(reader);
					Set<Object> set = properties.keySet();
					for (Object key : set) {
						String string = (String) properties.get(key);
						JSONObject object = (JSONObject) parser.parse(string);
						addUser(new User(object));
					}
					Log.i(LOGTAG, "The properties are now loaded");
				} catch (IOException e) {
					fireError(e);
				} catch (ParseException e) {
					fireError(e);
				}

			}
		};

	}

}
