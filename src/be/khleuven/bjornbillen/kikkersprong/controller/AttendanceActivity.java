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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AttendanceActivity extends Activity {
	ListView listView;
	AttendanceDAO attendancecontroller;
	MemberDAO membercontroller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendance);
		attendancecontroller = new AttendanceDAO(getApplicationContext());
		membercontroller = new MemberDAO(getApplicationContext());
		int id = membercontroller.getCurrentMemberID();
		Member m = membercontroller.getMember(id);
		listView = (ListView) findViewById(R.id.listView1);
		// Defined Array values to show in ListView
		String[] values;
		List<Attendance> attendances = new ArrayList<Attendance>();
		attendancecontroller.addAttendance(new Attendance(0,m,Calendar.getInstance(),Calendar.getInstance()));
        for (Attendance a : attendancecontroller.getAllAttendances()){
        	if (a.getMember().getId() == membercontroller.getCurrentMemberID()){
        		attendances.add(a);
        	}
        }
        values = new String[attendances.size()];
        for (int i = 0; i < attendances.size(); i++){
        	values[i] = attendances.get(i).toString();
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
          android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter); 
        
        // ListView Item Click Listener
        listView.setOnItemClickListener(new OnItemClickListener() {

              @Override
              public void onItemClick(AdapterView<?> parent, View view,
                 int position, long id) {
                
               // ListView Clicked item index
               int itemPosition     = position;
               
               // ListView Clicked item value
               String  itemValue    = (String) listView.getItemAtPosition(position);
                  
                // Show Alert 
                Toast.makeText(getApplicationContext(),
                  "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                  .show();
             
              }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.attendance, menu);
		return true;
	}
}
