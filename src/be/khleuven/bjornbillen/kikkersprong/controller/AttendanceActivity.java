package be.khleuven.bjornbillen.kikkersprong.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import be.khleuven.bjornbillen.kikkersprong.controller.listadapter.CostumAttendanceListAdapter;
import be.khleuven.bjornbillen.kikkersprong.db.AttendanceDAO;
import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.model.Attendance;
import be.khleuven.bjornbillen.kikkersprong.model.Member;


import com.example.kikkersprong.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AttendanceActivity extends Activity {
	ListView listView;
	TextView membername, aanwezigheden;
	Spinner selectperiod;
	CostumAttendanceListAdapter listadapter;
	String[] values;
	List<Attendance> attendances;
	ArrayAdapter<String> adapter;
	AttendanceDAO attendancecontroller;
	MemberDAO membercontroller;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendance);
		attendancecontroller = AttendanceDAO.getInstance(getApplicationContext());
		membercontroller = MemberDAO.getInstance(getApplicationContext());
		Bundle bundle = getIntent().getExtras();
		int id = bundle.getInt("id");
		final Member m = getMemberController().getMember(id);
		listView = (ListView) findViewById(R.id.listViewAttendance);
		membername = (TextView) findViewById(R.id.membername);
		// Defined Array values to show in ListView
		selectperiod = (Spinner) findViewById(R.id.spinner1);
		attendances = new ArrayList<Attendance>();
		membername.setText(getMemberController().getMember(id).getFirstname() + " " + getMemberController().getMember(id).getLastname());
	
		attendances = getAttendanceController().getAttendances(getMemberController().getCurrentMemberID());
		showAll();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

							// Show Alert
				String datum = listView.getAdapter().getItem(position).toString().split(" ")[0];
				Attendance result = null;
				String aanwezigheid = "Niet gevonden";
				
				for (Attendance a : getAttendanceController().getAttendances(m.getId())){
					if (a.getStartdate().get(Calendar.DATE) == Integer.parseInt(datum.split("/")[2]) && (a.getStartdate().get(Calendar.MONTH)+1) == Integer.parseInt(datum.split("/")[1]) && a.getStartdate().get(Calendar.YEAR) == Integer.parseInt(datum.split("/")[0])){
						result = a;
					}
				}
				if (result != null){
				aanwezigheid = result.getStartdate().get(Calendar.HOUR_OF_DAY) + "u - " + result.getEnddate().get(Calendar.HOUR_OF_DAY) + "u";
				}
				Toast.makeText(getApplicationContext(),
						"Datum : " + datum + " (" + aanwezigheid + ")",
						Toast.LENGTH_LONG).show();

			}
		});

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

		listadapter = new CostumAttendanceListAdapter(getApplicationContext(),valuelist);

		listView.setAdapter(listadapter);
		
	}
	
	@Override
	public void onBackPressed() {
	   Log.d("CDA", "onBackPressed Called");
	   Intent setIntent = new Intent(getApplicationContext(),MemberActivity.class);
	   setIntent.putExtra("id", getMemberController().getCurrentMemberID());
	   startActivity(setIntent);
	   AttendanceActivity.this.finish();
	}
	
	private void showThisMonth(){
		List<String> valuelist = new ArrayList<String>();
		for (int i = 0; i < attendances.size(); i++) {
			Calendar date = attendances.get(i).getStartdate();
			Calendar currentdate = Calendar.getInstance();
			Log.d("test","" + date.get(Calendar.MONTH));
				
			
			if (date.get(Calendar.MONTH) == currentdate.get(Calendar.MONTH))
				valuelist.add(attendances.get(i).toString());
			
		}
		
		listadapter = new CostumAttendanceListAdapter(getApplicationContext(),valuelist);

		listView.setAdapter(listadapter);
		
			
		
	}
	
	private void showAll(){
		List<String> valuelist = new ArrayList<String>();
		for (Attendance a : attendances){
			valuelist.add(a.toString());
		}
		listadapter = new CostumAttendanceListAdapter(getApplicationContext(),valuelist);

		listView.setAdapter(listadapter);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.attendance, menu);
		return true;
	}
}
