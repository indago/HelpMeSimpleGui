package com.android.helpme.demo.gui;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.android.helpme.demo.R;
import com.android.helpme.demo.R.id;
import com.android.helpme.demo.R.layout;
import com.android.helpme.demo.gui.DrawManager.DRAWMANAGER_TYPE;
import com.android.helpme.demo.manager.HistoryManager;
import com.android.helpme.demo.manager.MessageOrchestrator;
import com.android.helpme.demo.manager.PositionManager;
import com.android.helpme.demo.manager.RabbitMQManager;
import com.android.helpme.demo.manager.UserManager;
import com.android.helpme.demo.manager.interfaces.HistoryManagerInterface;
import com.android.helpme.demo.manager.interfaces.MessageOrchestratorInterface;
import com.android.helpme.demo.utils.Task;
import com.android.helpme.demo.utils.ThreadPool;
import com.android.helpme.demo.utils.User;
import com.android.helpme.demo.utils.UserInterface;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SeekerActivity extends Activity implements DrawManager{
	private static final String LOG_TAG = "MainActivity"; 
	
	private Button buttonHelpMe;
	private ProgressBar progressBar;
	private MessageOrchestratorInterface orchestrator;
	private Handler uihandler;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		uihandler = new Handler();
		buttonHelpMe = (Button) findViewById(R.id.helpButton);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar.setVisibility(ProgressBar.INVISIBLE);
		buttonHelpMe.setVisibility(Button.VISIBLE);
		init();
	}
	
	private void init(){
		orchestrator = MessageOrchestrator.getInstance();
		orchestrator.addDrawManager(DRAWMANAGER_TYPE.SEEKER,this);
		wireButton();
	}
	
	

	private void wireButton() {
		buttonHelpMe.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				progressBar.setVisibility(ProgressBar.VISIBLE);
//				buttonHelpMe.setActivated(false);
//				buttonHelpMe.setClickable(false);
				buttonHelpMe.setVisibility(Button.INVISIBLE);
				HistoryManager.getInstance().startNewTask(); 
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_relog:
			ThreadPool.runTask(UserManager.getInstance().deleteUserChoice(getApplicationContext()));
			Intent intent = new Intent(getBaseContext(), SwitcherActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private Runnable noUserFound(final Context context) {
		return new Runnable() {
			
			@Override
			public void run() {
				AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
				dlgAlert.setTitle(getString(R.string.no_helper_title));
				dlgAlert.setMessage(getString(R.string.no_helper_text));
				dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						progressBar.setVisibility(ProgressBar.INVISIBLE);
						buttonHelpMe.setVisibility(Button.VISIBLE);
					}
				});

				dlgAlert.create().show();
				
			}
		};
	}
	
	@Override
	public void drawThis(Object object) {
		if (object instanceof User) {
//			progressBar.setVisibility(ProgressBar.INVISIBLE);
			Intent myIntent = new Intent(this.getApplicationContext(), HelperCommingActivity.class);
			startActivity(myIntent);
		}
		if (object instanceof Task) {
			uihandler.post(noUserFound(this));
		}
	}
	
	@Override
	public void onBackPressed() {
//		Intent intent = new Intent(this, SeekerActivity.class);
//		startActivity(intent);
	}
}
