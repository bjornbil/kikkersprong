package be.khleuven.bjornbillen.kikkersprong.controller;


import com.example.kikkersprong.R;
import com.example.kikkersprong.R.layout;
import com.example.kikkersprong.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AddMemberActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_member);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_member, menu);
		return true;
	}

	
}
