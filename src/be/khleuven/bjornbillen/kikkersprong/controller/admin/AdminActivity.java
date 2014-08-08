package be.khleuven.bjornbillen.kikkersprong.controller.admin;

import be.khleuven.bjornbillen.kikkersprong.controller.AttendanceActivity;
import be.khleuven.bjornbillen.kikkersprong.controller.BillsActivity;
import be.khleuven.bjornbillen.kikkersprong.db.AttendanceDAO;
import be.khleuven.bjornbillen.kikkersprong.db.BillDAO;
import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.model.Member;

import com.example.kikkersprong.R;
import com.example.kikkersprong.R.id;
import com.example.kikkersprong.R.layout;
import com.example.kikkersprong.R.menu;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AdminActivity extends Activity implements OnClickListener {
	MemberDAO membercontroller; 
	AttendanceDAO attendancecontroller;
	BillDAO billcontroller;
	TextView adminnaam;
	Button addmember, viewmembers;
	
	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin);
		membercontroller = new MemberDAO(getApplicationContext());
		attendancecontroller = new AttendanceDAO(getApplicationContext());
		billcontroller = new BillDAO(getApplicationContext());
		adminnaam = (TextView) findViewById(R.id.adminnaam);
		addmember = (Button) findViewById(R.id.add_member);
		viewmembers = (Button) findViewById(R.id.view_members);
		viewmembers.setOnClickListener(this);
		addmember.setOnClickListener(this);
		Bundle bundle = getIntent().getExtras();
		int id = bundle.getInt("id");
		membercontroller.setCurrentMemberID(id);
		String naam = bundle.getString("name");
		if (bundle.getString("updatetext") != null){
		String update = bundle.getString("updatetext");
		Toast.makeText(getApplicationContext(), update, Toast.LENGTH_LONG).show();
		
		}
		adminnaam.setText(naam);
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_member:
			Intent i = new Intent(getApplicationContext(),
					AddMemberActivity.class);
			i.putExtra("name", adminnaam.getText());
			this.startActivity(i);
			AdminActivity.this.finish();
			break;
		case R.id.view_members:
			Intent i2 = new Intent(getApplicationContext(),
					ViewMembersActivity.class);
			i2.putExtra("name", adminnaam.getText());
			this.startActivity(i2);
			AdminActivity.this.finish();
			break;
		case R.id.view_bet:
			Intent i3 = new Intent(getApplicationContext(),
					BillsActivity.class);
			i3.putExtra("name", adminnaam.getText());
			this.startActivity(i3);
			AdminActivity.this.finish();
			break;
		case R.id.view_aanw:
			Intent i4 = new Intent(getApplicationContext(),
					AttendanceActivity.class);
			i4.putExtra("name", adminnaam.getText());
			this.startActivity(i4);
			AdminActivity.this.finish();
			break;
	}
	}

}
