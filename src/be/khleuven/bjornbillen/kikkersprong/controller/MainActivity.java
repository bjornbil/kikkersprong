package be.khleuven.bjornbillen.kikkersprong.controller;

import java.io.IOException;
import java.util.Calendar;

import be.khleuven.bjornbillen.kikkersprong.controller.admin.AdminActivity;
import be.khleuven.bjornbillen.kikkersprong.db.AttendanceDAO;
import be.khleuven.bjornbillen.kikkersprong.db.BillDAO;
import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.db.XMLDatabase;
import be.khleuven.bjornbillen.kikkersprong.model.Attendance;
import be.khleuven.bjornbillen.kikkersprong.model.Bill;
import be.khleuven.bjornbillen.kikkersprong.model.Member;

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
	MemberDAO membercontroller;
	AttendanceDAO attendancecontroller;
	BillDAO billcontroller;
	

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@SuppressLint({ "ShowToast", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		membercontroller = new MemberDAO(getApplicationContext());
		attendancecontroller = new AttendanceDAO(getApplicationContext());
		billcontroller = new BillDAO(getApplicationContext());
		infobutton = (Button) findViewById(R.id.infobutton);
		scanbutton = (Button) findViewById(R.id.scanbutton);
		scan = (TextView) findViewById(R.id.begroeting);
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
			textclock.setFormat24Hour("MMM dd, yyyy k:mm:ss");
			textclock.setTextColor(Color.BLACK);
			lin.addView(textclock);
			title.setTextSize(38);
		}
		
		// HARDCODED TEST VALUES
		/*Calendar birthday = Calendar.getInstance();
		birthday.set(Calendar.DATE, 17);
		birthday.set(Calendar.MONTH, 2);
		birthday.set(Calendar.YEAR, 2008);
		membercontroller.addMember(new Member(0,"Jan","Jaap",birthday,"http://liverichly.me/wp-content/uploads/2013/08/child-angry.jpg",false,Calendar.getInstance()));
		Calendar birthday2 = Calendar.getInstance();
		birthday2.set(Calendar.DATE, 20);
		birthday2.set(Calendar.MONTH, 5);
		birthday2.set(Calendar.YEAR, 2008);
		membercontroller.addMember(new Member(1,"Kim","Pims",birthday2,"http://i.telegraph.co.uk/multimedia/archive/02102/baby_2102889c.jpg",false,Calendar.getInstance()));
		Member m = membercontroller.getMember(0);
		Calendar start = Calendar.getInstance();
		start.set(Calendar.DATE, 26);
		start.set(Calendar.MONTH, 6);
		start.set(Calendar.HOUR_OF_DAY,9);
		Calendar end = Calendar.getInstance();
		end.set(Calendar.DATE, 26);
		end.set(Calendar.MONTH, 6);
		end.set(Calendar.HOUR_OF_DAY, 12);
		attendancecontroller.addAttendance(new Attendance(0, m, start, end));
		Member m2 = membercontroller.getMember(1);
		Calendar start2 = Calendar.getInstance();
		start2.set(Calendar.DATE, 3);
		start2.set(Calendar.MONTH, 7);
		start2.set(Calendar.HOUR_OF_DAY,9);
		Calendar end2 = Calendar.getInstance();
		end2.set(Calendar.DATE, 3);
		end2.set(Calendar.MONTH, 7);
		end2.set(Calendar.HOUR_OF_DAY,15);
		attendancecontroller.addAttendance(new Attendance(1, m2, start2, end2));
		Calendar paydate = Calendar.getInstance();
		paydate.set(Calendar.DATE, 31);
		// AMOUNT = 240 IS HARDCODED HERE, need to be sum of all attendances x priceperhour
		billcontroller.addBill(new Bill(0,240,m,paydate,true));*/
		// END HARDCODED TEST VALUE++
		XMLDatabase db = new XMLDatabase(getApplicationContext());
		try {
			db.loadFromXML();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		launch();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
	   finish();
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
			if (id == -1){
				Intent i1 = new Intent(getApplicationContext(), AdminActivity.class);
				i1.putExtra("id",id);
				i1.putExtra("name",name);
				startActivity(i1);
			}
			else {
			Intent i = new Intent(getApplicationContext(), MemberActivity.class);
			i.putExtra("id", id);
			i.putExtra("name", name);
			startActivity(i);
			}
		}
		else if (resultCode != RESULT_OK){
			Intent i2 = new Intent(getApplicationContext(),MainActivity.class);
			startActivity(i2);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.scanbutton:
			
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
	scanIntegrator.initiateScan();
		/*Intent i = new
			 Intent(getApplicationContext(),AdminActivity.class);
			 i.putExtra("id", -1);
			 i.putExtra("name", "Bjorn Billen");
			 startActivity(i);
			 
			 MainActivity.this.finish();*/
			 
			break;
		case R.id.infobutton:
			Intent i2 = new Intent(getApplicationContext(), InfoActivity.class);
			this.startActivity(i2);
			MainActivity.this.finish();
			break;
		}
	}

}
