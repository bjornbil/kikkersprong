package be.khleuven.bjornbillen.kikkersprong.controller;

import java.io.IOException;
import be.khleuven.bjornbillen.kikkersprong.controller.admin.AdminActivity;
import be.khleuven.bjornbillen.kikkersprong.db.AttendanceDAO;
import be.khleuven.bjornbillen.kikkersprong.db.BillDAO;
import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.db.XMLDatabase;
import com.example.kikkersprong.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	Button infobutton, scanbutton;
	TextView scan, title;
	ImageView qr, in, out;
	LinearLayout lin;
	MemberDAO membercontroller;
	AttendanceDAO attendancecontroller;
	BillDAO billcontroller;
	
	

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@SuppressLint({ "ShowToast", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		membercontroller = MemberDAO.getInstance(getApplicationContext());
		attendancecontroller = AttendanceDAO.getInstance(getApplicationContext());
		billcontroller = BillDAO.getInstance(getApplicationContext());
		if (getIntent().getExtras() != null){
			if (getIntent().getExtras().getString("db") != null){
				
			}
			else {
				XMLDatabase xml = new XMLDatabase(getApplicationContext());
				try {
					xml.loadFromXML();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else {
			XMLDatabase xml = new XMLDatabase(getApplicationContext());
			try {
				xml.loadFromXML();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		infobutton = (Button) findViewById(R.id.infobutton);
		scanbutton = (Button) findViewById(R.id.scanbutton);
		scan = (TextView) findViewById(R.id.begroeting);
		title = (TextView) findViewById(R.id.main_title);
		qr = (ImageView) findViewById(R.id.qrexample);
		if (android.os.Build.VERSION.SDK_INT >= 17) {
			
			lin = (LinearLayout) findViewById(R.id.clockspace);
			TextClock textclock;
			textclock = new TextClock(getApplicationContext());
			textclock.setTimeZone("GMT+0200");
			textclock.setFormat12Hour("MMM dd, yyyy k:mm:ss");
			textclock.setFormat24Hour("MMM dd, yyyy k:mm:ss");
			textclock.setTextColor(Color.BLACK);
			lin.addView(textclock);
		}
		
		launch();
	}
	public MemberDAO getMemberController(){
		return membercontroller;
	}
	
	public AttendanceDAO getAttendanceController(){
		return attendancecontroller;
	}
	public BillDAO getBillController(){
		return billcontroller;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Kikkersprong sluiten")
	        .setMessage("Ben je zeker dat je wil afsluiten?")
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
	    {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            finish();    
	        }

	    })
	    .setNegativeButton("No", null)
	    .show();
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
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
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
			
			if (id == -1){
				Intent i1 = new Intent(getApplicationContext(), AdminActivity.class);
				i1.putExtra("name", name);
				startActivity(i1);
				MainActivity.this.finish();
			}
			else if (getMemberController().existMember(namearray[1], contentarray[2]) && id >= 0){
				
				Intent i = new Intent(getApplicationContext(), MemberActivity.class);
				i.putExtra("id", id);
				startActivity(i);
				MainActivity.this.finish();
			}
			else {
				
				startActivity(intent);
				MainActivity.this.finish();
			}
			
		}
		
	}
	
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.scanbutton:
			
		//IntentIntegrator scanIntegrator = new IntentIntegrator(this);
	//scanIntegrator.initiateScan();
	Intent i = new
			 Intent(getApplicationContext(),AdminActivity.class);
			 i.putExtra("id", -1);
			 i.putExtra("name", "Bjorn Billen");
			 startActivity(i);
			 
			 MainActivity.this.finish();
			 
			break;
		case R.id.infobutton:
			Intent i2 = new Intent(getApplicationContext(), InfoActivity.class);
			this.startActivity(i2);
			
			break;
		}
	}

}
