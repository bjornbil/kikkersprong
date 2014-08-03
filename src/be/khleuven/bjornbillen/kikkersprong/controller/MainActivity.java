package be.khleuven.bjornbillen.kikkersprong.controller;

import com.example.kikkersprong.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.print.PrintAttributes.Margins;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	Button infobutton, scanbutton;
	TextView scan, title;
	ImageView qr, in, out;
	LinearLayout lin;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@SuppressLint({ "ShowToast", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		infobutton = (Button) findViewById(R.id.infobutton);
		scanbutton = (Button) findViewById(R.id.scanbutton);
		scan = (TextView) findViewById(R.id.textView1);
		title = (TextView) findViewById(R.id.main_title);
		qr = (ImageView) findViewById(R.id.imageView2);
		if (android.os.Build.VERSION.SDK_INT >= 17) {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.BELOW, R.id.imageView1);
			params.setMargins(55, 0, 0, 0);
			infobutton.setLayoutParams(params);
			infobutton.getLayoutParams().height = 35;
			RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			params2.setMargins(20, 80, 0, 0);
			params2.addRule(RelativeLayout.BELOW, R.id.infobutton);
			scan.setLayoutParams(params2);

			lin = (LinearLayout) findViewById(R.id.clockspace);
			TextClock textclock;
			textclock = new TextClock(getApplicationContext());
			textclock.setTimeZone("GMT+0200");
			textclock.setFormat12Hour("MMM dd, yyyy k:mm:ss");
			textclock.setTextColor(Color.BLACK);
			lin.addView(textclock);
			title.setTextSize(40);
		}
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
	@SuppressLint("ShowToast")
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanResult.getContents() != null) {
			String contents = scanResult.getContents();
			String format = scanResult.getFormatName();
			Toast.makeText(getApplicationContext(), contents + "(Format : "
					+ format + ")", 1000);
			String contentarray[] = contents.split(" ");
			String idarray[] = contentarray[0].split("=");
			int id = Integer.parseInt(idarray[1]);
			String namearray[] = contentarray[1].split("=");
			String name = namearray[1] + " " + contentarray[2];
			Intent i = new Intent(getApplicationContext(), MemberActivity.class);
			i.putExtra("id", id);
			i.putExtra("name", name);
			this.startActivity(i);
		}
		else {
			Intent i = new Intent(getApplicationContext(),MainActivity.class);
			this.startActivity(i);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.scanbutton:
			//IntentIntegrator scanIntegrator = new IntentIntegrator(this);
			//scanIntegrator.initiateScan();
			 Intent i = new
			 Intent(getApplicationContext(),MemberActivity.class);
			 i.putExtra("id", 0);
			 i.putExtra("name", "Bjorn Billen");
			 this.startActivity(i);
			break;
		case R.id.infobutton:
			Intent i2 = new Intent(getApplicationContext(), InfoActivity.class);
			this.startActivity(i2);
			break;
		}
	}

}
