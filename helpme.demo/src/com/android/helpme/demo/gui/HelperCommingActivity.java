/**
 * 
 */
package com.android.helpme.demo.gui;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import com.android.helpme.demo.R;
import com.android.helpme.demo.manager.HistoryManager;
import com.android.helpme.demo.manager.MessageOrchestrator;
import com.android.helpme.demo.manager.UserManager;
import com.android.helpme.demo.utils.User;
import com.android.helpme.demo.utils.UserInterface;
import com.android.helpme.demo.utils.position.Position;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

/**
 * @author Andreas Wieland
 *
 */
public class HelperCommingActivity extends Activity implements DrawManager {
	private TextView textView;
	private Handler handler;
	private DecimalFormat decimalFormat;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helpercoming);
		textView = (TextView) findViewById(R.id.textView);
		handler = new Handler();
		handler.post(setText());

		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator('.');
		decimalFormatSymbols.setGroupingSeparator(',');
		decimalFormat = new DecimalFormat("#,##0.00", decimalFormatSymbols);
		
		MessageOrchestrator.getInstance().addDrawManager(DRAWMANAGER_TYPE.HELPERCOMMING, this);
	}
	
	private Runnable setText(){
		return new Runnable() {
			
			@Override
			public void run() {
				Double distance = HistoryManager.getInstance().getTask().getDistance();
				textView.setText(getString(R.string.distance_to_helper) + decimalFormat.format(distance) + " m");
			}
		};
		
	}

	/* (non-Javadoc)
	 * @see com.android.helpme.demo.gui.DrawManager#drawThis(java.lang.Object)
	 */
	@Override
	public void drawThis(Object object) {
		if (object instanceof UserInterface) {
			handler.post(setText());
		}
	}

}
