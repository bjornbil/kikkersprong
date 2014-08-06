package be.khleuven.bjornbillen.kikkersprong.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import be.khleuven.bjornbillen.kikkersprong.db.AttendanceDAO;
import be.khleuven.bjornbillen.kikkersprong.db.MemberDAO;
import be.khleuven.bjornbillen.kikkersprong.model.Attendance;
import be.khleuven.bjornbillen.kikkersprong.model.Member;

import com.example.kikkersprong.R;
import com.example.kikkersprong.R.id;
import com.example.kikkersprong.R.layout;
import com.example.kikkersprong.R.menu;
import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
	AttendanceDAO attendancecontroller;
	MemberDAO membercontroller;
	String[] values;
	List<Attendance> attendances;
	ArrayAdapter<String> adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendance);
		attendancecontroller = new AttendanceDAO(getApplicationContext());
		membercontroller = new MemberDAO(getApplicationContext());
		int id = membercontroller.getCurrentMemberID();
		Member m = membercontroller.getMember(id);
		listView = (ListView) findViewById(R.id.listView1);
		membername = (TextView) findViewById(R.id.membername);
		// Defined Array values to show in ListView
		selectperiod = (Spinner) findViewById(R.id.spinner1);
		attendances = new ArrayList<Attendance>();
		membername.setText(membercontroller.getMember(id).getFirstname() + " " + membercontroller.getMember(id).getLastname());
	
		attendances = attendancecontroller.getAllAttendances();
		showAll();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// ListView Clicked item index
				int itemPosition = position;

				// ListView Clicked item value
				String itemValue = (String) listView
						.getItemAtPosition(position);

				// Show Alert
				Toast.makeText(getApplicationContext(),
						"Index :" + itemPosition + "  Datum : " + itemValue,
						Toast.LENGTH_LONG).show();

			}
		});

		String[] spinner = new String[3];
		spinner[0] = "Deze week";
		spinner[1] = "Deze maand";
		spinner[2] = "Alles";

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
		values = new String[valuelist.size()];
		for (int i = 0; i < valuelist.size(); i++){
			values[i] = valuelist.get(i);
		}
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);

		listView.setAdapter(adapter);
		listView.setVisibility(0);
		listView.setVisibility(1);
		Log.d("size",values.length + " ");
	}
	
	private void showThisMonth(){
		List<String> valuelist = new ArrayList<String>();
		for (int i = 0; i < attendances.size(); i++) {
			Calendar date = attendances.get(i).getStartdate();
			Calendar currentdate = Calendar.getInstance();
			if (date.get(Calendar.MONTH) == currentdate.get(Calendar.MONTH))
				valuelist.add(attendances.get(i).toString());
			
		}
		values = new String[valuelist.size()];
		for (int i = 0; i < valuelist.size(); i++){
			values[i] = valuelist.get(i);
		}
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);

		listView.setAdapter(adapter);
		
			
		
	}
	
	private void showAll(){
		values = new String[attendances.size()];
		for (int i = 0; i < attendances.size(); i++){
			values[i] = attendances.get(i).toString();
		}
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);

		listView.setAdapter(adapter);
		Log.d("size",values.length + " ");
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.attendance, menu);
		return true;
	}
}
