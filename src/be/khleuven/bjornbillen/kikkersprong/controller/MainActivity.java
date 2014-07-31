package be.khleuven.bjornbillen.kikkersprong.controller;

import com.example.kikkersprong.R;
import com.example.kikkersprong.R.layout;
import com.example.kikkersprong.R.menu;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	Button infobutton, scanbutton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		infobutton = (Button) findViewById(R.id.infobutton);
		scanbutton = (Button) findViewById(R.id.scanbutton);
		launch();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void launch() {
		infobutton.setOnClickListener(this);
		scanbutton.setOnClickListener(this);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		boolean previouslyStarted = prefs.getBoolean(
				getString(R.string.started), false);
		if (!previouslyStarted) {
			SharedPreferences.Editor edit = prefs.edit();
			edit.putBoolean(getString(R.string.started), Boolean.TRUE);
			edit.commit();
			Intent i = new Intent(getApplicationContext(), InfoActivity.class);
			startActivity(i);
		}
	}
	// TODO : need to test this on device
	// OnActivityResult should give us extra information about the scan
	// TODO : get the code out of this contents -> get id -> redirect to member page
	@SuppressLint("ShowToast")
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				Toast.makeText(getApplicationContext(), contents + "(Format : " + format + ")", 1000);
				//Intent i = new Intent(getApplicationContext(),MemberActivity.class);
				//this.startActivity(i);
				// Handle successful scan
			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.scanbutton:
			//IntentIntegrator scanIntegrator = new IntentIntegrator(this);
			//scanIntegrator.initiateScan();
			Intent i = new Intent(getApplicationContext(),MemberActivity.class);
			this.startActivity(i);
			break;
		case R.id.infobutton:
			Intent i2 = new Intent(getApplicationContext(), InfoActivity.class);
			this.startActivity(i2);
			break;
		}
	}

}
