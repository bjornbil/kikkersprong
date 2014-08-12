package be.khleuven.bjornbillen.kikkersprong.controller;

import java.io.IOException;
import java.util.Calendar;

import be.khleuven.bjornbillen.kikkersprong.db.AttendanceDAO;
import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.db.XMLDatabase;
import be.khleuven.bjornbillen.kikkersprong.model.Attendance;
import be.khleuven.bjornbillen.kikkersprong.model.Member;

import com.example.kikkersprong.R;
import com.example.kikkersprong.R.id;
import com.example.kikkersprong.R.layout;
import com.example.kikkersprong.R.menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CheckinActivity extends Activity {
	TextView checkstatus, checkdate, begroeting;
	int id;
	MemberDAO membercontroller;
	AttendanceDAO attendancecontroller;
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkin);
		checkstatus = (TextView) findViewById(R.id.checkstatus);
		begroeting = (TextView) findViewById(R.id.begroeting);
		membercontroller = MemberDAO.getInstance(getApplicationContext());
		attendancecontroller = AttendanceDAO.getInstance(getApplicationContext());	
		Bundle bundle = getIntent().getExtras();
		int id = bundle.getInt("id");
		this.id = id;
		getMemberController().setCurrentMemberID(id);
		Member m = getMemberController().getMember(id);
		if (m.isPresent() == true) {

			checkstatus.setTextColor(Color.GREEN);
			checkstatus.setText("Ingecheckt");
			Calendar startdate = Calendar.getInstance();
			if (startdate.get(Calendar.HOUR_OF_DAY) < 12) {
				begroeting.setText("Goedemorgen, je bent nu");
			} else {
				begroeting.setText("Goeiedag, je bent nu");
			}
			m.setLastcheckin(startdate);
			getMemberController().updateMember(m);
			

		} else {
			checkstatus.setTextColor(Color.RED);
			checkstatus.setText("Uitgecheckt");
			Calendar enddate = Calendar.getInstance();
			if (enddate.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
				begroeting.setText("Goed weekend, je bent nu");
			} else {
				begroeting.setText("Goede avond, je bent nu");
			}
			Attendance nieuw = new Attendance(getAttendanceController().getSize(),
					m, m.getLastcheckin(), enddate);
			if (nieuw.getDuration() > 1) {
				getAttendanceController().addAttendance(nieuw);
				getMemberController().update();
			}

		}
		XMLDatabase xml = new XMLDatabase(getApplicationContext());
		try {
			xml.writetoXML();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		checkstatus.invalidate();
	}
	
	public MemberDAO getMemberController(){
		return membercontroller;
	}
	
	public AttendanceDAO getAttendanceController(){
		return attendancecontroller;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Intent i = new Intent(getApplicationContext(), MemberActivity.class);
			i.putExtra("id", getMemberController().getCurrentMemberID());
			i.putExtra("name",  getMemberController().getMember(id).getFirstname()
					+ " " + getMemberController().getMember(id).getLastname());
			startActivity(i);
		}
		CheckinActivity.this.finish();
		return super.onKeyDown(keyCode, event);
	}

	
	
}
