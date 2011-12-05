package edu.purdue.cs252.lab06;

import java.io.IOException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

public class DirectoryActivity extends ListActivity
{
	String serverAddress;
	String username;
	
	Handler UIhandler;
	
	private DirectoryClient dc;
	
	private ArrayAdapter<String> database;
	private ArrayList<String> usernames;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directory);
        
        usernames = new ArrayList<String>();
        database = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usernames);
        
        setListAdapter(database);
        
        connectToServer();
    }
    
    public void connectToServer()
    {
    	Log.i("Checkpoint", "Entered connectToServer()...");
    	Bundle extras = getIntent().getExtras();
    	serverAddress = extras.getString("serverAddress");
    	username = extras.getString("username");
        
        try
        {
        	setupHandler();
        	
            dc = new DirectoryClient(serverAddress, UIhandler);
              
            dc.connect();
            dc.addUser(username);
        }
        catch (Exception ex)
        {
        	AlertDialog.Builder adb = new AlertDialog.Builder(DirectoryActivity.this);
			   
			adb.setTitle("Error");
			adb.setMessage(ex.getMessage());
			adb.setPositiveButton("OK", null);
			adb.show();
        }
        
        finishActivity(0);
    }
    
    public void setupHandler()
    {
    	UIhandler = new Handler() {    		
    		public void handleMessage(Message msg)
    		{
    			if (msg.what == 0)
    			{
    				updateDirectory(((ArrayList<User>)msg.obj));
    			}
    			else if (msg.what == 1)
    			{
    				displayIncomingCall(((String[])msg.obj)[0], ((String[])msg.obj)[1]);
    			}
    			else if (msg.what == 2)
    			{
    				displayHangup();
    			}
    			else if (msg.what == 3)
    			{
    				displayBusy();
    			}
    		}
    	};
    }
    
    public void updateDirectory(ArrayList<User> directory)
    {
		usernames.clear();
		for (User u : directory)
		{
			usernames.add(u.getUsername());
		}
		
		database.notifyDataSetChanged();
    }
    
    private String senderIP = null;
    public void displayIncomingCall(String username, String ipAddress)
    {
    	senderIP = ipAddress;
    	Log.i("Checkpoint", "Entered displayIncomingCall()...");
    	AlertDialog.Builder adb = new AlertDialog.Builder(DirectoryActivity.this);

		adb.setTitle(ipAddress);
		adb.setMessage("You have an incoming call from " + username + " at " + ipAddress + ".");
		adb.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO: implement this action
					Log.i("Value Validation", "Sender IP: " + senderIP);
					
					try {
						dc.acceptCall(senderIP);
					} catch (IOException e) {
						// TODO Auto-generated catch block
					}
	        }
		});
		adb.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO: implement this action
	        }
		});
		adb.show();
    }
    
    public void displayHangup()
    {
     // TODO: implement this method
    }
    
    public void displayBusy()
    {
     // TODO: implement this method
    }
}
