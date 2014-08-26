package be.khleuven.bjornbillen.kikkersprong.controller.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import be.khleuven.bjornbillen.kikkersprong.controller.listadapter.CostumAttendancesAdapter;
import be.khleuven.bjornbillen.kikkersprong.db.AttendanceDAO;
import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.db.XMLDatabase;
import be.khleuven.bjornbillen.kikkersprong.model.Attendance;
import com.example.kikkersprong.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ViewAttendancesActivity extends Activity {
	
	ListView listView;
	RelativeLayout layout;
	CostumAttendancesAdapter listadapter;
	List<Attendance> attendances;
	TextView adminnaam;
	Spinner selectperiod;
	MemberDAO membercontroller;
	AttendanceDAO attendancecontroller;
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_attendance);
		membercontroller = MemberDAO.getInstance(getApplicationContext());
		attendancecontroller = AttendanceDAO.getInstance(getApplicationContext());
	
		listView = (ListView) findViewById(R.id.listViewAttendance);
		layout = (RelativeLayout) findViewById(R.id.aanwlayout);
		layout.setBackgroundResource(R.color.infcolor);
		adminnaam = (TextView) findViewById(R.id.membername);
		selectperiod = (Spinner) findViewById(R.id.spinner1);
		attendances = getAttendanceController().getAllAttendances();
		List<String> valuelist = new ArrayList<String>();
		for (Attendance a : attendances){
			valuelist.add(a.toString());
		}
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

							// Show Alert
				String datum = listView.getAdapter().getItem(position).toString().split(" ")[0];
				String prijs = listView.getAdapter().getItem(position).toString().split(" ")[1];
				
				Toast.makeText(getApplicationContext(),
						"Datum : " + datum + " (" + prijs + ")",
						Toast.LENGTH_LONG).show();

			}
		});
		listadapter = new CostumAttendancesAdapter(getApplicationContext(),valuelist);

		listView.setAdapter(listadapter);
		Bundle b = getIntent().getExtras();
		adminnaam.setText(b.getString("name"));
		String[] spinner = new String[3];
		spinner[0] = "Deze week";
		spinner[1] = "Deze maand";
		spinner[2] = "Alles          ";

		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayAdapter spinneradapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, spinner);
		selectperiod.setAdapter(spinneradapter);
		selectperiod.setSelection(2);
		selectperiod.setOnItemSelectedListener(
	            new AdapterView.OnItemSelectedListener() {

	                @Override
	                public void onItemSelected(AdapterView<?> arg0, View arg1,
	                        int index, long arg3) {
	                	if (index == 0){
	                		showThisWeek();
	                	}
	                	else if (index == 1){
	                		showThisMonth();
	                	}
	                	else {
	                		showAll();
	                	}
	                }

	                @Override
	                public void onNothingSelected(AdapterView<?> arg0) {
	                    // TODO Auto-generated method stub

	                }
	                //add some code here
	            }
	        );
	}
	
	public MemberDAO getMemberController(){
		return membercontroller;
	}
	
	public AttendanceDAO getAttendanceController(){
		return attendancecontroller;
	}
	
	
	private void showThisWeek(){
		List<String> valuelist = new ArrayList<String>();
		for (int i = 0; i < attendances.size(); i++) {
			Calendar date = attendances.get(i).getStartdate();
			Log.d("date","" + date.toString());
			Calendar weekstart, weekend;
			weekstart = Calendar.getInstance();
			
			while(weekstart.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY){
				weekstart.add(Calendar.DATE, -1);
			}
			weekstart.set(Calendar.HOUR_OF_DAY, 0);
			weekstart.set(Calendar.HOUR, 0);
			weekstart.set(Calendar.MINUTE, 0);
			weekstart.set(Calendar.SECOND, 0);
			weekstart.set(Calendar.MILLISECOND, 0);
			Log.d("date","" + weekstart.toString());
			weekend = Calendar.getInstance();
			weekend.set(Calendar.DATE, weekstart.get(Calendar.DATE));
			weekend.set(Calendar.MONTH, weekstart.get(Calendar.MONTH));
			weekend.add(Calendar.DATE,7);
			
			if (date.after(weekstart) && date.before(weekend))
				valuelist.add(attendances.get(i).toString());
			
		}

		listadapter = new CostumAttendancesAdapter(getApplicationContext(),valuelist);

		listView.setAdapter(listadapter);
		
	}
	
	
	
	private void showThisMonth(){
		List<String> valuelist = new ArrayList<String>();
		for (int i = 0; i < attendances.size(); i++) {
			Calendar date = attendances.get(i).getStartdate();
			Calendar currentdate = Calendar.getInstance();
			
			if (date.get(Calendar.MONTH) == currentdate.get(Calendar.MONTH))
				valuelist.add(attendances.get(i).toString());
			
		}
		
		listadapter = new CostumAttendancesAdapter(getApplicationContext(),valuelist);

		listView.setAdapter(listadapter);
		
			
		
	}
	
	@Override
	public void onBackPressed() {
	   Log.d("CDA", "onBackPressed Called");
	   Intent setIntent = new Intent(getApplicationContext(),AdminActivity.class);
	   setIntent.putExtra("name", adminnaam.getText().toString());
	   startActivity(setIntent);
	   ViewAttendancesActivity.this.finish();
	}
	
	private void showAll(){
		List<String> valuelist = new ArrayList<String>();
		for (Attendance a : attendances){
			valuelist.add(a.toString());
		}
		listadapter = new CostumAttendancesAdapter(getApplicationContext(),valuelist);

		listView.setAdapter(listadapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.member, menu);

		return true;
	}
	
}
