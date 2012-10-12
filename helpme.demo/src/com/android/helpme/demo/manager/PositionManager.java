/**
 * 
 */
package com.android.helpme.demo.manager;

import java.util.ArrayList;

import com.android.helpme.demo.messagesystem.AbstractMessageSystem;
import com.android.helpme.demo.messagesystem.InAppMessage;
import com.android.helpme.demo.messagesystem.MESSAGE_TYPE;
import com.android.helpme.demo.position.Position;
import com.android.helpme.demo.position.SimpleSelectionStrategy;
import com.android.helpme.demo.utils.User;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

/**
 * @author Andreas Wieland
 * 
 */
public class PositionManager extends AbstractMessageSystem implements LocationListener {
	private static final String LOGTAG = PositionManager.class.getSimpleName();
	private InAppMessage message;
	private static PositionManager manager;
	private LocationManager locationManager;
	private Location lastLocation;
	private boolean started;
	private ArrayList<User> positions;

	/**
	 * 
	 */
	private PositionManager(Activity activity) {
		locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
		lastLocation = null;
		started = false;
		positions = new ArrayList<User>();
	}

	public static PositionManager getInstance() {
		return manager;
	}

	public static PositionManager getInstance(Activity activity) {
		if (manager == null) {
			manager = new PositionManager(activity);
		}
		return manager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.indago.android.demo.messagesystem.AbstractMessageSystem#getLogTag()
	 */
	@Override
	public String getLogTag() {
		return LOGTAG;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.indago.android.demo.messagesystem.AbstractMessageSystem#getMessage()
	 */
	@Override
	protected InAppMessage getMessage() {
		return message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.indago.android.demo.messagesystem.AbstractMessageSystem#setMessage
	 * (com.indago.android.demo.messagesystem.InAppMessage)
	 */
	@Override
	protected void setMessage(InAppMessage inAppMessage) {
		this.message = inAppMessage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.indago.android.demo.messagesystem.AbstractMessageSystem#getManager()
	 */
	@Override
	public AbstractMessageSystem getManager() {
		return manager;
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.i(getLogTag(), "new Location arrived");
		if (!SimpleSelectionStrategy.isPositionRelevant(location)) {
			return;
		}
		if (lastLocation != null && !SimpleSelectionStrategy.isPositionRelevant(lastLocation, location)) {
			return;
		}
		lastLocation = location;
		Position wayPointData = new Position(location);
		fireMessageFromManager(wayPointData, MESSAGE_TYPE.LOCATION);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public Runnable startLocationTracking() {
		return new Runnable() {

			@Override
			public void run() {
				if (started) {
					return;
				}
//				if (Looper.myLooper() == null) {
//					Looper.prepare();
//				}
//				;

				lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (lastLocation != null && SimpleSelectionStrategy.isPositionRelevant(lastLocation)) {
					fireMessageFromManager(new Position(lastLocation), MESSAGE_TYPE.LOCATION);
				} else {
					locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, manager);
					started = true;
					// Looper.myLooper().quit();
				}

			}
		};

	}

	public Runnable stopLocationTracking() {
		return new Runnable() {

			@Override
			public void run() {
				if (!started) {
					return;
				}
				if (Looper.myLooper() == null) {
					Looper.prepare();
				}
				;

				locationManager.removeUpdates(manager);
				Looper.myLooper().quit();
				started = false;
			}
		};

	}

	public boolean isStarted() {
		return started;
	}

	public void addPosition(User position) {
		synchronized (positions) {
			positions.add(position);
		}
	}

	public ArrayList<User> getPositions() {
		return positions;
	}

	public User getPosition(String item) {
		item = item.trim();
		for (User user : positions) {
			if (user.getName().compareToIgnoreCase(item) == 0) {
				return user;
			}
		}
		return null;
	}

}
