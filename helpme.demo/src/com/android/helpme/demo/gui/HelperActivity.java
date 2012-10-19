package com.android.helpme.demo.gui;

import java.util.ArrayList;

import javax.crypto.spec.PSource;

import org.json.simple.JSONObject;

import com.android.helpme.demo.R;
import com.android.helpme.demo.R.id;
import com.android.helpme.demo.R.layout;
import com.android.helpme.demo.manager.MessageOrchestrator;
import com.android.helpme.demo.manager.PositionManager;
import com.android.helpme.demo.manager.UserManager;
import com.android.helpme.demo.utils.User;
import com.android.helpme.demo.utils.UserInterface;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
/**
 * 
 * @author Andreas Wieland
 *
 */
public class HelperActivity extends Activity implements DrawManager {
	private ListView listView;
	public static ArrayAdapter<String> adapter;
	private ArrayList<String> data;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.foundhelper);
		listView = (ListView) findViewById(R.id.foundHelper);
		data = new ArrayList<String>();
		handler = new Handler();

		adapter = new ArrayAdapter<String>(this, R.layout.simplerow,data);
		for (UserInterface user : UserManager.getInstance().getUsers()) {
			if (user == null) {
				break;
			}
			adapter.add(user.toString());
		}

		listView.setAdapter(adapter);
		MessageOrchestrator.getInstance().addDrawManager(DRAWMANAGER_TYPE.LIST, this);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				String[] name = adapter.getItem(position).split(":");
				UserInterface user = UserManager.getInstance().getUserByName(name[1]);
				if (user != null) {
					showPosition(user);
				}
			}
		});
	}

	private Runnable addUser(final User user, final Context context){
		return new Runnable() {

			@Override
			public void run() {
				adapter.add(user.toString());
				
				AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
				dlgAlert.setTitle("new Help Assignment");
				dlgAlert.setMessage("Do you want to help: " + user.getName() +" ?");
				dlgAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						showPosition(user);
					}
					
				});
				
				dlgAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});

				dlgAlert.create().show();

				
			}
		};
	}


	@Override
	public void drawThis(Object object) {
		if (object instanceof User) {
			handler.post(addUser((User)object, this));
		}
	}

	public void showPosition(UserInterface user) {
		Intent myIntent = new Intent(this.getApplicationContext(), Maps.class);
		startActivity(myIntent);

	}

}
