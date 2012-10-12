package com.android.helpme.demo;


import org.json.simple.JSONObject;

import com.android.helpme.demo.manager.MessageOrchestrator;
import com.android.helpme.demo.manager.PositionManager;
import com.android.helpme.demo.manager.RabbitMQManager;
import com.android.helpme.demo.utils.ThreadPool;
import com.android.helpme.demo.utils.User;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity implements DrawManager{
	private static final String LOG_TAG = "MainActivity"; 
	public static final User user = new User("Andy", true);
	private TextView textView;
	private Button buttonHelpMe;
	private ProgressBar progressBar;
	private MessageOrchestrator orchestrator;
	private Handler uihandler;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		uihandler = new Handler();
		textView = (TextView) findViewById(R.id.textView1);
		buttonHelpMe = (Button) findViewById(R.id.helpButton);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar.setVisibility(ProgressBar.INVISIBLE);
		init();
	}
	private void init(){
		ThreadPool.getThreadPool(10);
		orchestrator = MessageOrchestrator.getInstance();
		orchestrator.setDrawManager(DRAWMANAGER_TYPE.MAIN,this);
		
		MessageOrchestrator.listenToMessageSystem(RabbitMQManager.getInstance());
		MessageOrchestrator.listenToMessageSystem(PositionManager.getInstance(this));

		ThreadPool.runTask(RabbitMQManager.getInstance().connect());

		wireButton();
		
	}

	private void wireButton() {
		buttonHelpMe.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				progressBar.setVisibility(ProgressBar.VISIBLE);
				uihandler.post (PositionManager.getInstance().startLocationTracking());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	@Override
	public void drawThis(Object object) {
		if (object instanceof JSONObject) {
//			progressBar.setVisibility(ProgressBar.INVISIBLE);
			Intent myIntent = new Intent(this.getApplicationContext(), FoundHelperActivity.class);
			startActivity(myIntent);
		}

	}
}
