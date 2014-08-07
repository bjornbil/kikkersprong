package be.khleuven.bjornbillen.kikkersprong.controller;

import be.khleuven.bjornbillen.kikkersprong.db.AttendanceDAO;
import be.khleuven.bjornbillen.kikkersprong.db.BillDAO;
import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.model.Member;

import com.example.kikkersprong.R;
import com.example.kikkersprong.R.id;
import com.example.kikkersprong.R.layout;
import com.example.kikkersprong.R.menu;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AdminActivity extends Activity {
	MemberDAO membercontroller; 
	AttendanceDAO attendancecontroller;
	BillDAO billcontroller;
	TextView adminnaam;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin);
		membercontroller = new MemberDAO(getApplicationContext());
		attendancecontroller = new AttendanceDAO(getApplicationContext());
		billcontroller = new BillDAO(getApplicationContext());
		adminnaam = (TextView) findViewById(R.id.adminnaam);
		Bundle bundle = getIntent().getExtras();
		int id = bundle.getInt("id");
		membercontroller.setCurrentMemberID(id);
		String naam = bundle.getString("name");
		adminnaam.setText(naam);
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin, menu);
		return true;
	}

}
