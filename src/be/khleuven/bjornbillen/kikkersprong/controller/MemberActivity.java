package be.khleuven.bjornbillen.kikkersprong.controller;

import java.util.Calendar;

import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.model.Member;

import com.example.kikkersprong.R;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

public class MemberActivity extends Activity implements OnClickListener {
	MemberDAO membercontroller;
	TextView txtnaam, geboortedatum,checktext;
	LinearLayout lin;
	ImageView foto;
	ImageButton checkin, attendances, bills;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member);
		membercontroller = new MemberDAO(getApplicationContext());
		txtnaam = (TextView) findViewById(R.id.naam);
		geboortedatum = (TextView) findViewById(R.id.gebdatum);
		foto = (ImageView) findViewById(R.id.imageView2);
		if (android.os.Build.VERSION.SDK_INT >= 17) {
			lin = (LinearLayout) findViewById(R.id.clockspace2);
			TextClock textclock;
			textclock = new TextClock(getApplicationContext());
			textclock.setTimeZone("GMT+0200");
			textclock.setFormat12Hour("MMM dd, yyyy k:mm:ss");
			textclock.setTextColor(Color.BLACK);
			lin.addView(textclock);
		}
		checkin = (ImageButton) findViewById(R.id.checkinbutton);
		bills = (ImageButton) findViewById(R.id.billsbutton);
		attendances = (ImageButton) findViewById(R.id.attendancebutton);
		checkin.setOnClickListener(this);
		bills.setOnClickListener(this);
		attendances.setOnClickListener(this);
		checktext = (TextView) findViewById(R.id.checktext);
		Bundle bundle = getIntent().getExtras();
		int id = bundle.getInt("id");
		membercontroller.setCurrentMemberID(id);
		String naam = bundle.getString("name");
		membercontroller.addMember(new Member(0,"Bjorn","Billen",Calendar.getInstance(),"test",true));
		Member m = membercontroller.getMember(id);
		String dbnaam = m.getFirstname() + " " + m.getLastname();
		if (naam.equals(dbnaam)){
		txtnaam.setText(naam);
		geboortedatum.setText(m.getBirthdayString());
		foto.setImageResource(R.drawable.ic_nopic);
		}
		else {
		Intent i = new Intent(getApplicationContext(),MainActivity.class);
	  i.putExtra("ERROR", "Er is geen persoon gevonden");
		 }
		updateCheckin();
	}
	
	public void updateCheckin(){
		int id = membercontroller.getCurrentMemberID();
		
		if(membercontroller.getMember(id).isPresent()){
			checkin.setImageResource(R.drawable.ic_checkout);
			checktext.setText("Uitchecken");
		}
		else{
			checkin.setImageResource(R.drawable.ic_checkin);
			checktext.setText("Inchecken");
		}
		checkin.refreshDrawableState();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.member, menu);

		return true;
	}
	


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.checkinbutton:
			int id = membercontroller.getCurrentMemberID();
			Member m = membercontroller.getMember(id);
			if (m.isPresent()){
				m.setPresent(false);
			}
			else {
				m.setPresent(true);
			}
			membercontroller.updateMember(m);
			
			Intent i = new Intent(getApplicationContext(),
					CheckinActivity.class);
			i.putExtra("id", membercontroller.getCurrentMemberID());
			this.startActivity(i);
			updateCheckin();
			break;
		case R.id.attendancebutton:
			Intent i2 = new Intent(getApplicationContext(),
					AttendanceActivity.class);
			i2.putExtra("id", membercontroller.getCurrentMemberID());
			this.startActivity(i2);
			break;
		case R.id.billsbutton:
			Intent i3 = new Intent(getApplicationContext(), BillsActivity.class);
			i3.putExtra("id", membercontroller.getCurrentMemberID());
			this.startActivity(i3);
			break;
		}

	}

}
